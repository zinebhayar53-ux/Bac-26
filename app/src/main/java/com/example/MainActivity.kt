package com.example

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.roundToInt
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("main_scaffold"),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    val viewModel: BacViewModel = viewModel()
                    MainAppNavHost(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainAppNavHost(
    viewModel: BacViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    Crossfade(
        targetState = currentScreen,
        animationSpec = tween(durationMillis = 350),
        modifier = modifier.fillMaxSize(),
        label = "screen_transition"
    ) { screen ->
        when (screen) {
            is Screen.Dashboard -> {
                DashboardScreen(viewModel = viewModel)
            }
            is Screen.SubjectDetail -> {
                SubjectDetailScreen(viewModel = viewModel)
            }
        }
    }
}

// -------------------------------------------------------------
// 1. DASHBOARD SCREEN
// -------------------------------------------------------------
@Composable
fun DashboardScreen(viewModel: BacViewModel) {
    val totalPoints by viewModel.totalPoints.collectAsState()
    val studyStreak by viewModel.studyStreak.collectAsState()
    val lessonProgress by viewModel.lessonProgress.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Header & Branding (Crimson - Neon Blue and Black Gloss Glow)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "DOUROUS BAC PC",
                    fontSize = 28.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag("app_logo_title")
                )
                Text(
                    text = "2 Bac Sciences Physiques • Maroc",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            // Streak & XP Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Streak
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x22FF1744))
                        .border(1.dp, Color(0xFFFF1744), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = Color(0xFFFF1744),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$studyStreak j.",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Points XP
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x2200E5FF))
                        .border(1.dp, Color(0xFF00E5FF), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Points XP",
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$totalPoints XP",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Large Motivational Target (Glossy glass, black & neon)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF141520), Color(0xFF0A0A0E))
                    )
                )
                .border(2.dp, Color(0x66FF1744), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Préparez votre National 2026 🎯",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Toutes les matières scientifiques en français, avec explications approfondies, flashcards intelligentes, exercices interactifs d'examens et support en arabe.",
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Progress Info
                val chaptersDone = lessonProgress.size
                val totalChapters = 7 // total chapters across curriculum
                val pct = if (totalChapters > 0) (chaptersDone.toFloat() / totalChapters.toFloat()) else 0f
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Maîtrise du Programme : $chaptersDone / $totalChapters Chapitres",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "${(pct * 100).roundToInt()}%",
                        fontSize = 11.sp,
                        color = Color(0xFF00E5FF),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { pct },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = Color(0xFF00E5FF),
                    trackColor = Color(0xFF222630)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Main List Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Choisir une Matière (المواد الدراسية) :",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Subject selector cards
        CurriculumData.subjects.forEachIndexed { index, subject ->
            val gradientBorder = if (index % 2 == 0) {
                Brush.horizontalGradient(listOf(Color(0xFFFF1744), Color(0xFF06060A)))
            } else {
                Brush.horizontalGradient(listOf(Color(0xFF00E5FF), Color(0xFF06060A)))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF10121A))
                    .border(
                        width = 1.5.dp,
                        brush = if (index % 2 == 0) Brush.sweepGradient(listOf(Color(0xFFFF1744), Color(0xFF10121A), Color(0xFF00E5FF))) else Brush.linearGradient(listOf(Color(0xFF00E5FF), Color(0xFF10121A))),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { viewModel.navigateToSubject(subject.id) }
                    .testTag("subject_card_${subject.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Custom Dynamic Subject Icon box
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (index % 2 == 0) Color(0x33FF1744) else Color(0x3300E5FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = mapIconToSubject(subject.id),
                                contentDescription = subject.nameFr,
                                tint = if (index % 2 == 0) Color(0xFFFF1744) else Color(0xFF00E5FF),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Text Descriptions
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = subject.nameFr,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = subject.nameAr,
                                    fontSize = 12.sp,
                                    color = Color(0xFF00E5FF),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = subject.description,
                                fontSize = 11.sp,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Arrow Icon
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Entrer",
                        tint = Color.LightGray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        
        // Footer Credits
        Text(
            text = "Dourous Bac PC v1.2 • Conforme aux spécifications du Ministère d'Éducation Marocain",
            fontSize = 10.sp,
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

private fun mapIconToSubject(id: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (id) {
        "pc" -> Icons.Default.Science
        "math" -> Icons.Default.Functions
        "svt" -> Icons.Default.Biotech
        "philo" -> Icons.Default.Psychology
        "english" -> Icons.Default.Translate
        else -> Icons.Default.Book
    }
}

// -------------------------------------------------------------
// 2. SUBJECT DETAILS & TABS
// -------------------------------------------------------------
@Composable
fun SubjectDetailScreen(viewModel: BacViewModel) {
    val subject by viewModel.selectedSubject.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()

    val currentSub = subject ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF04050A))
    ) {
        // TOP Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0A0B12))
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = { viewModel.navigateToDashboard() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0x11FFFFFF))
                        .testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour",
                        tint = Color.White
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currentSub.nameFr,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = currentSub.nameAr,
                            color = Color(0xFF00E5FF),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Examens Nationaux & Synthèse",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }

            // Simple active state marker
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0x2200E5FF))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "PC PRO",
                    fontSize = 10.sp,
                    color = Color(0xFF00E5FF),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Subnavigation row matching ActiveTab (Glossy Slider tabs)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0D0F18))
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActiveTab.values().forEach { tab ->
                val isActive = activeTab == tab
                val containerColor = if (isActive) Color(0xFFFF1744) else Color(0xFF141624)
                val strokeColor = if (isActive) Color(0xFFFF1744) else Color(0xFF1E2134)
                val textColor = if (isActive) Color.White else Color.LightGray

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(containerColor)
                        .border(1.dp, strokeColor, RoundedCornerShape(12.dp))
                        .clickable { viewModel.changeTab(tab) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("tab_${tab.name}")
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = tab.labelFr,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = tab.labelAr,
                            color = if (isActive) Color.White else Color.Gray,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = Color(0xFF181B26), thickness = 1.dp)

        // Contents switching based on Active Tab
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (activeTab) {
                ActiveTab.LESSONS -> LessonsTabContent(viewModel = viewModel, subject = currentSub)
                ActiveTab.FLASHCARDS -> FlashcardsTabContent(viewModel = viewModel, subject = currentSub)
                ActiveTab.EXAMS -> ExamsTabContent(viewModel = viewModel, subject = currentSub)
                ActiveTab.QUIZ -> QuizTabContent(viewModel = viewModel, subject = currentSub)
                ActiveTab.AI_TUTOR -> AiTutorTabContent(viewModel = viewModel, subject = currentSub)
            }
        }
    }
}

