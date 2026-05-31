package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

sealed class Screen {
    object Dashboard : Screen()
    data class SubjectDetail(val subjectId: String) : Screen()
}

enum class ActiveTab(val labelFr: String, val labelAr: String) {
    LESSONS("Cours", "الدروس"),
    FLASHCARDS("Billes Flash", "بطاقات مراجعة"),
    EXAMS("Exams Nationaux", "تمارين وطنية"),
    QUIZ("Quiz Rapide", "اختبار"),
    AI_TUTOR("Coach IA", "مدرب الذكاء الاصطناعي")
}

data class ChatMessage(
    val sender: String, // "USER" or "AI"
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis()
)

class BacViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPrefs = application.getSharedPreferences("bac_study_prefs", Context.MODE_PRIVATE)

    // Navigation and subject tracking
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Dashboard)
    val currentScreen = _currentScreen.asStateFlow()

    private val _selectedSubject = MutableStateFlow<Subject?>(null)
    val selectedSubject = _selectedSubject.asStateFlow()

    private val _activeTab = MutableStateFlow(ActiveTab.LESSONS)
    val activeTab = _activeTab.asStateFlow()

    // Study statistics
    private val _studyStreak = MutableStateFlow(sharedPrefs.getInt("study_streak", 3))
    val studyStreak = _studyStreak.asStateFlow()

    private val _totalPoints = MutableStateFlow(sharedPrefs.getInt("total_points", 120))
    val totalPoints = _totalPoints.asStateFlow()

    private val _lessonProgress = MutableStateFlow<Set<String>>(
        sharedPrefs.getStringSet("lesson_progress", emptySet()) ?: emptySet()
    )
    val lessonProgress = _lessonProgress.asStateFlow()

    // Flashcard State
    private val _currentCardIndex = MutableStateFlow(0)
    val currentCardIndex = _currentCardIndex.asStateFlow()

    private val _isCardFlipped = MutableStateFlow(false)
    val isCardFlipped = _isCardFlipped.asStateFlow()

    // Quiz State
    private val _quizQuestionIndex = MutableStateFlow(0)
    val quizQuestionIndex = _quizQuestionIndex.asStateFlow()

    private val _selectedQuizAnswer = MutableStateFlow(-1)
    val selectedQuizAnswer = _selectedQuizAnswer.asStateFlow()

    private val _isQuizAnswerSubmitted = MutableStateFlow(false)
    val isQuizAnswerSubmitted = _isQuizAnswerSubmitted.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore = _quizScore.asStateFlow()

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished = _isQuizFinished.asStateFlow()

    // AI Tutor States
    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory = _chatHistory.asStateFlow()

    private val _isAiGenerating = MutableStateFlow(false)
    val isAiGenerating = _isAiGenerating.asStateFlow()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    init {
        // Initialize with default custom welcome message in AI Tutor
        resetChatHistory()
    }

    fun navigateToDashboard() {
        _currentScreen.value = Screen.Dashboard
        _selectedSubject.value = null
    }

    fun navigateToSubject(subjectId: String) {
        val subject = CurriculumData.subjects.find { it.id == subjectId }
        _selectedSubject.value = subject
        _activeTab.value = ActiveTab.LESSONS
        _currentScreen.value = Screen.SubjectDetail(subjectId)
        
        // Reset interactive sub-tabs
        resetFlashcardState()
        resetQuizState()
        resetChatHistory()
    }

    fun changeTab(tab: ActiveTab) {
        _activeTab.value = tab
    }

    fun resetFlashcardState() {
        _currentCardIndex.value = 0
        _isCardFlipped.value = false
    }

    // Flashcard modifiers
    fun nextFlashcard() {
        val subject = _selectedSubject.value ?: return
        if (subject.flashcards.isEmpty()) return
        _isCardFlipped.value = false
        _currentCardIndex.value = (_currentCardIndex.value + 1) % subject.flashcards.size
    }

    fun previousFlashcard() {
        val subject = _selectedSubject.value ?: return
        if (subject.flashcards.isEmpty()) return
        _isCardFlipped.value = false
        val total = subject.flashcards.size
        _currentCardIndex.value = (_currentCardIndex.value - 1 + total) % total
    }

    fun flipCard() {
        _isCardFlipped.value = !_isCardFlipped.value
    }

    fun bookmarkCardStudied() {
        _totalPoints.value += 10
        sharedPrefs.edit().putInt("total_points", _totalPoints.value).apply()
        nextFlashcard()
    }

    // Lesson Progress
    fun toggleLessonCompleted(lessonId: String) {
        val currentProgress = _lessonProgress.value.toMutableSet()
        if (currentProgress.contains(lessonId)) {
            currentProgress.remove(lessonId)
            _totalPoints.value = maxOf(0, _totalPoints.value - 15)
        } else {
            currentProgress.add(lessonId)
            _totalPoints.value += 15
            incrementStreak()
        }
        _lessonProgress.value = currentProgress
        sharedPrefs.edit()
            .putStringSet("lesson_progress", currentProgress)
            .putInt("total_points", _totalPoints.value)
            .apply()
    }

    private fun incrementStreak() {
        _studyStreak.value += 1
        sharedPrefs.edit().putInt("study_streak", _studyStreak.value).apply()
    }

    // Quiz Modifiers
    fun submitQuizAnswer(answerIndex: Int) {
        if (_isQuizAnswerSubmitted.value) return
        _selectedQuizAnswer.value = answerIndex
        _isQuizAnswerSubmitted.value = true

        val subject = _selectedSubject.value ?: return
        val currentQuestion = subject.quiz.getOrNull(_quizQuestionIndex.value) ?: return

        if (answerIndex == currentQuestion.correctAnswerIndex) {
            _quizScore.value += 1
            _totalPoints.value += 30
            sharedPrefs.edit().putInt("total_points", _totalPoints.value).apply()
        }
    }

    fun nextQuizQuestion() {
        val subject = _selectedSubject.value ?: return
        val nextIndex = _quizQuestionIndex.value + 1
        if (nextIndex < subject.quiz.size) {
            _quizQuestionIndex.value = nextIndex
            _selectedQuizAnswer.value = -1
            _isQuizAnswerSubmitted.value = false
        } else {
            _isQuizFinished.value = true
            incrementStreak()
        }
    }

    fun resetQuizState() {
        _quizQuestionIndex.value = 0
        _selectedQuizAnswer.value = -1
        _isQuizAnswerSubmitted.value = false
        _quizScore.value = 0
        _isQuizFinished.value = false
    }

    // AI Chat Modifiers
    fun resetChatHistory() {
        val sub = _selectedSubject.value
        val subjectContext = sub?.nameFr ?: "tous les sujets du Bac PC Maroc"
        val labelAr = sub?.nameAr ?: "جميع مواد الباكالوريا"
        val welcomeMsg = "Bonjour ! Je suis votre coach IA spécialisé en $subjectContext (2 Bac SM & PC).\n" +
                "Posez-moi des questions sur les équations de physique, limites, biochimie ou philo !\n" +
                "أهلاً بك! أنا مدربك الخاص للتحضير للامتحان الوطني للباكالوريا في $labelAr. سأسعد بتبسيط المعادلات الصعبة والشرح بالعربية !"
        _chatHistory.value = listOf(ChatMessage("AI", welcomeMsg))
    }

    fun sendAiTutorMessage(userText: String) {
        if (userText.trim().isEmpty() || _isAiGenerating.value) return

        // 1. Add User message to list
        val currentHistory = _chatHistory.value.toMutableList()
        currentHistory.add(ChatMessage("USER", userText))
        _chatHistory.value = currentHistory

        _isAiGenerating.value = true

        // 2. Perform background request to Gemini
        viewModelScope.launch {
            val systemPrompt = "You are an elite academic Coach for the Moroccan 2nd Baccalaureate Physical Sciences (2 Bac PC) curriculum. " +
                    "Explain scientific and philosophical subjects clearly in French. At the end, translate complex terms and explain heavy math derivations in Moroccan Arabic (Darija/Arabic) so that it is highly intuitive. " +
                    "Use crisp markdown with bold equations and guidelines. Current subject context: ${_selectedSubject.value?.nameFr ?: "All Bac PC subjects"}"

            try {
                val responseContent = callGeminiApiRest(userText, systemPrompt)
                val updatedHistory = _chatHistory.value.toMutableList()
                updatedHistory.add(ChatMessage("AI", responseContent))
                _chatHistory.value = updatedHistory
            } catch (e: Exception) {
                Log.e("BacViewModel", "Gemini call failed", e)
                val updatedHistory = _chatHistory.value.toMutableList()
                updatedHistory.add(ChatMessage("AI", "Désolé, une erreur s'est produite lors de la connexion au serveur d'IA. Veuillez vérifier votre connexion. (خطأ في الاتصال بالخادم الداخلي للمدرب)"))
                _chatHistory.value = updatedHistory
            } finally {
                _isAiGenerating.value = false
            }
        }
    }

    private suspend fun callGeminiApiRest(prompt: String, systemInstruction: String): String =
        withContext(Dispatchers.IO) {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                // If API key is placeholder, return a supportive simulated coaching response filled with knowledge!
                return@withContext getSimulatedResponse(prompt)
            }

            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", systemInstruction)
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                })
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            try {
                okHttpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        return@withContext "Erreur serveur: ${response.code}. Veuillez configurer votre clé API Gemini Correctement.\n(يرجى التأكد من مفتاح الـ API)"
                    }
                    val bodyString = response.body?.string() ?: return@withContext "Réponse vide"
                    val jsonResponse = JSONObject(bodyString)
                    val candidates = jsonResponse.getJSONArray("candidates")
                    if (candidates.length() > 0) {
                        val firstCandidate = candidates.getJSONObject(0)
                        val content = firstCandidate.getJSONObject("content")
                        val parts = content.getJSONArray("parts")
                        if (parts.length() > 0) {
                            return@withContext parts.getJSONObject(0).getString("text")
                        }
                    }
                    return@withContext "Impossible de lire la réponse du robot."
                }
            } catch (e: IOException) {
                return@withContext "Erreur réseau: ${e.localizedMessage}. Réessayez."
            }
        }

    private fun getSimulatedResponse(prompt: String): String {
        val lower = prompt.lowercase()
        val subjectContext = _selectedSubject.value?.id ?: "general"
        
        return when {
            lower.contains("onde") || lower.contains("wave") || lower.contains("سرعة") -> {
                "**[Coach IA - Physique Waves]**\n" +
                        "Pour déterminer la célérité d'une onde mécanique :\n" +
                        "$$\nv = \\frac{d}{\\Delta t}\n$$\n" +
                        "- **d** : Distance parcourue par le front d'onde (en m).\n" +
                        "- **\\Delta t** : Retard ou durée du parcours (en s).\n\n" +
                        "**الشرح بالعربية (Explication en Arabe) :**\n" +
                        "حساب سرعة انتشار موجة ميكانيكية يعتمد طردياً على المسافة المقطوعة وعكسياً على المدة الزمنية المستغرقة. انتبه في الامتحان الوطني من تحويل السنتيمتر إلى المتر بضرب القيمة في \$10^{-2}\$ !\n\n" +
                        "Est-ce que vous aimeriez faire un exercice pratique sur les ondes de la session 2023 ?"
            }
            lower.contains("complex") || lower.contains("عقد") || lower.contains("معيار") -> {
                "**[Coach IA - Math Complexes]**\n" +
                        "Un nombre complexe s'écrit sous la forme algébrique \$z = x + iy\$.\n" +
                        "Le module de \$z\$ est donné par : \n" +
                        "$$\n|z| = \\sqrt{x^2 + y^2}\n$$\n\n" +
                        "**الشرح بالعربية (Explication en Arabe) :**\n" +
                        "المعيار \$|z|\$ للمركب يمثل هندسياً المسافة من أصل المعلم إلى النقطة ذات الإحداثيات \$(x,y)\$. وهو دائماً عدد حقيقي موجب. لحل معادلة من الدرجة الثانية، احسب المميز ديلتا \$\\Delta = b^2 - 4ac\$ وإذا كان سالباً فإن الحلين مترافقان عقدياً \$z_1, z_2\$."
            }
            lower.contains("atp") || lower.contains("krebs") || lower.contains("ميتوك") -> {
                "**[Coach IA - SVT Énergie]**\n" +
                        "Le cycle de Krebs se déroule dans la matrice de la mitochondrie. Son équation totale produit du CO2, du NADH,H+, du FADH2 et de l'ATP.\n" +
                        "La phosphorylation d'ADP en ATP se produit au niveau des sphères pédonculées grâce au gradient de protons.\n\n" +
                        "**الشرح بالعربية (Explication en Arabe) :**\n" +
                        "تحدث دورة كريبس داخل الماتريس لإنتاج جزيئات حاملة للطاقة والـ ATP. بينما السلسلة التنفسية تحدث عبر الغشاء الداخلي للميتوكوندري، حيث تتدفق البروتونات لتعود عبر الكرات ذات شمراخ مفعلةً ديناميكية إنتاج الـ ATP."
            }
            lower.contains("autrui") || lower.contains("غير") || lower.contains("صراع") -> {
                "**[Coach IA - Philosophie]**\n" +
                        "Pour Sartre, l'existence d'autrui est indispensable à la constitution de mon propre ego. Le regard de l'autre me chosifie et pourtant me révèle à moi-même.\n\n" +
                        "**الشرح بالعربية (Explication en Arabe) :**\n" +
                        "مفهوم الغير يطرح مفارقة حيوية: وجود الغير قد يعتبره البعض مهدداً للحرية الشخصية (سارتر: الآخرون هم الجحيم)، ولكنه في ذات الوقت مرآة ضرورية لكي تكتمل معرفتي بذاتي الفردية والأخلاقية."
            }
            else -> {
                "**[Coach IA - Bac Maroc Expert]**\n" +
                        "Votre question est excellente ! Pour le niveau 2 Bac PC, il est fondamental de parfaitement maîtriser les relations d'examens nationaux.\n\n" +
                        "**نصيحة ذهبية بالدراجة للاستعداد :**\n" +
                        "احرص على كتابة القوانين كاملة بالرموز وتأطير نتائجك النهائية. إذا أردت التعمق، ابدأ بحل أسئلة الـ Quiz والتمارين المتوفرة في تبيوباتها العلوية وسأقوم بمساعدتك خطوة بخطوة !"
            }
        }
    }
}
