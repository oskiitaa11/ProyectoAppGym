package com.example.proyectoappgym.db.db_routines

import com.example.proyectoappgym.entity.Exercise
import com.example.proyectoappgym.entity.ExerciseLevel
import com.example.proyectoappgym.entity.TypeExercise
import com.example.proyectoappgym.entity.TypeTensExercise

object AllExercises {
    val machinesRoutines = listOf(
        Exercise(
            name = "Chest Press Machine (Peck Deck or Chest Press Machine)",
            description = "To work the pectorals, primarily the pectoralis major.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Pectorals"),
            stepsForDoIt = """
        1. Siéntate en la máquina y ajusta el asiento para que las manijas estén a la altura del pecho.
        2. Coloca las manos en las manijas con un agarre firme.
        3. Empuja las manijas hacia adelante hasta extender casi completamente los brazos, manteniendo una ligera flexión en los codos.
        4. Regresa lentamente a la posición inicial controlando el movimiento.
        5. Repite el número deseado de repeticiones.
    """.trimIndent()
        ),
        Exercise(
            name = "Peck Deck Machine",
            description = "Excellent for working the inner part of the pectorals and focusing on muscle contraction.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Inner Pectorals"),
            stepsForDoIt = """
        1. Siéntate en la máquina con la espalda recta y ajusta la altura del asiento.
        2. Coloca los antebrazos sobre las almohadillas o las manos en las manijas, dependiendo del modelo.
        3. Junta los brazos hacia el centro del pecho, contrayendo los pectorales.
        4. Detente un segundo en la contracción máxima.
        5. Regresa lentamente a la posición inicial.
        6. Repite el ejercicio según tus repeticiones programadas.
    """.trimIndent()
        ),
        Exercise(
            name = "Incline Press Machine",
            description = "Works the upper part of the pectoral muscle.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Upper Pectorals"),
            stepsForDoIt = """
        1. Ajusta el asiento para que las manijas estén alineadas con la parte superior del pecho.
        2. Siéntate con la espalda apoyada en el respaldo.
        3. Agarra las manijas con un agarre firme.
        4. Empuja hacia arriba y ligeramente hacia adelante, extendiendo los brazos.
        5. Detén el movimiento sin bloquear los codos.
        6. Regresa lentamente a la posición inicial.
    """.trimIndent()
        ),
        Exercise(
            name = "Lat Pulldown Machine",
            description = "Ideal for working the lat muscles (latissimus dorsi).",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Lats"),
            stepsForDoIt = """
        1. Siéntate y ajusta la almohadilla sobre tus muslos.
        2. Sujeta la barra con un agarre amplio y firme.
        3. Tira de la barra hacia abajo hasta que toque la parte superior del pecho.
        4. Mantén el torso recto sin inclinarte hacia atrás.
        5. Regresa lentamente la barra a la posición inicial.
    """.trimIndent()
        ),
        Exercise(
            name = "Seated Row Machine",
            description = "Works the middle back and trapezius.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Middle Back", "Trapezius", "latissimus_dorsi", "teres_major", "teres_minor"),
            stepsForDoIt = """
        1. Siéntate con los pies apoyados en la plataforma.
        2. Sujeta las manijas con ambas manos.
        3. Tira de las manijas hacia tu abdomen mientras mantienes la espalda recta.
        4. Aprieta los omóplatos al final del movimiento.
        5. Regresa lentamente a la posición inicial sin dejar caer el peso.
    """.trimIndent()
        ),
        Exercise(
            name = "Pull Over Machine",
            description = "Works the lats and upper back area.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Lats"),
            stepsForDoIt = """
        1. Siéntate en la máquina y ajusta el respaldo y la altura de los brazos.
        2. Sujeta las manijas con los brazos extendidos hacia arriba.
        3. Baja los brazos en un arco hacia tu cintura, manteniendo los codos ligeramente doblados.
        4. Contrae los dorsales al final del movimiento.
        5. Regresa a la posición inicial lentamente.
    """.trimIndent()
        ),
        Exercise(
            name = "Leg Press Machine",
            description = "Primarily works the quadriceps, glutes, and to a lesser extent the hamstrings.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Quadriceps", "Glutes", "Hamstrings"),
            stepsForDoIt = """
        1. Siéntate en la máquina y coloca los pies en la plataforma al ancho de los hombros.
        2. Suelta los seguros si es necesario.
        3. Empuja la plataforma con los pies hasta extender casi completamente las piernas.
        4. Evita bloquear las rodillas en la extensión.
        5. Regresa lentamente a la posición inicial con control.
    """.trimIndent()
        ),
        Exercise(
            name = "Leg Extension Machine",
            description = "Ideal for isolating the quadriceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Quadriceps"),
            stepsForDoIt = """
        1. Sit on the leg extension machine.
        2. Adjust the pads so they rest just above your ankles.
        3. Hold the side handles for stability.
        4. Extend your legs forward until they are fully straight.
        5. Hold the position briefly at the top.
        6. Slowly return to the starting position.
    """.trimIndent()
        ),
        Exercise(
            name = "Leg Curl Machine",
            description = "Works the hamstrings.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Hamstrings"),
            stepsForDoIt = """
        1. Lie face down on the leg curl machine.
        2. Place your legs under the padded rollers.
        3. Hold the handles for stability if available.
        4. Curl your legs by bending your knees, bringing the pads toward your glutes.
        5. Squeeze your hamstrings at the top of the movement.
        6. Slowly return to the starting position.
    """.trimIndent()
        ),
        Exercise(
            name = "Abductor/Adductor Machine",
            description = "Works the inner and outer thigh muscles (adductors and abductors).",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Adductors", "Glutes"),
            stepsForDoIt = """
        1. Sit on the machine and adjust the pads to your desired position.
        2. Place your legs inside or outside the pads depending on the targeted muscles.
        3. Hold the side handles to maintain control.
        4. Push outward or inward with your legs against the machine’s resistance.
        5. Perform the movement slowly and controlled.
        6. Return to the starting position.
    """.trimIndent()
        ),
        Exercise(
            name = "Glute Press Machine",
            description = "Focuses on the glutes and to a lesser extent the thighs.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Glutes", "Thighs"),
            stepsForDoIt = """
        1. Adjust the machine to fit your height.
        2. Place one foot on the pressing platform.
        3. Hold the handles for stability.
        4. Push the platform back with your leg, engaging the glutes.
        5. Fully extend your leg without locking the knee.
        6. Return slowly to the starting position.
    """.trimIndent()
        ),
        Exercise(
            name = "Shoulder Press Machine",
            description = "Works the deltoid muscles and secondary muscles such as the triceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Deltoids", "Triceps"),
            stepsForDoIt = """
        1. Sit on the machine and adjust the seat so the handles are at shoulder height.
        2. Grip the handles firmly.
        3. Press the handles upward until your arms are almost fully extended.
        4. Avoid locking your elbows at the top.
        5. Slowly lower the handles back to the starting position.
    """.trimIndent()
        ),
        Exercise(
            name = "Reverse Pec Deck Machine",
            description = "Works the posterior deltoid and trapezius muscles.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Posterior Deltoids", "Trapezius"),
            stepsForDoIt = """
        1. Sit facing the pad of the machine.
        2. Adjust the seat so your arms are aligned with your shoulders.
        3. Hold the handles or place your forearms on the pads.
        4. Pull your arms back in a reverse fly motion.
        5. Squeeze the rear shoulder muscles at the peak.
        6. Return to the starting position in a controlled manner.
    """.trimIndent()
        ),
        Exercise(
            name = "Lateral Raise Machine",
            description = "Isolates the middle part of the deltoid.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Middle Deltoids"),
            stepsForDoIt = """
        1. Sit on the machine and adjust the seat to the appropriate height.
        2. Position your arms under the padded rollers.
        3. Raise your arms sideways until they are parallel to the floor.
        4. Keep a slight bend in your elbows throughout the movement.
        5. Slowly return to the starting position.
    """.trimIndent()
        ),
        Exercise(
            name = "Bicep Curl Machine",
            description = "Perfect for isolating the biceps and working the inner part of the arm.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Biceps"),
            stepsForDoIt = """
        1. Sit on the machine and adjust the seat so your elbows align with the pivot point.
        2. Grip the handles with your palms facing up.
        3. Curl the handles toward you by bending your elbows.
        4. Squeeze your biceps at the top.
        5. Slowly return to the starting position.
    """.trimIndent()
        ),
        Exercise(
            name = "Tricep Extension Machine",
            description = "Works the posterior part of the arm, specifically the triceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Triceps"),
            stepsForDoIt = """
        1. Sit and adjust the seat so your elbows are in line with the machine's axis.
        2. Grip the handles with your palms facing down.
        3. Extend your arms downward until they are fully straightened.
        4. Contract your triceps at the bottom of the movement.
        5. Slowly return to the starting position.
    """.trimIndent()
        ),
        Exercise(
            name = "Preacher Curl Machine",
            description = "For bicep isolation.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Biceps"),
            stepsForDoIt = """
        1. Sit down and adjust the seat so your arms rest comfortably on the angled pad.
        2. Grip the bar or handles with your palms facing up.
        3. Curl the weight up by bending your elbows.
        4. Squeeze your biceps at the top.
        5. Slowly lower the weight back to the starting position.
    """.trimIndent()
        ),
        Exercise(
            name = "Ab Crunch Machine",
            description = "Targets the rectus abdominis.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Abdominals"),
            stepsForDoIt = """
        1. Sit on the ab crunch machine and adjust the pads to fit your body.
        2. Hold the handles or position your arms on the pads.
        3. Crunch your torso forward by engaging your abdominal muscles.
        4. Pause briefly at the bottom of the movement.
        5. Slowly return to the starting position.
    """.trimIndent()
        ),
        Exercise(
            name = "Oblique Machine",
            description = "Ideal for working the lateral abdominal muscles.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Obliques"),
            stepsForDoIt = """
        1. Sit on the oblique machine and adjust the pads.
        2. Position your torso to allow a rotational movement.
        3. Rotate your torso to one side by contracting your obliques.
        4. Return to the center in a controlled motion.
        5. Repeat on the opposite side.
    """.trimIndent()
        ),
        Exercise(
            name = "Cable Crunch Machine",
            description = "Provides a more intense focus on the core.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Abdominals"),
            stepsForDoIt = """
        1. Kneel in front of the cable machine with a rope attachment.
        2. Hold the rope with both hands above your head.
        3. Crunch your torso downward, contracting your abs.
        4. Hold the contraction briefly.
        5. Slowly return to the starting position using your abs, not your lower back.
    """.trimIndent()
        ),
        Exercise(
            name = "Back Extension Machine",
            description = "Targets the lower back and lumbar area.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Lower Back"),
            stepsForDoIt = """
        1. Adjust the machine so your hips are aligned with the pivot point.
        2. Position your thighs on the pad and cross your arms over your chest.
        3. Bend your torso forward slowly.
        4. Extend your back to return to the starting position.
        5. Avoid hyperextending your back at the top.
    """.trimIndent()
        ),
        Exercise(
            name = "Trapezius Machine",
            description = "Works the upper trapezius with movements such as shoulder shrugs.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Trapezius"),
            stepsForDoIt = """
        1. Stand or sit at the machine depending on the design.
        2. Grip the handles or bar with your arms relaxed.
        3. Shrug your shoulders upward toward your ears.
        4. Squeeze your trapezius muscles at the top.
        5. Slowly lower your shoulders back down.
    """.trimIndent()
        )
    )

    val exercisesCalisthenicBasics = listOf(
        // Upper Body (Push)
        Exercise(
            name = "Push-ups",
            description = "Standard push-ups that target the chest, triceps, and shoulders.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Chest", "Triceps", "Shoulders"),
            stepsForDoIt = "1. Get into a plank position with your hands under your shoulders.\n" +
                    "2. Lower your body until your chest lightly touches the floor.\n" +
                    "3. Push your body back up to the starting position.\n"
        ),
        Exercise(
            name = "Dips",
            description = "Dips on parallel bars to target the chest and triceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Chest", "Triceps", "Shoulders"),
            stepsForDoIt = "1. Position yourself between the parallel bars with your hands on them.\n" +
                    "2. Lower your body until your elbows form a 90-degree angle.\n" +
                    "3. Push yourself back up to full arm extension.\n"
        ),
        Exercise(
            name = "Clap Push-ups",
            description = "More advanced push-ups that include a hand clap in the air.",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Chest", "Triceps", "Shoulders"),
            stepsForDoIt = "1. Perform a standard push-up.\n" +
                    "2. As you push up, generate enough force to clap your hands in the air.\n" +
                    "3. Land softly and repeat.\n"
        ),
        // Upper Body (Pull)
        Exercise(
            name = "Pull-ups",
            description = "Basic exercise to target the back, shoulders, and biceps using a bar.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Lats", "Biceps", "Traps"),
            stepsForDoIt = "1. Grip the bar with your hands at shoulder width.\n" +
                    "2. Pull your body upwards until your chin is above the bar.\n" +
                    "3. Lower yourself slowly back down to the starting position.\n"
        ),
        Exercise(
            name = "Chin-ups",
            description = "Pull-ups with a supine grip (palms facing you), targeting the biceps more.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Biceps", "Lats"),
            stepsForDoIt = "1. Grip the bar with your palms facing you.\n" +
                    "2. Pull your body up until your chin is above the bar.\n" +
                    "3. Slowly lower yourself.\n"
        ),
        Exercise(
            name = "Negative Pull-ups",
            description = "If you can't do a full pull-up yet, start at the top and lower yourself slowly.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Lats", "Biceps"),
            stepsForDoIt = "1. Jump or climb to the top position of the pull-up bar.\n" +
                    "2. Lower yourself slowly, keeping your body under control.\n" +
                    "3. Repeat the movement as many times as possible.\n"
        ),
        // Legs
        Exercise(
            name = "Squats",
            description = "Bodyweight squats to work the legs and glutes.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Quads", "Glutes", "Hamstrings"),
            stepsForDoIt = "1. Stand with your feet shoulder-width apart.\n" +
                    "2. Lower your hips as if sitting in a chair, keeping your back straight.\n" +
                    "3. Lower until your thighs are parallel to the ground, then push back up.\n"
        ),
        Exercise(
            name = "Jump Squats",
            description = "Explosive squats with a jump to work strength and power.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Quads", "Glutes", "Hamstrings"),
            stepsForDoIt = "1. Perform a regular squat.\n" +
                    "2. As you rise, explosively jump upwards.\n" +
                    "3. Land softly and repeat.\n"
        ),
        Exercise(
            name = "Lunges",
            description = "Lunges to target quads, glutes, and stability.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Quads", "Glutes", "Hamstrings"),
            stepsForDoIt = "1. Step forward with one leg.\n" +
                    "2. Lower your back knee toward the ground.\n" +
                    "3. Return to the starting position and repeat with the other leg.\n"
        ),
        Exercise(
            name = "Step-ups",
            description = "Step onto an elevated platform (such as a bench or park structure) alternating legs.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Quads", "Glutes", "Hamstrings"),
            stepsForDoIt = "1. Position yourself in front of an elevated platform.\n" +
                    "2. Step up with one leg, pushing off with the leg that steps up.\n" +
                    "3. Lower yourself with control and repeat with the other leg.\n"
        ),
        Exercise(
            name = "Calf Raises",
            description = "Exercise to target the calves, done on an elevated structure or flat ground.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Calves"),
            stepsForDoIt = "1. Stand with your feet on an elevated surface (e.g., a step).\n" +
                    "2. Raise your heels as high as possible, contracting your calves.\n" +
                    "3. Lower slowly until your heels are below your toes.\n"
        ),
        // Core (Abs)
        Exercise(
            name = "Plank",
            description = "An isometric exercise to strengthen the core.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Abs", "Core"),
            stepsForDoIt = "1. Get into a plank position, resting on your forearms and toes.\n" +
                    "2. Keep your body straight and engage your core.\n" +
                    "3. Hold the position for 30-60 seconds.\n"
        ),
        Exercise(
            name = "Crunches",
            description = "Classic exercise to target the abdominal muscles.\n",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Abs"),
            stepsForDoIt = "1. Lie on your back with your knees bent.\n" +
                    "2. Perform a small lift of your torso, contracting your abs.\n" +
                    "3. Lower yourself back slowly.\n"
        ),
        Exercise(
            name = "Leg Raises",
            description = "Exercise for the lower abs, done while hanging from a bar.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Lower Abs", "Core"),
            stepsForDoIt = "1. Hang from a bar with your legs straight.\n" +
                    "2. Raise your legs towards your chest while keeping them straight.\n" +
                    "3. Slowly lower them back down.\n"
        ),
        Exercise(
            name = "Russian Twists",
            description = "Exercise for the obliques, performed seated with a twisting motion.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Obliques", "Core"),
            stepsForDoIt = "1. Sit on the ground with your legs slightly bent.\n" +
                    "2. Lean slightly back and rotate your torso side to side, touching the ground with your hands.\n"
        ),
        Exercise(
            name = "Mountain Climbers",
            description = "Cardio exercise that also works the abs and core.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.CARDIO,
            trainedMuscles = listOf("Abs", "Core"),
            stepsForDoIt = "1. Start in a plank position.\n" +
                    "2. Drive one knee towards your chest, then quickly alternate legs.\n"
        ),
        Exercise(
            name = "Toe Touches",
            description = "Exercise for the abs, performed by touching your toes with your hands.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Upper Abs"),
            stepsForDoIt = "1. Lie on your back with your legs extended towards the ceiling.\n" +
                    "2. Lift your torso and touch your toes with your hands.\n"
        )
    )

    val weightlifting = listOf(
        // Chest Exercises
        Exercise(
            name = "Press de banca plano con barra",
            description = "Es uno de los ejercicios fundamentales para trabajar el pecho.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Pectoral mayor"),
            stepsForDoIt = "1. Lie on a flat bench. \n2. Grip the barbell at shoulder width. \n3. Lower it to the chest. \n4. Push it back up."
        ),
        Exercise(
            name = "Press de banca inclinado con barra o mancuernas",
            description = "Enfoca más en la parte superior del pecho.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Pectoral mayor"),
            stepsForDoIt = "1. Lie on an incline bench. \n2. Grip the barbell or dumbbells. \n3. Lower them to chest level. \n4. Push them back up."
        ),
        Exercise(
            name = "Fondos en paralelas",
            description = "Con el torso inclinado hacia adelante, se trabaja más el pecho.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Pectoral mayor"),
            stepsForDoIt = "1. Hold onto the parallel bars. \n2. Lower your body controlled until your elbows are at 90 degrees. \n3. Push back up."
        ),
        Exercise(
            name = "Aperturas con mancuernas en banco plano",
            description = "Para trabajar la parte externa del pecho.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Pectoral mayor"),
            stepsForDoIt = "1. Lie on a flat bench. \n2. Hold the dumbbells with slightly bent elbows. \n3. Open your arms to the sides. \n4. Bring them together."
        ),
        Exercise(
            name = "Aperturas con mancuernas en banco inclinado",
            description = "Enfoca la parte superior del pecho.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Pectoral mayor"),
            stepsForDoIt = "1. Lie on an incline bench. \n2. Perform the flyes with dumbbells. \n3. Focus on the upper chest."
        ),

        // Back Exercises
        Exercise(
            name = "Peso muerto",
            description = "Fundamental para trabajar la espalda baja, glúteos y piernas.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Espalda baja", "Glúteos", "Isquiotibiales"),
            stepsForDoIt = "1. Place your feet at hip width. \n2. Grip the barbell with your hands by your legs. \n3. Lower the bar while keeping your back straight. \n4. Lift it back up."
        ),
        Exercise(
            name = "Dominadas",
            description = "Un clásico que trabaja principalmente la espalda, aunque también involucra otros músculos.",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Dorsales", "Bíceps"),
            stepsForDoIt = "1. Grab the bar with your hands at shoulder width. \n2. Pull your body up until your chin passes the bar. \n3. Lower back down."
        ),
        Exercise(
            name = "Remo con barra",
            description = "Trabaja la parte media de la espalda.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Dorsales", "Trapecio", "Romboides"),
            stepsForDoIt = "1. Bend forward, keeping your back straight. \n2. Grip the barbell at shoulder width. \n3. Row it towards your abdomen."
        ),
        Exercise(
            name = "Remo con mancuernas a un brazo",
            description = "Excelente para enfocar los músculos de la espalda de forma unilateral.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Dorsales", "Trapecio"),
            stepsForDoIt = "1. Place one knee and hand on a bench. \n2. Grip a dumbbell with the other hand. \n3. Row the elbow back, focusing on the contraction of the back."
        ),
        Exercise(
            name = "Pull-over con mancuerna",
            description = "Aísla la zona de la espalda superior.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Dorsales", "Pectoral mayor"),
            stepsForDoIt = "1. Lie on a bench. \n2. Hold the dumbbell with both hands. \n3. Lower it behind your head. \n4. Bring it back up."
        ),
        Exercise(
            name = "Jalones en polea (lat pulldown)",
            description = "Similar a las dominadas pero con polea.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Dorsales"),
            stepsForDoIt = "1. Grip the lat pulldown bar with a wide grip. \n2. Pull the bar down to chest level."
        ),

        // Leg Exercises
        Exercise(
            name = "Sentadillas con barra",
            description = "El rey de los ejercicios para piernas.",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Cuádriceps", "Glúteos", "Isquiotibiales"),
            stepsForDoIt = "1. Place the barbell on your traps. \n2. Squat down keeping your back straight until your thighs are parallel to the floor. \n3. Push back up."
        ),
        Exercise(
            name = "Prensa de piernas",
            description = "Similar a las sentadillas pero con diferente ángulo y carga.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Cuádriceps", "Glúteos"),
            stepsForDoIt = "1. Sit in the leg press machine. \n2. Place your feet on the platform. \n3. Push upward extending your legs."
        ),
        Exercise(
            name = "Peso muerto rumano",
            description = "Para trabajar los isquiotibiales y glúteos.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Isquiotibiales", "Glúteos"),
            stepsForDoIt = "1. With the barbell in front of your legs, bend forward while keeping your legs almost straight. \n2. Lower the barbell below your knees. \n3. Lift it back up."
        ),
        Exercise(
            name = "Zancadas (lunges) con mancuernas",
            description = "Aislando cada pierna.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Cuádriceps", "Glúteos", "Isquiotibiales"),
            stepsForDoIt = "1. Take a long step forward. \n2. Lower your front leg to 90 degrees. \n3. Push back up."
        ),
        Exercise(
            name = "Elevaciones de talones (gemelos) de pie o sentado",
            description = "Para trabajar los gemelos.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Gémeos"),
            stepsForDoIt = "1. Stand on a platform or bench. \n2. Raise your heels as high as possible. \n3. Lower back down in a controlled manner."
        ),
        Exercise(
            name = "Military Press with Barbell or Dumbbells",
            description = "To work the deltoids.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Deltoids"),
            stepsForDoIt = """
            1. Hold the barbell or dumbbells at shoulder height.
            2. Press the dumbbells or barbell overhead until your arms are fully extended.
            3. Lower back down in a controlled manner to the starting position.
        """.trimIndent()
        ),
        Exercise(
            name = "Lateral Raises with Dumbbells",
            description = "For the middle deltoid.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Middle deltoid"),
            stepsForDoIt = """
            1. Hold a dumbbell in each hand with your arms slightly bent.
            2. Raise the dumbbells to the sides until your arms are parallel to the ground.
            3. Lower them slowly back to the starting position.
        """.trimIndent()
        ),
        Exercise(
            name = "Front Raises with Dumbbells or Barbell",
            description = "For the anterior deltoid.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Anterior deltoid"),
            stepsForDoIt = """
            1. Hold a dumbbell in each hand or the barbell with an overhand grip.
            2. Raise the dumbbells or barbell to the front until your arms are parallel to the ground.
            3. Lower back down slowly to the starting position.
        """.trimIndent()
        ),
        Exercise(
            name = "Reverse Fly with Dumbbells",
            description = "To target the rear deltoids.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Rear deltoid"),
            stepsForDoIt = """
            1. Bend forward at the waist while keeping your back straight.
            2. Hold a dumbbell in each hand with your arms extended down.
            3. Raise the dumbbells out to the sides, keeping your elbows slightly bent.
            4. Lower back down in a controlled manner to the starting position.
        """.trimIndent()
        ),
        Exercise(
            name = "Arnold Press",
            description = "A variation of the military press that works the deltoids more comprehensively.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Deltoids"),
            stepsForDoIt = """
            1. Hold the dumbbells in front of you with palms facing inward.
            2. Rotate your wrists as you press the dumbbells overhead.
            3. Lower back down in a controlled manner to the starting position.
        """.trimIndent()
        ),
        Exercise(
            name = "Barbell Curl",
            description = "A basic exercise for the biceps.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Biceps brachii"),
            stepsForDoIt = """
            1. Hold the barbell with an underhand grip (palms facing up).
            2. Keep the bar close to your body and curl it toward your shoulders.
            3. Lower it back down in a controlled manner to the starting position.
        """.trimIndent()
        ),
        Exercise(
            name = "Dumbbell Curl",
            description = "Can be done alternately or simultaneously.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Biceps brachii"),
            stepsForDoIt = """
            1. Hold a dumbbell in each hand with your elbows close to your body.
            2. Curl the dumbbells toward your shoulders.
            3. Lower them slowly back to the starting position.
        """.trimIndent()
        ),
        Exercise(
            name = "Hammer Curl with Dumbbells",
            description = "Also works the forearms and outer biceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Biceps brachii", "Brachioradialis"),
            stepsForDoIt = """
            1. Hold the dumbbells with palms facing each other.
            2. Curl the dumbbells toward your shoulders, keeping your elbows close to your body.
            3. Lower them back down in a controlled manner to the starting position.
        """.trimIndent()
        ),
        Exercise(
            name = "Concentration Curl",
            description = "To isolate the biceps and maximize contraction.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Biceps brachii"),
            stepsForDoIt = """
            1. Sit on a bench and rest your elbow on your thigh.
            2. Curl the dumbbell toward your shoulder, focusing the effort on the biceps.
            3. Lower back down slowly to the starting position.
        """.trimIndent()
        ),
        Exercise(
            name = "Preacher Curl",
            description = "Completely isolates the biceps, eliminating momentum.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Biceps brachii"),
            stepsForDoIt = """
            1. Rest your arms on the preacher curl pad.
            2. Hold the bar or dumbbells with palms facing up.
            3. Curl the weight toward your shoulders.
            4. Lower it back down in a controlled manner to the starting position.
        """.trimIndent()
        ),
        Exercise(
            name = "Bench Dips",
            description = "A great exercise for the triceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Triceps brachii"),
            stepsForDoIt = """
            1. Place your hands on the benches and your heels on the ground.
            2. Lower your torso in a controlled manner by bending your elbows to about 90 degrees.
            3. Push back up to the starting position, fully extending your arms.
        """.trimIndent()
        ),
        Exercise(
            name = "Overhead Triceps Extension with Dumbbell",
            description = "To target the long head of the triceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Triceps brachii"),
            stepsForDoIt = """
            1. Hold a dumbbell with both hands and extend your arms overhead.
            2. Lower the dumbbell behind your head in a controlled manner.
            3. Extend your elbows to press the dumbbell back overhead.
        """.trimIndent()
        ),
        Exercise(
            name = "French Press with Barbell or Dumbbells",
            description = "A classic triceps exercise.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Triceps brachii"),
            stepsForDoIt = """
            1. Hold the barbell or dumbbells with an overhand grip, keeping your hands close together.
            2. Lower the weight toward your forehead in a controlled manner.
            3. Extend your elbows to press the weight back up.
        """.trimIndent()
        ),
        Exercise(
            name = "Triceps Rope Extension on High Pulley",
            description = "To isolate the triceps more effectively.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Triceps brachii"),
            stepsForDoIt = """
            1. Grip the rope on the high pulley with palms facing down.
            2. Push the rope downward by extending your elbows.
            3. Return to the starting position in a controlled manner.
        """.trimIndent()
        ),
        Exercise(
            name = "Triceps Kickback with Dumbbell",
            description = "Isolating the triceps at the back of the arm.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Triceps brachii"),
            stepsForDoIt = """
            1. Lean forward while keeping your back straight.
            2. Hold a dumbbell with one hand, with your elbow bent.
            3. Extend your arm backward until it is fully straight.
            4. Lower the dumbbell back to the starting position slowly.
        """.trimIndent()
        ),
        Exercise(
            name = "Weighted Crunch",
            description = "You can add a barbell or dumbbell to your chest.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Rectus abdominis"),
            stepsForDoIt = """
            1. Lie on a bench and hold the weight on your chest.
            2. Perform a crunch by lifting your torso towards your knees.
            3. Lower slowly back to the starting position.
        """.trimIndent()
        ),
        Exercise(
            name = "Leg Raises with Weight",
            description = "To target the lower abs.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Rectus abdominis"),
            stepsForDoIt = """
            1. Lie on a bench or on the floor, holding a weight between your feet.
            2. Raise your legs until they form a 90-degree angle.
            3. Slowly lower your legs back to the starting position.
        """.trimIndent()
        ),
        Exercise(
            name = "Russian Twist with Dumbbell or Plate",
            description = "To target the obliques.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedMuscles = listOf("Obliques"),
            stepsForDoIt = """
            1. Sit with your legs elevated and your torso upright.
            2. Twist your torso from side to side while holding the weight with both hands.
            3. Maintain control throughout the movement, avoiding jerky motions.
        """.trimIndent()
        ),
        Exercise(
            name = "Weighted Plank",
            description = "To strengthen the core in general.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Abdominals"),
            stepsForDoIt = """
            1. Get into a plank position with your elbows under your shoulders.
            2. Place a weight on your back if desired for added intensity.
            3. Hold the position for as long as you can while maintaining proper form.
        """.trimIndent()
        )
    )

    val tensExercises = listOf(
        Exercise(
            "Combinations of tension exercises",
            "Tens exercise combos without getting off the parallel bar",
            ExerciseLevel.ADVANCED,
            TypeExercise.TENS,
            emptyList(),
            "1. You do a tension exercise.\n" +
                    "2. When you finish the exercise, you move on to another tension exercise without getting off the parallel bar or pull-up bar.\n" +
                    "3. After completing all the exercises, you get off the bar, rest for a few minutes, and then start over.\n",
            TypeTensExercise.ALL
        ),
        Exercise(
            "Press plank",
            "Press plank: go up to handstand and return to plank position. Use a resistance band",
            ExerciseLevel.ADVANCED,
            TypeExercise.TENS,
            emptyList(),
            "1. Hang the resistance band from a pull-up bar and tie a secure knot.\n" +
                    "2. Leave a loop, step into it, and make sure the elastic band rests around your hips.\n" +
                    "3. Get into a plank position, then lift your legs up into a handstand, and return back down into the plank",
            TypeTensExercise.PLANK
        ),
        Exercise(
            "Push-up plank",
            "Push-up plank: lower as much as possible and return to plank. Use a resistance band",
            ExerciseLevel.ADVANCED,
            TypeExercise.TENS,
            emptyList(),
            "1. Hang the resistance band from a pull-up bar and tie a secure knot.\n" +
                    "2. Leave a loop, step into it, and make sure the elastic band rests around your hips.\n" +
                    "3. Get into a plank position, then do you a push-up, without puffing out your chest, and return back up into the plank",
            TypeTensExercise.PLANK
        ),
        Exercise(
            "Front lever press",
            "Front lever press: lift legs to touch the bar, then return to front lever. Use a resistance band",
            ExerciseLevel.ADVANCED,
            TypeExercise.TENS,
            emptyList(),
            "1. Hang the resistance band from a parallel bar.\n" +
                    "2. Step into the band so that one part is attached to the bar and the other wraps around your hips. Tuck your legs into the lower part of the band.\n" +
                    "3. Get into a front lever position, then lift your legs up until you're upside down, and return back down to the front lever.",
            TypeTensExercise.FRONT_LEVEL
        ),
        Exercise(
            "Front lever pull-up",
            "Front lever pull-up: pull up to touch the bar, then return to front lever. Use a resistance band",
            ExerciseLevel.ADVANCED,
            TypeExercise.TENS,
            emptyList(),
            "1. Hang the resistance band from a parallel bar.\n" +
                    "2. Step into the band so that one part is attached to the bar and the other wraps around your hips. Tuck your legs into the lower part of the band.\n" +
                    "3. Get into a front lever position, then lift your body up until which your hips touches the parallel bar, and return back down to the front lever.",
            TypeTensExercise.FRONT_LEVEL
        ),
    )
}