// -------------------------------------------------------------
// 2A. TAB - COURS (Detailed Lessons lists & expandable charts)
// -------------------------------------------------------------
@Composable
fun LessonsTabContent(viewModel: BacViewModel, subject: Subject) {
    val lessonProgress by viewModel.lessonProgress.collectAsState()
    var expandedLessonId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Résumé approfondi du cours (ملخصات مركزة) :",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }

        items(subject.lessons) { lesson ->
            val isExpanded = expandedLessonId == lesson.id
            val isCompleted = lessonProgress.contains(lesson.id)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF121422))
                    .border(
                        width = 1.dp,
                        color = if (isCompleted) Color(0xFF00E5FF) else if (isExpanded) Color(0xFFFF1744) else Color(0xFF222538),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Title Bar clickable
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedLessonId = if (isExpanded) null else lesson.id
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            // Check icon representing completeness
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (isCompleted) Color(0x3300E5FF) else Color(0x11FFFFFF))
                                    .clickable { viewModel.toggleLessonCompleted(lesson.id) }
                                    .testTag("complete_checkbox_${lesson.id}"),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Completed",
                                        tint = Color(0xFF00E5FF),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Text(
                                text = lesson.title,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Ouvrir",
                            tint = Color.LightGray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = lesson.summary,
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0x22FFFFFF), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Full Content
                        Text(
                            text = "Détails essentiels du programme :",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF1744)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = lesson.fullContent,
                            color = Color(0xFFECEFF1),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        // If there is a diagram, render the interactive Vector visual builder!
                        if (lesson.diagramType != "none") {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Mise en situation interactive / Représentation :",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00E5FF)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            InteractiveDiagramBox(diagramType = lesson.diagramType)
                        }

                        // Rendering the table formulas
                        if (lesson.formulas.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Relations Mathématiques Clés (صيغ وطنية) :",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00E5FF)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            lesson.formulas.forEach { (expr, desc) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF0B0C12))
                                        .border(1.dp, Color(0x2200E5FF), RoundedCornerShape(8.dp))
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = expr,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 14.sp,
                                        color = Color(0xFFFF1744),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = desc,
                                        fontSize = 11.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.weight(1f).padding(start = 12.dp)
                                    )
                                }
                            }
                        }

                        // Explication en Arabe Box
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1DFFA2A2))
                                .border(1.dp, Color(0x40FF1744), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                                        contentDescription = "Arabe",
                                        tint = Color(0xFFFF5252),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "الشرح بالدراجة والترجمة (Explication Vocabulaire) :",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFF8A80)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = lesson.arabicExplanations,
                                    fontSize = 12.sp,
                                    color = Color(0xFFFFEBEE),
                                    lineHeight = 18.sp,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Button to toggle complete
                        Button(
                            onClick = { viewModel.toggleLessonCompleted(lesson.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCompleted) Color(0xFFFF1744) else Color(0xFF00E5FF),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Default.Close else Icons.Default.Check,
                                contentDescription = "Valid",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isCompleted) "Marquer non révisé" else "Valider comme REVISÉ (+15 XP)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Interactive Jetpack Compose Canvas Diagrams for Study
// -------------------------------------------------------------
@Composable
fun InteractiveDiagramBox(diagramType: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF080910))
            .border(1.dp, Color(0xFF1E2134), RoundedCornerShape(12.dp))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        when (diagramType) {
            "WAVE_PROPAGATION" -> WavePropagationDiagram()
            "RADIOACTIVE_DECAY" -> RadioactiveDecayDiagram()
            "ACID_BASE_PREDOMINANCE" -> AcidBasePredominanceDiagram()
            "COMPLEX_PLANE" -> ComplexPlaneDiagram()
            "MITOCHONDRIA_ENERGY" -> MitochondriaEnergyDiagram()
            "TIMELINE_TENSES" -> TimelineTensesDiagram()
            else -> {
                Text(
                    text = "Aucune représentation disponible.",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun WavePropagationDiagram() {
    // Live anim sinusoidal wave
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val centerY = height / 2f
        val amplitude = height * 0.25f
        val wavelength = width / 2.5f

        // Draw coordinate grid lines
        drawLine(
            color = Color(0x33FFFFFF),
            start = Offset(0f, centerY),
            end = Offset(width, centerY),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = Color(0x33FFFFFF),
            start = Offset(40f, 0f),
            end = Offset(40f, height),
            strokeWidth = 1.dp.toPx()
        )

        // Wave Path
        val path = Path()
        for (xp in 0..width.toInt()) {
            val radians = (xp / wavelength) * (2 * PI) - waveOffset
            val yp = centerY + amplitude * sin(radians).toFloat()
            if (xp == 0) {
                path.moveTo(xp.toFloat(), yp)
            } else {
                path.lineTo(xp.toFloat(), yp)
            }
        }

        drawPath(
            path = path,
            color = Color(0xFF00E5FF),
            style = Stroke(width = 3.dp.toPx())
        )

        // Draw wave markers
        drawCircle(
            color = Color(0xFFFF1744),
            radius = 6.dp.toPx(),
            center = Offset(width * 0.4f, centerY + amplitude * sin((width * 0.4f / wavelength) * (2 * PI) - waveOffset).toFloat())
        )
    }
}

@Composable
fun RadioactiveDecayDiagram() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Axis
        drawLine(
            color = Color.Gray,
            start = Offset(20f, height - 20f),
            end = Offset(width - 20f, height - 20f),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = Color.Gray,
            start = Offset(20f, 20f),
            end = Offset(20f, height - 20f),
            strokeWidth = 2.dp.toPx()
        )

        // Exponential curve
        val path = Path()
        val startX = 20f
        val startY = 40f
        val curveWidth = width - 40f
        val curveHeight = height - 60f

        path.moveTo(startX, startY)
        for (i in 0..curveWidth.toInt()) {
            val xp = startX + i
            val progress = i / curveWidth
            val yp = startY + curveHeight * (1f - exp(-3.5f * progress))
            path.lineTo(xp, yp)
        }

        drawPath(
            path = path,
            color = Color(0xFFFF1744),
            style = Stroke(width = 2.5.dp.toPx())
        )

        // Axis annotations markers
        // t_1/2 mark at progress ≈ 0.2
        val halfLifeX = startX + curveWidth * 0.2f
        val halfLifeY = startY + curveHeight * 0.5f

        // Draw dotted lines for half-life
        drawLine(
            color = Color(0x7700E5FF),
            start = Offset(halfLifeX, height - 20f),
            end = Offset(halfLifeX, halfLifeY),
            strokeWidth = 1.5.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    }
}

@Composable
fun AcidBasePredominanceDiagram() {
    // Draggable chemical physical laboratory simulator!
    var phValue by remember { mutableStateOf(4.8f) }
    val pKa = 4.8f

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Bar diagram
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .clip(RoundedCornerShape(8.dp))
                .drawBehind {
                    val width = size.width
                    val height = size.height
                    val pKaPercent = pKa / 14f

                    // Draw Acid predominance region (Warm soft red)
                    drawRect(
                        color = Color(0xAAFF1744),
                        size = androidx.compose.ui.geometry.Size(width * pKaPercent, height)
                    )
                    // Draw Base predominance region (Neon Blue glass overlay)
                    drawRect(
                        color = Color(0xAA00E5FF),
                        topLeft = Offset(width * pKaPercent, 0f),
                        size = androidx.compose.ui.geometry.Size(width * (1f - pKaPercent), height)
                    )

                    // Draw pKa boundary line
                    drawLine(
                        color = Color.White,
                        start = Offset(width * pKaPercent, 0f),
                        end = Offset(width * pKaPercent, height),
                        strokeWidth = 3.dp.toPx()
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("[AH] prédomine (حمض)", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Text("pH = pKa ($pKa)", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Black)
                Text("[A-] prédomine (قاعدة)", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Interactivity slider
        Slider(
            value = phValue,
            onValueChange = { phValue = it },
            valueRange = 0f..14f,
            steps = 140,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color(0xFF00E5FF),
                inactiveTrackColor = Color.DarkGray
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("pH de votre solution : ${String.format("%.1f", phValue)}", fontSize = 11.sp, color = Color.White)
            val descAr = if (phValue < pKa) "الصفة الحمضية مهيمنة" else "الصفة القاعدية مهيمنة"
            Text(descAr, fontSize = 11.sp, color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ComplexPlaneDiagram() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = height * 0.35f

        // Horizontal axis (Re) & Vertical axis (Im)
        drawLine(color = Color.Gray, start = Offset(10f, centerY), end = Offset(width - 10f, centerY), strokeWidth = 1.5.dp.toPx())
        drawLine(color = Color.Gray, start = Offset(centerX, 10f), end = Offset(centerX, height - 10f), strokeWidth = 1.5.dp.toPx())

        // Unit circle bounding
        drawCircle(
            color = Color(0x22FFFFFF),
            radius = radius,
            center = Offset(centerX, centerY),
            style = Stroke(width = 1.dp.toPx())
        )

        // Dynamic complex number representation (z = a + ib = r.e^iθ)
        // Set fixed z at z = radius_len.e^(i*pi/4) (e.g. θ = 45°)
        val angleRad = PI / 4.0
        val zX = centerX + radius * cos(angleRad).toFloat()
        val zY = centerY - radius * sin(angleRad).toFloat()

        // Vector arrow representing complex number
        drawLine(
            color = Color(0xFF00E5FF),
            start = Offset(centerX, centerY),
            end = Offset(zX, zY),
            strokeWidth = 3.dp.toPx()
        )

        // Point z
        drawCircle(
            color = Color(0xFFFF1744),
            radius = 5.dp.toPx(),
            center = Offset(zX, zY)
        )

        // Dotted coordinates projections
        drawLine(
            color = Color.DarkGray,
            start = Offset(zX, zY),
            end = Offset(zX, centerY),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
        )
        drawLine(
            color = Color.DarkGray,
            start = Offset(zX, zY),
            end = Offset(centerX, zY),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
        )
    }
}

@Composable
fun MitochondriaEnergyDiagram() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        // Draw outer membrane ellipse
        drawCircle(
            color = Color(0xFF37474F),
            center = Offset(centerX, centerY),
            radius = height * 0.45f,
            style = Stroke(width = 3.dp.toPx())
        )

        // Draw folded inner membrane cristea path
        val innerPath = Path().apply {
            val hRad = height * 0.35f
            val wRad = width * 0.4f
            
            moveTo(centerX - wRad + 30, centerY)
            cubicTo(
                centerX - wRad/2, centerY - hRad,
                centerX - wRad/2, centerY + hRad,
                centerX, centerY
            )
            cubicTo(
                centerX + wRad/2, centerY - hRad,
                centerX + wRad/2, centerY + hRad,
                centerX + wRad - 30, centerY
            )
        }

        drawPath(
            path = innerPath,
            color = Color(0xFFFF1744),
            style = Stroke(width = 2.dp.toPx())
        )

        // Draw shiny glucose and ATP indicator bubbles
        drawCircle(
            color = Color(0xFF00E5FF),
            radius = 12f,
            center = Offset(centerX - 80, centerY - 20)
        )
        drawCircle(
            color = Color(0xFFFFEB3B),
            radius = 16f,
            center = Offset(centerX + 80, centerY + 15)
        )
    }
}

@Composable
fun TimelineTensesDiagram() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val centerY = height / 2f

        // Draw Main Timeline horizontal line
        drawLine(
            color = Color(0xFF37474F),
            start = Offset(20f, centerY),
            end = Offset(width - 20f, centerY),
            strokeWidth = 4.dp.toPx()
        )

        // Draw indicators
        // Past Perfect
        drawCircle(
            color = Color(0xFFFF1744),
            radius = 16f,
            center = Offset(width * 0.25f, centerY)
        )

        // Now flag
        drawLine(
            color = Color.White,
            start = Offset(width * 0.5f, centerY - 30f),
            end = Offset(width * 0.5f, centerY + 30f),
            strokeWidth = 3.dp.toPx()
        )

        // Future Perfect
        drawCircle(
            color = Color(0xFF00E5FF),
            radius = 16f,
            center = Offset(width * 0.75f, centerY)
        )
    }
}

// -------------------------------------------------------------
// 2B. TAB - FLASHCARDS (3D Flip, Swipe offset and drag support)
// -------------------------------------------------------------
@Composable
fun FlashcardsTabContent(viewModel: BacViewModel, subject: Subject) {
    val currentCardIndex by viewModel.currentCardIndex.collectAsState()
    val isCardFlipped by viewModel.isCardFlipped.collectAsState()

    val totalCards = subject.flashcards.size
    if (totalCards == 0) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Aucune carte dans cette unité.", color = Color.Gray)
        }
        return
    }

    val currentCard = subject.flashcards[currentCardIndex]

    // Animation transition variables for 3d rotation (flipping)
    val cardFlipRotation by animateFloatAsState(
        targetValue = if (isCardFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "rotation_card"
    )

    // Swipe Offset and Drag variables
    var cardSwipeOffset by remember { mutableStateOf(0f) }
    val animatedSwipeOffset by animateFloatAsState(
        targetValue = cardSwipeOffset,
        animationSpec = spring(),
        label = "swipe_offset"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Glissez la carte à Droit pour Valider ou cliquez pour révéler la formule ! " +
                    "(اسحب لليمين للحفظ، أو اضغط لكشف السر)",
            fontSize = 11.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        // The Flash Card Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .offset { IntOffset(animatedSwipeOffset.roundToInt(), 0) }
                .graphicsLayer {
                    rotationY = cardFlipRotation
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            cardSwipeOffset += dragAmount.x
                        },
                        onDragEnd = {
                            if (cardSwipeOffset > 250f) {
                                // Swiped right -> Retained master points
                                viewModel.bookmarkCardStudied()
                            } else if (cardSwipeOffset < -250f) {
                                // Swiped left -> Revise / Skip
                                viewModel.nextFlashcard()
                            }
                            cardSwipeOffset = 0f
                        }
                    )
                }
                .clickable { viewModel.flipCard() }
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF0A0C16))
                .border(
                    width = 2.dp,
                    color = if (isCardFlipped) Color(0xFFFF1744) else Color(0xFF00E5FF),
                    shape = RoundedCornerShape(24.dp)
                )
                .testTag("flashcard_box"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .graphicsLayer {
                        // Correct mirrors of text content upon rotation
                        rotationY = if (cardFlipRotation > 90f) 180f else 0f
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Category Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isCardFlipped) Color(0x22FF1744) else Color(0x2200E5FF))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = currentCard.category.uppercase(),
                        fontSize = 10.sp,
                        color = if (isCardFlipped) Color(0xFFFF1744) else Color(0xFF00E5FF),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Question or Answer Text
                Text(
                    text = if (cardFlipRotation < 90f) currentCard.question else currentCard.answer,
                    fontSize = if (isCardFlipped) 14.sp else 18.sp,
                    fontWeight = if (isCardFlipped) FontWeight.Medium else FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Translation guide panel
                if (cardFlipRotation < 90f) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x0EFFFFFF))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "💡 تلميح : " + currentCard.hintArabic,
                            fontSize = 12.sp,
                            color = Color(0xFFA0C4FF),
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    Text(
                        text = "Toucher pour retourner (اضغط للقلب)",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }

        // Card Indexing Progress bar
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Carte : ${currentCardIndex + 1} / $totalCards",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { (currentCardIndex + 1) / totalCards.toFloat() },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(6.dp)
                    .clip(CircleShape),
                color = Color(0xFFFF1744),
                trackColor = Color(0xFF1B2030)
            )
        }

        // TACTILE ACTIONS BUTTON BAR
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.nextFlashcard() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("fc_prev_button"),
                border = BorderStroke(1.5.dp, Color(0xFFFF1744)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Passer", color = Color(0xFFFF1744), fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.bookmarkCardStudied() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("fc_mastered_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Bien Retenu (+10 XP)", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// 2C. TAB - EXAMS NATIONAUX (Challenging exam practice challenges)
// -------------------------------------------------------------
@Composable
fun ExamsTabContent(viewModel: BacViewModel, subject: Subject) {
    var expandedSolutionId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Problèmes types des examens nationaux précédents :",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }

        items(subject.examExercises) { exercise ->
            val isSolutionVisible = expandedSolutionId == exercise.id

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F111E))
                    .border(1.dp, Color(0xFF202336), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    // Header with year
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = exercise.examYear,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFF1744)
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33FF1744))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = exercise.difficulty,
                                fontSize = 10.sp,
                                color = Color(0xFFFF1744),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = exercise.questionText,
                        fontSize = 13.sp,
                        color = Color.White,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Astuce/Tip box in Arabic
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x2200E5FF))
                            .border(1.dp, Color(0x4000E5FF), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = exercise.arabicTips,
                            fontSize = 12.sp,
                            color = Color(0xFFE0F7FA),
                            lineHeight = 18.sp,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Show solution toggle button
                    Button(
                        onClick = {
                            expandedSolutionId = if (isSolutionVisible) null else exercise.id
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSolutionVisible) Color(0xFF1E2134) else Color(0xFF00E5FF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (isSolutionVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Solution",
                            tint = if (isSolutionVisible) Color.White else Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isSolutionVisible) "Masquer les étapes" else "Afficher la Correction Pas-à-Pas",
                            fontWeight = FontWeight.Bold,
                            color = if (isSolutionVisible) Color.White else Color.Black,
                            fontSize = 12.sp
                        )
                    }

                    // Expandable Correction steps
                    AnimatedVisibility(
                        visible = isSolutionVisible,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(
                                text = "Correction détaillée :",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00E5FF)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            exercise.solutionSteps.forEachIndexed { iNum, step ->
                                Row(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFF1744)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${iNum + 1}",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Text(
                                        text = step,
                                        fontSize = 12.sp,
                                        color = Color.LightGray,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2D. TAB - QUIZ (Interactive knowledge check exams)
// -------------------------------------------------------------
@Composable
fun QuizTabContent(viewModel: BacViewModel, subject: Subject) {
    val quizQuestionIndex by viewModel.quizQuestionIndex.collectAsState()
    val selectedQuizAnswer by viewModel.selectedQuizAnswer.collectAsState()
    val isQuizAnswerSubmitted by viewModel.isQuizAnswerSubmitted.collectAsState()
    val quizScore by viewModel.quizScore.collectAsState()
    val isQuizFinished by viewModel.isQuizFinished.collectAsState()

    val totalQuestions = subject.quiz.size

    if (totalQuestions == 0) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Prochainement : Des quiz de révision rapides.", color = Color.Gray)
        }
        return
    }

    if (isQuizFinished) {
        // Success complete UI view
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0x3300E5FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Gagné",
                    tint = Color(0xFF00E5FF),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Quiz Terminé ! 🎉",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Votre Résultat pour ${subject.nameFr}:",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "$quizScore / $totalQuestions",
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFFF1744)
            )

            val passedAll = quizScore == totalQuestions
            Text(
                text = if (passedAll) "Excellent ! Un sans-faute digne du National." else "Pas mal, entraînez-vous pour atteindre le 20/20 !",
                fontSize = 13.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x2200E5FF))
                    .padding(14.dp)
            ) {
                Text(
                    text = "ممتاز! الامتحان الوطني بانتظارك لتصنع تفوقك. كرر التمرين لترسيخ المعادلات والروابط.",
                    fontSize = 12.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { viewModel.resetQuizState() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Recommencer l'évaluation", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
        return
    }

    val currentQuestion = subject.quiz[quizQuestionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progression Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question ${quizQuestionIndex + 1} sur $totalQuestions",
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Score : $quizScore",
                fontSize = 12.sp,
                color = Color(0xFF00E5FF),
                fontWeight = FontWeight.Bold
            )
        }

        LinearProgressIndicator(
            progress = { (quizQuestionIndex + 1) / totalQuestions.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = Color(0xFFFF1744),
            trackColor = Color(0xFF161924)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Question display card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF10121F))
                .border(1.dp, Color(0xFF22263C), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Text(
                text = currentQuestion.question,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 22.sp
            )
        }

        // List Options buttons
        currentQuestion.options.forEachIndexed { iIndex, option ->
            val isSelected = selectedQuizAnswer == iIndex
            val optionColor = when {
                !isQuizAnswerSubmitted -> {
                    if (isSelected) Color(0xFF00E5FF) else Color(0xFF1E2134)
                }
                iIndex == currentQuestion.correctAnswerIndex -> {
                    Color(0xFF4CAF50) // Green for correct
                }
                isSelected -> {
                    Color(0xFFFF1744) // Red for wrong selected
                }
                else -> {
                    Color(0xFF1E2134)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0B0D18))
                    .border(1.5.dp, optionColor, RoundedCornerShape(12.dp))
                    .clickable { viewModel.submitQuizAnswer(iIndex) }
                    .padding(16.dp)
                    .testTag("quiz_option_$iIndex"),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) optionColor else Color(0x33FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ('A' + iIndex).toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSelected) Color.Black else Color.White
                        )
                    }

                    Text(
                        text = option,
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Show correction explanation if submitted
        AnimatedVisibility(
            visible = isQuizAnswerSubmitted,
            enter = slideInVertically() + fadeIn()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1B1D2C))
                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val isCorrect = selectedQuizAnswer == currentQuestion.correctAnswerIndex
                        Icon(
                            imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = "Status",
                            tint = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFFF1744)
                        )
                        Text(
                            text = if (isCorrect) "Excellente réponse !" else "Correction et Analyse :",
                            color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFFF1744),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = currentQuestion.explanationFr,
                        fontSize = 12.sp,
                        color = Color.LightGray,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color(0x1AFFFFFF), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "التوضيح العلمي بالعربية : " + currentQuestion.explanationAr,
                        fontSize = 12.sp,
                        color = Color(0xFFA5D6A7),
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.nextQuizQuestion() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (quizQuestionIndex + 1 < totalQuestions) "Question suivante" else "Voir mes résultats",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2E. TAB - AI TUTOR & COACH (Moroccan Bac API conversational agent)
// -------------------------------------------------------------
@Composable
fun AiTutorTabContent(viewModel: BacViewModel, subject: Subject) {
    val chatHistory by viewModel.chatHistory.collectAsState()
    val isAiGenerating by viewModel.isAiGenerating.collectAsState()
    var userDraftMsg by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Quick help action prompts
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val suggestions = when (subject.id) {
                "pc" -> listOf("Formule onde ?", "Demi-vie t1/2 ?", "Ka et Henderson ?")
                "math" -> listOf("Calcul Limite ?", "Module complexe ?", "Théorème TVI ?")
                "svt" -> listOf("Cycle Krebs ?", "Bilan ATP ?", "Fermentation ?")
                "philo" -> listOf("Autrui Sartre ?", "Cogito Descartes ?", "Lutte Maître-Esclave")
                else -> listOf("Examen phrasal verbs ?", "Past vs Future perfect ?", "Conseils National")
            }

            suggestions.forEach { sug ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x19FFFFFF))
                        .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                        .clickable { viewModel.sendAiTutorMessage(sug) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = sug,
                        fontSize = 11.sp,
                        color = Color(0xFF00E5FF),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Message Feed list
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF080911))
                .border(1.dp, Color(0xFF171A2E), RoundedCornerShape(16.dp))
                .padding(10.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                reverseLayout = false
            ) {
                items(chatHistory) { chatMsg ->
                    val isAi = chatMsg.sender == "AI"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isAi) Arrangement.Start else Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 12.dp,
                                        topEnd = 12.dp,
                                        bottomStart = if (isAi) 0.dp else 12.dp,
                                        bottomEnd = if (isAi) 12.dp else 0.dp
                                    )
                                )
                                .background(if (isAi) Color(0xFF131526) else Color(0xFFFF1744))
                                .border(
                                    width = 1.dp,
                                    color = if (isAi) Color(0xFF282C4E) else Color(0xFFFF1744),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = if (isAi) "🤖 Coach IA (المدرب الذكي)" else "👨‍🎓 Candidat au Bac",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isAi) Color(0xFF00E5FF) else Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = chatMsg.messageText,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                if (isAiGenerating) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color(0xFF00E5FF),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Réflexion en cours... (جاري التفكير والتفسير)",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input entry section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = userDraftMsg,
                onValueChange = { userDraftMsg = it },
                placeholder = { Text("Poser une question en arabe ou français...", color = Color.Gray, fontSize = 12.sp) },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .testTag("chat_input"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF15192D),
                    unfocusedContainerColor = Color(0xFF0F111E),
                    focusedIndicatorColor = Color(0xFF00E5FF),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                maxLines = 2,
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
            )

            Button(
                onClick = {
                    viewModel.sendAiTutorMessage(userDraftMsg)
                    userDraftMsg = ""
                },
                modifier = Modifier
                    .width(72.dp)
                    .height(48.dp)
                    .testTag("send_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1744)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Envoyer",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
