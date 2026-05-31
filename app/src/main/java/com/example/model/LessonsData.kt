package com.example.model

data class Lesson(
    val id: String,
    val title: String,
    val summary: String,
    val fullContent: String,
    val arabicExplanations: String, // Arabic translation and guidelines for tricky parts
    val diagramType: String = "none", // Special custom jetpack canvas diagram
    val formulas: List<Pair<String, String>> = emptyList()
)

data class FlashCard(
    val id: String,
    val question: String,
    val answer: String,
    val hintArabic: String,
    val category: String
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanationFr: String,
    val explanationAr: String
)

data class NationalExamExercise(
    val id: String,
    val examYear: String,
    val questionText: String,
    val solutionSteps: List<String>,
    val arabicTips: String,
    val difficulty: String = "Difficile"
)

data class Subject(
    val id: String,
    val nameFr: String,
    val nameAr: String,
    val description: String,
    val lessons: List<Lesson>,
    val flashcards: List<FlashCard>,
    val quiz: List<QuizQuestion>,
    val examExercises: List<NationalExamExercise>
)

object CurriculumData {
    val subjects = listOf(
        Subject(
            id = "pc",
            nameFr = "Physique-Chimie",
            nameAr = "الفيزياء والكيمياء",
            description = "Ondes, Nucléaire, Électricité, Mécanique, Acide-Base & Évolution Chimique.",
            lessons = listOf(
                Lesson(
                    id = "pc_waves",
                    title = "Ondes Mécaniques Progressives",
                    summary = "Phénomène de propagation d'une perturbation dans un milieu matériel élastique, sans transport de matière mais avec transport d'énergie.",
                    fullContent = "1. Types d'ondes:\n- Ondes transversales: perturbation perpendiculaire à la direction de propagation (ex: onde sur une corde, ondes à la surface de l'eau).\n- Ondes longitudinales: perturbation parallèle à la direction de propagation (ex: onde sonore dans l'air, compression d'un ressort).\n\n2. Vitesse de propagation (Célérité):\nv = d / Δt (m/s). Elle dépend uniquement des propriétés du milieu (rigidité, inertie, température) et non de la forme de la perturbation.\n- Pour une corde: v = √(F/μ) où F est la tension (N) et μ la masse linéique (kg/m).\n\n3. Retard temporel:\nL'onde atteint le point M2 à l'instant t2 après avoir traversé le point M1 à l'instant t1. Le retard temporel τ est:\nτ = t2 - t1 = M1M2 / v.\nLa relation entre l'état de perturbation de M2 et M1 est:\ny_M2(t) = y_M1(t - τ).",
                    arabicExplanations = "موجات ميكانيكية متتالية:\n• الموجة المستعرضة (Onde transversale): يكون فيها اتجاه التشويه متعامدا مع اتجاه الانتشار (مثال: حبل، سطح الماء).\n• الموجة الطولية (Onde longitudinale): يكون فيها اتجاه التشويه موازيا لاتجاه الانتشار (مثال: الصوت، النابض).\n• التأخر الزمني (Retard temporel τ): هو المدة الزمنية التي تستغرقها الموجة لقطع المسافة بين نقطتين: τ = d / v.\nاستطالة النقطة M تكتب بدلالة استطالة المنبع S كالتالي: (y_M(t) = y_S(t - τ).",
                    diagramType = "WAVE_PROPAGATION",
                    formulas = listOf(
                        "v = d / \\Delta t" to "Vitesse de propagation (Célérité d'onde en m/s)",
                        "\\tau = d / v" to "Retard temporel entre deux points éloignés de d",
                        "y_M(t) = y_S(t - \\tau)" to "Relation de l'élongation par rapport à la source S"
                    )
                ),
                Lesson(
                    id = "pc_radioactive",
                    title = "Décroissance Radioactive & Nucléaire",
                    summary = "Étude de la désintégration spontanée des noyaux instables, équations de réactions nucléaires (α, β-, β+, γ) et loi de décroissance.",
                    fullContent = "1. Composition du noyau:\nNoyau représenté par _Z^A X où A est le nombre de nucléons et Z est le nombre de protons (numéro atomique).\n- Énergie de liaison: E_l = Δm . c² où Δm = [Z.m_p + (A-Z).m_n] - m_noyau (Défaut de masse).\n\n2. Types de radioactivité (Instabilité):\n- Radioactivité α: Noyaux lourds émettent un noyau d'Hélium _2^4 He. Équation: _Z^A X → _{Z-2}^{A-4} Y + _2^4 He.\n- Radioactivité β-: Noyaux excédentaires en neutrons transforment un neutron en proton + électron _-1^0 e. Équation: _Z^A X → _{Z+1}^A Y + _-1^0 e + ν_e.\n- Radioactivité β+: Noyaux excédentaires en protons transforment un proton en neutron + positon _1^0 e. Équation: _Z^A X → _{Z-1}^A Y + _1^0 e + ν_e.\n\n3. Loi de décroissance radioactive:\nN(t) = N0 . exp(-λ.t) où N(t) est le nombre de noyaux radioactifs à l'instant t, N0 à t=0, et λ la constante radioactive (s^-1).\n- Demi-vie t_1/2: temps au bout duquel la moitié des noyaux initiaux se désintègrent.\nt_1/2 = ln(2) / λ.\n- Activité A(t): nombre de désintégrations par seconde. A(t) = -dN/dt = λ.N(t) = A0 . exp(-λ.t) (unité: Becquerel (Bq)).",
                    arabicExplanations = "التناقص الإشعاعي:\n• النواة الممثلة بـ _Z^A X تتكون من Z بروتون و N = A - Z نيوترون.\n• طاقة الربط (Énergie de liaison): هي الطاقة اللازمة لتفتيت النواة إلى نوياتها وهي معزولة وساكنة: E_l = Δm . c².\n• قانون التناقص الإشعاعي: N(t) = N0 . e^(-λ.t).\n• عمر النصف (t_1/2): هو المدة الزمنية اللازمة لتفتت نصف عدد النوى البدئية: t_1/2 = ln(2) / λ.\n• النشاط الإشعاعي A(t) يقاس بـ البيكريل (Bq) ويمثل عدد التفتتات في الثانية.",
                    diagramType = "RADIOACTIVE_DECAY",
                    formulas = listOf(
                        "N(t) = N_0 e^{-\\lambda t}" to "Loi de décroissance radioactive",
                        "t_{1/2} = \\frac{\\ln(2)}{\\lambda}" to "Temps de demi-vie d'un nucléide",
                        "E_l = \\Delta m \\cdot c^2" to "Énergie de liaison du noyau (Défaut de masse Δm)"
                    )
                ),
                Lesson(
                    id = "pc_acid_base",
                    title = "Transformations Acide-Base",
                    summary = "Modèle de Brönsted, pH d'une solution, taux d'avancement final, constante d'équilibre Ka et diagramme de prédominance.",
                    fullContent = "1. Définition selon Brönsted:\n- Un acide est une espèce capable de céder un proton H+.\n- Une base est une espèce capable de capter un proton H+.\nCouple Acide/Base: AH / A- avec demi-équation: AH ⇌ A- + H+.\n\n2. pH et Taux d'avancement τ:\npH = -log[H3O+] <=> [H3O+] = 10^-pH.\n- Taux d'avancement final: τ = x_f / x_max.\n- Si τ = 1: la réaction est totale (acide fort).\n- Si τ < 1: la réaction est limitée, aboutit à un état d'équilibre chimique (acide faible).\n\n3. Constante d'acidité Ka:\nPour la réaction d'un acide faible avec l'eau: AH + H2O ⇌ A- + H3O+\nKa = ([A-]_eq * [H3O+]_eq) / [AH]_eq.\npKa = -log(Ka).\n- Relation d'Henderson: pH = pKa + log([A-]_eq / [AH]_eq).\n\n4. Diagramme de prédominance:\n- Si pH < pKa: l'espèce acide AH prédomine ([AH] > [A-]).\n- Si pH > pKa: l'espèce basique A- prédomine ([A-] > [AH]).\n- Si pH = pKa: [AH] = [A-].",
                    arabicExplanations = "التفاعلات حمض - قاعدة:\n• الحمض حسب برونشتد: هو كل نوع كيميائي قادرا على فقدان بروتون H+.\n• القاعدة حسب برونشتد: هو كل نوع كيميائي قادرا على اكتساب بروتون H+.\n• نسبة التقدم النهائي (τ): τ = x_f / x_max. إذا كانت τ = 1 فالتفاعل كلي، وإذا كانت τ < 1 فالتفاعل غير كلي (محدود) ويصل لحالة توازن.\n• ثابتة الحمضية Ka: تميز كل ثنائية حمض/قاعدة. العلاقة الأساسية: pH = pKa + log([A-] / [AH]).\n• مخطط الهيمنة (Prédominance): عند pH < pKa يهيمن الحمض، وعند pH > pKa تهيمن القاعدة.",
                    diagramType = "ACID_BASE_PREDOMINANCE",
                    formulas = listOf(
                        "pH = -\\log[H_3O^+]" to "Définition du pH d'une solution aqueuse",
                        "\\tau = x_f / x_{max}" to "Taux d'avancement final (réaction totale si τ = 1)",
                        "pH = pK_a + \\log\\frac{[A^-]_{eq}}{[AH]_{eq}}" to "Relation fondamentale d'Henderson"
                    )
                )
            ),
            flashcards = listOf(
                FlashCard(
                    id = "pc_fc1",
                    question = "Quelle est la différence entre une onde transversale et une onde longitudinale ?",
                    answer = "Pour une onde transversale, le déplacement du milieu est perpendiculaire à la direction de propagation (ex: onde sur une corde). Pour une onde longitudinale, le déplacement est parallèle à la direction de propagation (ex: son).",
                    hintArabic = "المستعرضة يكون التشويه عموديا على الانتشار والطورية يكون موازيا.",
                    category = "Ondes"
                ),
                FlashCard(
                    id = "pc_fc2",
                    question = "Qu'est-ce que le retard temporel τ ?",
                    answer = "Le retard temporel τ est la durée nécessaire pour qu'une onde se propage d'un point M1 à un point M2: τ = d / v. L'élongation de M2 est décalée: y_M2(t) = y_M1(t - τ).",
                    hintArabic = "المدة الزمنية لانتشار الموجة بين نقطتين.",
                    category = "Ondes"
                ),
                FlashCard(
                    id = "pc_fc3",
                    question = "Établir la formule de la demi-vie t_1/2 à partir de la loi de décroissance.",
                    answer = "Par définition N(t_1/2) = N0 / 2. En injectant dans la loi de décroissance: N0/2 = N0.exp(-λ.t_1/2) => 1/2 = exp(-λ.t_1/2) => -ln(2) = -λ.t_1/2 => t_1/2 = ln(2)/λ.",
                    hintArabic = "البرهان انطلاقا من قانون التناقص الإشعاعي.",
                    category = "Nucléaire"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "pc_q1",
                    question = "Une onde sonore est-elle une onde transversale ou longitudinale ?",
                    options = listOf("Transversale", "Longitudinale", "Les deux à la fois", "Ni l'un ni l'autre"),
                    correctAnswerIndex = 1,
                    explanationFr = "Le son est une onde longitudinale car les zones de compression et de dilatation de l'air se déplacent parallèlement à la direction de la propagation de l'onde.",
                    explanationAr = "الصوت موجة طولية لأن اتجاه انضغاط وتمدد جزيئيات الهواء مواز لاتجاه انتشار الصوت."
                ),
                QuizQuestion(
                    id = "pc_q2",
                    question = "Au bout de combien de temps l'activité d'un échantillon radioactif est-elle divisée par 4 ?",
                    options = "0.5 * t_1/2|1 * t_1/2|2 * t_1/2|4 * t_1/2".split("|"),
                    correctAnswerIndex = 2,
                    explanationFr = "Après une demi-vie t_1/2, l'activité est divisée par 2. Après deux demi-vies (2*t_1/2), elle est divisée par 2 puis encore par 2, soit divisée par 4 au total.",
                    explanationAr = "بعد مرور t_1/2 ينقص النشاط إلى النصف. وبعد مرور دورين 2*t_1/2 ينقص إلى الربع (أي قسمة 4)."
                ),
                QuizQuestion(
                    id = "pc_q3",
                    question = "Si le pH d'une solution acide AH/A- est supérieur à son pKa, quelle forme chimique majoritaire domine en solution ?",
                    options = listOf("La forme acide AH", "La forme basique A-", "Les deux sont en concentrations égales", "Aucune des deux"),
                    correctAnswerIndex = 1,
                    explanationFr = "Lorsque pH > pKa, d'après la relation pH = pKa + log([A-]/[AH]), le rapport [A-]/[AH] est supérieur à 1. C'est donc la forme basique conjuguée A- qui prédomine.",
                    explanationAr = "عندما يكون الرقم الهيدروجيني pH أكبر من pKa، فإن الصيغة القاعدة A- هي التي تهيمن في المحلول الكيميائي."
                )
            ),
            examExercises = listOf(
                NationalExamExercise(
                    id = "pc_ex1",
                    examYear = "National 2023 Ordinaire (PC)",
                    questionText = "Une onde mécanique se propage à la surface de l'eau à partir d'une source S. On prend une photo de la surface à l'instant t1 = 0,04 s. La distance entre la source S et le front de l'onde est d = 20 cm.\n1. Déterminer la célérité v de l'onde.\n2. Calculer le retard temporel τ d'un point M situé à d_M = 15 cm de la source S.",
                    solutionSteps = listOf(
                        "Étape 1: Identifier la relation fondamentale v = d / Δt.",
                        "Étape 2: Convertir les unités de distance en mètres: d = 20 cm = 0,20 met Δt = t1 - 0 = 0,04 s.",
                        "Étape 3: Faire l'application numérique: v = 0,20 / 0,04 = 5 m/s. La célérité de l'onde est 5 m/s.",
                        "Étape 4: Calculer le retard temporel: τ = d_M / v = 0,15 m / (5 m/s) = 0,03 s ou 30 ms."
                    ),
                    arabicTips = "نصيحة الامتحان الوطني:\n• انتبه جيدا للوحدات! يجب تحويل المسافة دائما من السنتيمتر (cm) إلى المتر (m) بضربها في 10^-2.\n• احرص على كتابة الصيغة الحرفية أولا قبل التعويض العددي للحصول على النقاط كاملة."
                ),
                NationalExamExercise(
                    id = "pc_ex2",
                    examYear = "National 2022 Rattrapage (PC)",
                    questionText = "Le Césium 137 (_55^137 Cs) est radioactif β- avec une demi-vie t_1/2 = 30 ans. Lors d'un incident, un échantillon présente une activité A1 = 800 Bq.\n1. Trouver sa constante radioactive λ en an^-1 et en s^-1.\n2. Calculer la durée Δt pour que l'activité devienne A2 = 200 Bq.",
                    solutionSteps = listOf(
                        "Étape 1: Utiliser la relation λ = ln(2) / t_1/2. λ = 0,693 / 30 ans = 0,0231 an^-1.",
                        "Étape 2: Convertir λ en s^-1 en prenant en compte qu'un an correspond à 365 * 24 * 3600 secondes ≈ 3,15 * 10^7 s. λ ≈ 7,33 * 10^-10 s^-1.",
                        "Étape 3: Utiliser la loi d'évolution de l'activité: A(t) = A0.exp(-λ.t). Ici: A2 = A1.exp(-λ.Δt) => A2/A1 = exp(-λ.Δt) => ln(A2/A1) = -λ.Δt => Δt = ln(A1/A2) / λ.",
                        "Étape 4: On remarque que A1/A2 = 800 / 200 = 4. Donc Δt = ln(4) / λ = 2 * ln(2) / λ. Comme t_1/2 = ln(2)/λ, on en déduit immédiatement: Δt = 2 * t_1/2 = 60 ans."
                    ),
                    arabicTips = "نصيحة الامتحان الوطني:\n• استخدم الذكاء الرياضي! عندما تلاحظ أن النشاط تقلص بمقدار 4 مرات (نشاط بدئي/4) فهذا يعني مباشرة مرور زمنين لنصف العمر (2 * t_1/2) دون الحاجة لحسابات معقدة باللوغاريتم."
                )
            )
        ),
        Subject(
            id = "math",
            nameFr = "Mathématiques",
            nameAr = "الرياضيات",
            description = "Limites et Continuité, Dérivabilité, Nombres complexes, Suites numériques, Intégrales.",
            lessons = listOf(
                Lesson(
                    id = "math_limits",
                    title = "Limites et Continuité",
                    summary = "Étude des limites, continuité d'une fonction en un point, sur un intervalle, théorème des valeurs intermédiaires (TVI) et fonctions réciproques.",
                    fullContent = "1. Continuité en un point x0:\nUne fonction f est continue en x0 si et seulement si:\nlim_{x → x0} f(x) = f(x0).\n\n2. Théorème des Valeurs Intermédiaires (TVI):\nSi f est continue sur un intervalle [a, b] alors pour tout réel y compris entre f(a) et f(b), il existe au moins un réel c ∈ [a, b] tel que f(c) = y.\n- Cas d'existence unique: Si de plus f est strictement monotone (croissante ou décroissante) sur [a, b], l'équation f(x) = k (avec k entre f(a) et f(b)) admet une unique solution α dans [a, b].\n\n3. Fonction réciproque f^-1:\nSi f est continue et strictement monotone sur un intervalle I, elle réalise une bijection de I vers J = f(I).\n- f^-1 est continue sur J et a les mêmes variations que f.\n- Courbe de f^-1: C_f^-1 est la symétrique de C_f par rapport à la première bissectrice (la droite d'équation y = x).",
                    arabicExplanations = "النهايات والاتصال:\n• الاتصال في نقطة: تكون f متصلة في x0 إذا وفقط إذا كانت نهاية f عند x0 تساوي f(x0).\n• مبرهنة القيم الوسيطية (TVI): إذا كانت f متصلة ورتيبة قطعا (تزايدية أو تناقصية قطعا) على مجال [a, b]، فإن المعادلة f(x) = k تقبل حلا وحيدا α في هذا المجال.\n• الدالة العكسية f^-1: إذا كانت f متصلة ورتيبة قطعا على مجال I فإنها تقبل دالة عكسية معرفة على J = f(I). منحنى الدالة العكسية يماثل منحنى f بالنسبة للمنصف الأول (y = x).",
                    diagramType = "FUNCTION_CONTINUITY",
                    formulas = listOf(
                        "\\lim_{x \\to x_0} f(x) = f(x_0)" to "Condition de continuité de f en x0",
                        "f(a) \\cdot f(b) < 0" to "Condition pour f(x)=0 d'avoir un changement de signe sur [a,b]",
                        "(f^{-1})'(y_0) = \\frac{1}{f'(x_0)}" to "Dérivée de la fonction réciproque en y0 = f(x0)"
                    )
                ),
                Lesson(
                    id = "math_complex",
                    title = "Nombres Complexes",
                    summary = "Forme algébrique, trigonométrique et exponentielle. Argument, module, équations du second degré et interprétation géométrique.",
                    fullContent = "1. Définition et forme algébrique:\nTout nombre complexe s'écrit de façon unique: z = x + i.y où x, y ∈ ℝ et i² = -1. x est la partie réelle Re(z), y la partie imaginaire Im(z).\n- Conjugué de z: z̄ = x - i.y.\n- Module: |z| = √(x² + y²).\n\n2. Formes trigonométrique et exponentielle:\nz = r(cos θ + i sin θ) = r.e^(iθ) où r = |z| et θ ≡ arg(z) [2π].\n- cos θ = x / r et sin θ = y / r.\n\n3. Formules fondamentales:\n- Formule de Moivre: [cos(θ) + i.sin(θ)]^n = cos(nθ) + i.sin(nθ) <=> (e^(iθ))^n = e^(inθ).\n- Formules d'Euler: cos(θ) = (e^(iθ) + e^(-iθ)) / 2 et sin(θ) = (e^(iθ) - e^(-iθ)) / 2i.\n\n4. Résolution d'équations dans ℂ:\na.z² + b.z + c = 0 (a, b, c ∈ ℝ, a≠0).\nCalcul du discriminant Δ = b² - 4ac.\n- Si Δ > 0: deux solutions réelles z1, z2 = (-b ± √Δ) / 2a.\n- Si Δ = 0: une solution réelle double z = -b/2a.\n- Si Δ < 0: deux solutions complexes conjuguées: z1 = (-b + i√|Δ|) / 2a, z2 = z1̄.",
                    arabicExplanations = "الأعداد العقدية:\n• الشكل الجبري: z = x + i.y حيث x الجزء الحقيقي و y الجزء التخيلي.\n• معيار العدد العقدي: ( |z| = √(x² + y²).\n• الشكل المثلثي والأسّي: r . e^(iθ) =  r(cos θ + i sin θ) حيث r هو المعيار و θ هي العمدة arg(z).\n• صيغة مويفر وصيغ أولير تمكن من إخطاط الحدوديات المثلثية.\n• حل المعادلات من الدرجة الثانية في ℂ: عندما يكون مميز المعادلة Δ أصغر من صفر، تقبل المعادلة حلين عقدين مترافقين z1 و z2.",
                    diagramType = "COMPLEX_PLANE",
                    formulas = listOf(
                        "z = x + i y" to "Forme algébrique d'un nombre complexe",
                        "z = r e^{i \\theta}" to "Forme exponentielle avec r = |z| et θ arg(z)",
                        "e^{i \\theta} = \\cos\\theta + i\\sin\\theta" to "Formule trigonométrique d'Euler"
                    )
                )
            ),
            flashcards = listOf(
                FlashCard(
                    id = "math_fc1",
                    question = "Quel est le module de z = 3 + 4i ?",
                    answer = "|z| = √(3² + 4²) = √(9 + 16) = √25 = 5.",
                    hintArabic = "المعيار يساوي جذر (مربع الحقيقي + مربع التخيلي).",
                    category = "Complexes"
                ),
                FlashCard(
                    id = "math_fc2",
                    question = "Quelles sont les conditions de TVI pour avoir une unique solution f(x) = 0 ?",
                    answer = "f doit être continue sur l'intervalle [a,b], strictement monotone (strictement croissante ou décroissante), et f(a) * f(b) < 0 (le produit doit être négatif pour qu'il y ait changement de signe).",
                    hintArabic = "الاتصال، الرتابة قطعا، وتغير الإشارة بين الطرفين.",
                    category = "Continuité"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "math_q1",
                    question = "Quelle est l'écriture de e^(iπ) ?",
                    options = listOf("1", "-1", "i", "-i"),
                    correctAnswerIndex = 1,
                    explanationFr = "D'après la formule d'Euler: e^(iπ) = cos(π) + i.sin(π) = -1 + i(0) = -1.",
                    explanationAr = "أسية iπ تساوي ناقص واحد حسب تعريف الشكل المثلثي (جيب تمام π يساوي -1 وجيب π يساوي 0)."
                ),
                QuizQuestion(
                    id = "math_q2",
                    question = "Si f est continue sur [0, 2] avec f(0) = -3 et f(2) = 5. Qu'affirme le TVI ?",
                    options = listOf("Il n'y a pas de solution", "Il existe au moins un c ∈ ]0, 2[ tel que f(c) = 0", "L'unique racine est c = 1", "f est toujours croissante"),
                    correctAnswerIndex = 1,
                    explanationFr = "Puisque f est continue sur [0,2] et 0 est compris entre f(0)=-3 et f(2)=5, le TVI garantit l'existence d'au moins une solution à f(x)=0.",
                    explanationAr = "بما أن الدالة متصلة والصفر محصور بين -3 و 5، فإن الدالة تمر حتما بالصفر على الأقل مرة واحدة في المجال."
                )
            ),
            examExercises = listOf(
                NationalExamExercise(
                    id = "math_ex1",
                    examYear = "National 2023 Ordinaire",
                    questionText = "Résoudre dans l'ensemble des nombres complexes ℂ l'équation:\nz² - 6z + 13 = 0",
                    solutionSteps = listOf(
                        "Étape 1: Écrire les coefficients. a = 1, b = -6, c = 13.",
                        "Étape 2: Calculer le discriminant Δ = b² - 4ac = (-6)² - 4*(1)*(13) = 36 - 52 = -16.",
                        "Étape 3: Puisque Δ < 0, l'équation admet deux solutions complexes conjuguées.",
                        "Étape 4: Calculer les solutions: z1 = (-b + i√|Δ|) / 2a = (6 + i√16) / 2 = (6 + 4i) / 2 = 3 + 2i.",
                        "Étape 5: Déduire la deuxième solution: z2 = z1̄ = 3 - 2i. L'ensemble de solutions est S = {3 - 2i ; 3 + 2i}."
                    ),
                    arabicTips = "نصيحة الامتحان الوطني:\n• تأكد دائما من كتابة الخطوات بالتفصيل. مصححو الوطني يوزعون النقاط على المميز Δ أولا، ثم الصياغة الحرفية للحلين، وأخيرا النتيجة الختامية وكتابة مجموعة الحلول S."
                )
            )
        ),
        Subject(
            id = "svt",
            nameFr = "Sciences de la Vie et de la Terre (SVT)",
            nameAr = "علوم الحياة و الأرض",
            description = "Consommation de la matière organique, Cycle de Krebs, Expression de l'information génétique, Immunologie.",
            lessons = listOf(
                Lesson(
                    id = "svt_atp",
                    title = "Consommation de la Matière Organique & Libération de l'Énergie",
                    summary = "Assimilation du glucose par les cellules, comparaison de la respiration aérobie et de la fermentation anaérobie.",
                    fullContent = "1. Glycolyse (Voie commune dans le hyaloplasme):\nLe glucose (C6H12O6) est dégradé en 2 molécules d'acide pyruvique (CH3COCOOH) avec production de 2 ATP et réduction de 2 NAD+ en 2 NADH,H+.\nC6H12O6 + 2 ADP + 2 Pi + 2 NAD+ → 2 Acide Pyruvique + 2 ATP + 2 NADH,H+.\n\n2. Destinée de l'acide pyruvique:\n- En présence d'O2 (Voie aérobie) d'abord dans la matrice mitochondriale:\nL'acide pyruvique subit une décarboxylation oxydative pour former l'Acétyl-Coenzyme A (Acétyl-CoA). Elle alimente le Cycle de Krebs (production de CO2, ATP, NADH,H+, FADH2).\n\n3. Chaîne respiratoire (Dans la membrane interne de la mitochondrie):\nOxydation des coenzymes réduits (NADH,H+ et FADH2) qui cèdent leurs électrons à la chaîne respiratoire jusqu'à l'O2 (accepteur final, réduit en H2O). Le flux d'électrons induit un gradient de protons H+ vers l'espace intermembranaire. Les protons retournent à la matrice via les sphères pédonculées, activant l'ATP synthase (phosphorylation de l'ADP en ATP).\n- Bilan énergétique total d'une molécule de glucose: 38 ATP (ou 36 ATP).\n\n4. Voie anaérobie (Fermentation dans le hyaloplasme):\n- Fermentation lactique: Réduction de l'acide pyruvique en acide lactique (ex: cellules musculaires). Bilan: 2 ATP par glucose.\n- Fermentation alcoolique: Décarbonylation et réduction de l'acide pyruvique en Éthanol + CO2. Bilan: 2 ATP.",
                    arabicExplanations = "ميكانيزمات إنتاج الطاقة:\n• انحلال الكلوكوز (Glycolyse): يتم في الجبلة الشفافة، وهو مرحلة مشتركة بين التنفس والتخمر ينتج عنها جزيئتان من حمض البيروفيك إضافة لـ 2 ATP و 2 NADH,H+.\n• التنفس الخلوي (Respiration aérobie): يتم داخل الميتوكوندري بوجود O2 عبر دورة كريبس (Cycle de Krebs) والسلسلة التنفسية (Chaîne respiratoire). المردود النهائي مرتفع: 38 ATP.\n• التخمر الخلوي (Fermentation): يتم بغياب الأكسجين في الجبلة الشفافة بمردود طاقي ضعيف جدا: 2 ATP (التخمر اللبني ينتج حمض اللبني، والتخمر الكحولي ينتج الإيثانول وثنائي أكسيد الكربون).",
                    diagramType = "MITOCHONDRIA_ENERGY",
                    formulas = emptyList()
                )
            ),
            flashcards = listOf(
                FlashCard(
                    id = "svt_fc1",
                    question = "Quel est le bilan énergétique net de la glycolyse ?",
                    answer = "Le bilan net de la glycolyse est de 2 molécules d'ATP et 2 molécules de coenzyme réduit (NADH, H+) par molécule de glucose dégradée dans le hyaloplasme.",
                    hintArabic = "حصيلة انحلال الكلوكوز في الجبلة الشفافة.",
                    category = "Énergie"
                ),
                FlashCard(
                    id = "svt_fc2",
                    question = "Quel est le rôle de l'oxygène O2 dans la chaîne respiratoire ?",
                    answer = "L'O2 est l'accepteur final d'électrons au bout de la chaîne respiratoire de la membrane interne mitochondriale. Il est réduit en eau (H2O) par capture des électrons et des protons: O2 + 4H+ + 4e- → 2 H2O.",
                    hintArabic = "المتقبل النهائي للإلكترونات في الميتوكوندري.",
                    category = "Mitochondrie"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "svt_q1",
                    question = "Où se déroule précisément le Cycle de Krebs ?",
                    options = listOf("Dans le hyaloplasme (cytoplasme)", "Dans l'espace intermembranaire", "Dans la matrice mitochondriale", "Dans la membrane externe"),
                    correctAnswerIndex = 2,
                    explanationFr = "Le cycle de Krebs se déroule dans la matrice de la mitochondrie grâce aux enzymes spécifiques qui y catalysent la décarboxylation oxydative du groupement acétyle.",
                    explanationAr = "تحدث تفاعلات دورة كريبس داخل ماتريس (Matrice) الميتوكوندري بفضل الأنزيمات النوعية المتواجدة هناك."
                ),
                QuizQuestion(
                    id = "svt_q2",
                    question = "Combien d'ATP sont générés par la fermentation lactique d'une molécule de glucose ?",
                    options = listOf("2 ATP", "4 ATP", "36 ATP", "38 ATP"),
                    correctAnswerIndex = 0,
                    explanationFr = "La fermentation lactique produit un bilan net très faible de seulement 2 ATP, provenant uniquement de la réaction initiale de la glycolyse.",
                    explanationAr = "ينتج عن التخمر اللبني حصيلة طاقية ضعيفة جدا تبلغ جزيئتين فقط من الـ ATP ناتجة مباشرة عن انحلال الكلوكوز."
                )
            ),
            examExercises = listOf(
                NationalExamExercise(
                    id = "svt_ex1",
                    examYear = "National 2022 (SVT / PC)",
                    questionText = "La fatigue musculaire est souvent liée à un manque d'oxygénation des fibres. Des études montrent qu'un entraînement prolongé augmente le nombre de mitochondries.\n1. Expliquer pourquoi un manque d'O2 conduit à une baisse de performance musculaire.\n2. Quel est l'intérêt métabolique d'avoir plus de mitochondries pour un athlète ?",
                    solutionSteps = listOf(
                        "Étape 1: Établir la relation entre l'O2 et la production d'ATP. L'absence d'oxygène bloque la chaîne respiratoire mitochondriale.",
                        "Étape 2: Expliquer la compensation cellulaire. Pour survivre, la fibre passe en fermentation lactique qui produit seulement 2 ATP au lieu de 38 ATP par glucose, tout en accumulant de l'acide lactique de pH acide qui fatigue la fibre.",
                        "Étape 3: Relier le nombre de mitochondries aux performances d'endurance. Plus de mitochondries augmentent la capacité de la cellule à réaliser la respiration aérobie à haut rendement thermique/énergétique (38 ATP), retardant le métabolisme de fermentation.",
                        "Étape 4: Conclure que l'entraînement permet d'optimiser le flux d'ATP aérobie réutilisé pour les contractions musculaires soutenues."
                    ),
                    arabicTips = "مفاتيح كتابة الأجوبة في مادة SVT:\n• مادة علوم الحياة والأرض تتطلب دقة المصطلحات (Mots-clés). ركز على ربط الميتوكوندري بالهوائية والتنفس ومردود طاقة ATP مرتفع.\n• اربط دائما العياء بـ انخفاض الأكسجين وتراكم حمض اللبن (Acide lactique) السام للعضلات."
                )
            )
        ),
        Subject(
            id = "philo",
            nameFr = "Philosophie",
            nameAr = "الفلسفة",
            description = "L'existence humaine (Le Sujet, Autrui, l'Histoire), Épistémologie (La Vérité), La Politique.",
            lessons = listOf(
                Lesson(
                    id = "philo_autrui",
                    title = "Le concept d'Autrui (Espace Humain)",
                    summary = "Analyse de la relation à autrui: Est-il un obstacle, un médiateur nécessaire, ou une source de richesse constructive?",
                    fullContent = "1. Existence d'Autrui:\n- René Descartes: L'existence d'autrui est douteuse au niveau du Cogito initial (Seul mon esprit existe au départ, solipsisme). Autrui n'est perçu que par induction instrumentale extérieure (vêtements/chapeaux).\n- Jean-Paul Sartre: Autrui est un médiateur indispensable entre moi et moi-même. Par le regard d'autrui, je prends conscience de mes attributs (ex: la honte). 'Autrui est le sujet qui n'est pas moi et pour qui je suis objet'.\n\n2. La Connaissance d'Autrui:\n- Est-elle possible ou impossible? Pour Max Scheler, nous pouvons appréhender autrui globalement à travers ses expressions physiques et psychiques unifiées. Pour Jean-Paul Sartre, la relation d'objet fige autrui dans l'inertie: la connaissance d'autrui reste partielle et conflictuelle.\n\n3. La relation avec Autrui:\n- Relation de conflit (Sartre, Hegel au travers du maître et de l'esclave: lutte à mort pour la reconnaissance) ou relation d'amitié et de respect mutuel (Aristote: l'amitié vertu supérieure indispensable à la société, Emmanuel Kant: la relation morale doit traiter l'autre comme une fin en soi et non comme un moyen).",
                    arabicExplanations = "مفهوم الغير (الوضع البشري):\n• وجود الغير: يرى ديكارت (Descartes) أن وجود الغير افتراضي وغير مؤكد بالوعي المنفرد. بينما يرى سارتر (Sartre) أن الغير وسيط لا غنى عنه لوعي الذات بنفسها (أنا أخجل لأن الغير يراني ويحكم علي).\n• معرفة الغير: هل هي ممكنة أم مستحيلة؟ مع مع يرى الفلاسفة الظاهريون كـ شيلر أنها ممكنة بالتعاطف والاندماج، في حين يراها سارتر جالبة للصراع كشيء يخترق ذاتيتنا.\n• العلاقة مع الغير: صراع واعتراف بالجميل (هيغل وسارتر) أم صداقة واحترام أخلاقي ذاتي (أرسطو وكانط).",
                    diagramType = "PHILOSOPHY_RELATIONS",
                    formulas = emptyList()
                )
            ),
            flashcards = listOf(
                FlashCard(
                    id = "philo_fc1",
                    question = "Quelle est la vision sartrienne d'autrui ?",
                    answer = "Pour Sartre, autrui est 'le médiateur indispensable entre moi et moi-même' car je ne peux saisir ma propre identité intimement que lorsque je suis exposé au regard d'autrui. Néanmoins, ce regard tend à s'objectiver, créant un conflit.",
                    hintArabic = "الغير هو الوسيط الضروري للوعي بالذات.",
                    category = "Autrui"
                ),
                FlashCard(
                    id = "philo_fc2",
                    question = "Quel est l'impératif catégorique d'Emmanuel Kant vis-à-vis d'autrui ?",
                    answer = "Kant stipule qu'il faut toujours agir de telle sorte que l'on traite l'humanité, aussi bien dans sa personne que dans celle d'autrui, toujours comme une fin et jamais simplement comme un moyen.",
                    hintArabic = "معاملة الغير كغاية أخلاقية وليس كوسيلة.",
                    category = "Relation"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "philo_q1",
                    question = "Quel philosophe prétend que le Cogito initial isole l'esprit des autres (solipsisme) ?",
                    options = listOf("René Descartes", "Jean-Paul Sartre", "G.W.F. Hegel", "Aristote"),
                    correctAnswerIndex = 0,
                    explanationFr = "René Descartes, dans ses Méditations Métaphysiques, suspecte le monde extérieur de doute et fonde la vérité sur le Cogito isolé: 'Je pense donc je suis' avant d'accueillir autrui.",
                    explanationAr = "رينيه ديكارت بمقولته الكوجيتو 'أنا أفكر إذن أنا موجود' أسس لفردانية الوعي المعزول كأول يقين فلسفي."
                ),
                QuizQuestion(
                    id = "philo_q2",
                    question = "D'après Hegel, comment s'effectue la lutte pour la reconnaissance entre deux consciences ?",
                    options = listOf("Par la discussion pacifique", "Par le compromis démocratique", "Par une lutte à mort entre Maître et Esclave", "Par l'ignorance réciproque"),
                    correctAnswerIndex = 2,
                    explanationFr = "Hegel stipule dans sa dialectique du Maître et de l'Esclave que la reconnaissance exige une confrontation à mort où l'un préfère la vie à la liberté et se soumet.",
                    explanationAr = "يرى هيغل أن الاعتراف المتبادل ينتج عن صراع حياة أو موت يسمى جدلية السيد والعبد."
                )
            ),
            examExercises = listOf(
                NationalExamExercise(
                    id = "philo_ex1",
                    examYear = "National Bac Philo Ma",
                    questionText = " Dissertation philosophique: 'La relation avec autrui repose-t-elle nécessairement sur le conflit ?'\nComment construire l'introduction et les transitions pour cet examen ?",
                    solutionSteps = listOf(
                        "Étape 1: Formuler la problématique. Définir 'Autrui' (celui qui n'est pas moi) et 'Conflit' (affrontement pour la reconnaissance ou l'espace vital).",
                        "Étape 2: Thèse 1 (Oui) - Le conflit constitutif. Sartre (le regard d'autrui chosifie mon être, vole ma liberté), Hegel (conflit d'autorité pour la reconnaissance dialectique).",
                        "Étape 3: Thèse 2 (Non) - Les fondations morales et sociales. Kant (le respect rationnel mutuel), Aristote (amitié civique vertueuse cimentant l'État).",
                        "Étape 4: Synthèse - La relation est dynamique. Le conflit sain fait émerger l'identité, mais l'éthique de la tolérance et du dialogue le sublime en amitié sincère."
                    ),
                    arabicTips = "نصيحة لتفوقك في موضوع الفلسفة:\n• ابدأ دائما بـ تمهيد للمفهوم (الوضع البشري - الغير) في المقدمة مع صياغة مفارقة واضحة (صداقة أم صراع؟).\n• احرص على توظيف المفاهيم الفلسفية الأساسية وابتعد عن التكلّم بضمير المتكلم الشخصي بل بأسلوب موضوعي رصين."
                )
            )
        ),
        Subject(
            id = "english",
            nameFr = "Anglais (English)",
            nameAr = "اللغة الإنجليزية",
            description = "Vocabulary, Grammar (Phrasal Verbs, Tenses), Expressing Opinion and Written Production rules.",
            lessons = listOf(
                Lesson(
                    id = "eng_verbs",
                    title = "Phrasal Verbs & Tenses (National Exam Focus)",
                    summary = "Perfecting phrasal verbs, past perfect, future perfect, and direct rules for high scores in the national exam.",
                    fullContent = "1. Crucial Phrasal Verbs for Bac Exam:\n- Bring about: cause to happen.\n- Give up: stop trying, surrender.\n- Make up: invent a story, compensate.\n- Look forward to (+ V-ing): anticipate with pleasure (e.g., 'I look forward to meeting you').\n- Back up: support someone or save copy of files.\n- Run out of: finish the supply (e.g., 'We ran out of gas').\n- Set up: establish a business or organization.\n\n2. Key Tenses for BAC:\n- Future Perfect: Action that will be completed before a specific point in the future.\nStructure: Subject + will + have + Past Participle.\nKeyword: By next month, By the end of this year (e.g., 'By 2027, I will have graduated').\n- Past Perfect: Action completed before another past action.\nStructure: Subject + had + Past Participle.\nKeywords: After, Before, By the time (e.g., 'By the time the police arrived, the robber had escaped').",
                    arabicExplanations = "قواعد اللغة الإنجليزية للوطني:\n• الأفعال المركبة (Phrasal Verbs): يجب حفظ معانيها جيدا لأنها ترد في الامتحان بشكل تزاوجي أو متعدد الاختيارات (مثل: Give up = يستسلم، Run out of = ينفذ منه شيء).\n• زمن المستقبل التام (Future Perfect): يستعمل للتعبير عن حدث سينتهي بحلول نقطة زمنية مستقبلية. تركيبته: Will have + Past Participle. المفتاح الشهير هو 'By the end of...'.\n• زمن الماضي التام (Past Perfect): حدث وقع قبل حدث ماضوي آخر. صيغته: Had + Past Participle.",
                    diagramType = "TIMELINE_TENSES",
                    formulas = emptyList()
                )
            ),
            flashcards = listOf(
                FlashCard(
                    id = "eng_fc1",
                    question = "What does 'to look forward to' mean, and what is the grammatical pattern after it?",
                    answer = "'To look forward to' means to anticipate something with positive excitement. It MUST be followed by a Gerund (Verb + ing) or a Noun. Example: 'I look forward to passing my national exam.'",
                    hintArabic = "تتطلع لشيء ما بشوق، ويتبعها دائما الفعل بصيغة ing.",
                    category = "Phrasal Verbs"
                ),
                FlashCard(
                    id = "eng_fc2",
                    question = "When do we use the Future Perfect tense?",
                    answer = "We use the Future Perfect for actions that will be completed by/before a specific time in the future. Example: 'By December, we will have finished all 2 Bac units.'",
                    hintArabic = "التعبير عن حدث سينتهي بشكل تام بحلول وقت معين في المستقبل.",
                    category = "Grammar"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "eng_q1",
                    question = "Complete: 'By the end of this month, our class _______ three mock national exams.'",
                    options = listOf("will take", "will have taken", "has taken", "takes"),
                    correctAnswerIndex = 1,
                    explanationFr = "La locution 'By the end of this month' exige le futur antérieur (Future Perfect) pour situer l'action achevée dans le futur: 'will have taken'.",
                    explanationAr = "وجود التعبير الإشاري 'By the end of...' يفرض حتما استعمال زمن المستقبل التام: will have taken."
                ),
                QuizQuestion(
                    id = "eng_q2",
                    question = "What is the meaning of 'Give up' in: 'Never give up on your dreams!'",
                    options = listOf("Surrender/Stop trying", "Continue working", "Succeed instantly", "Discuss with friends"),
                    correctAnswerIndex = 0,
                    explanationFr = "'Give up' signifie abandonner ou cesser d'essayer (to surrender). C'est un conseil d'endurance courant dans les examens.",
                    explanationAr = "الفعل المركب Give up يعني الاستسلام أو ترك المحاولة."
                )
            ),
            examExercises = listOf(
                NationalExamExercise(
                    id = "eng_ex1",
                    examYear = "National Exam 2022",
                    questionText = "Rewrite the following sentences starting with the words given:\n1. 'We finished our project last night.'\n-> By the time the teacher arrived today, we __________________.",
                    solutionSteps = listOf(
                        "Step 1: Notice the trigger phrase 'By the time' + simple past ('arrived').",
                        "Step 2: When an action happens before another action in the past, it requires the Past Perfect tense.",
                        "Step 3: Structure of Past Perfect is 'had' + past participle of 'finish' (finished).",
                        "Step 4: Formulate the correct answer: 'we had finished our project.'"
                    ),
                    arabicTips = "نصائح الامتحان الوطني لمادة الإنجليزية:\n• عند توظيف 'By the time + Simple Past' بالجزء الأول، فالفراغ الثاني يتطلب دائما توظيف الـ Past Perfect (Had + Past Participle).\n• لا تنس كتابة كلمة Had بشكل سليم واحرص على تصريف الأفعال غير القياسية (Irregular verbs) بدقة."
                )
            )
        )
    )
}
