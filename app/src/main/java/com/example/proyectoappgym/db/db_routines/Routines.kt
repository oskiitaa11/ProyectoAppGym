package com.example.proyectoappgym.db.db_routines

import com.example.proyectoappgym.entity.DayOfWeek
import com.example.proyectoappgym.entity.Exercise
import com.example.proyectoappgym.entity.ExerciseLevel
import com.example.proyectoappgym.entity.TensExercise
import com.example.proyectoappgym.entity.TypeExercise
import com.example.proyectoappgym.entity.TypeTensExercise

object Routines {
    val machinesRoutines = listOf(
        Exercise(
            name = "Chest Press Machine (Peck Deck or Chest Press Machine)",
            description = "To work the pectorals, primarily the pectoralis major.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Pectorals"),
            stepsForDoIt = "Sit on the machine and adjust the arm position. Push the levers forward, keeping your elbows slightly bent."
        ),
        Exercise(
            name = "Peck Deck Machine",
            description = "Excellent for working the inner part of the pectorals and focusing on muscle contraction.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Inner Pectorals"),
            stepsForDoIt = "Sit on the machine with your forearms on the pads, and bring your hands toward the center of your chest, contracting your pectorals."
        ),
        Exercise(
            name = "Incline Press Machine",
            description = "Works the upper part of the pectoral muscle.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Upper Pectorals"),
            stepsForDoIt = "Adjust the seat so the arms are at a 45-degree angle. Push the levers upward and inward."
        ),
        Exercise(
            name = "Lat Pulldown Machine",
            description = "Ideal for working the lat muscles (latissimus dorsi).",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Lats"),
            stepsForDoIt = "Sit on the machine, adjust the bar to chest height, and pull it down until it touches your chest, keeping your back straight."
        ),
        Exercise(
            name = "Seated Row Machine",
            description = "Works the middle back and trapezius.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Middle Back", "Trapezius"),
            stepsForDoIt = "Sit with your feet on the platform, grab the handles, and pull them toward your torso, squeezing your shoulder blades at the end of the movement."
        ),
        Exercise(
            name = "Pull Over Machine",
            description = "Works the lats and upper back area.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Lats", "Upper Back"),
            stepsForDoIt = "Grab the handles with your hands extended, then pull them down and back to activate the lats and upper back."
        ),
        Exercise(
            name = "Leg Press Machine",
            description = "Primarily works the quadriceps, glutes, and to a lesser extent the hamstrings.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Quadriceps", "Glutes", "Hamstrings"),
            stepsForDoIt = "Place your feet on the platform and push upward. Keep your feet aligned and avoid locking your knees at the top of the movement."
        ),
        Exercise(
            name = "Leg Extension Machine",
            description = "Ideal for isolating the quadriceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Quadriceps"),
            stepsForDoIt = "Sit on the machine, adjust the pads to your legs, and extend your legs until fully straight."
        ),
        Exercise(
            name = "Leg Curl Machine",
            description = "Works the hamstrings.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Hamstrings"),
            stepsForDoIt = "Lie face down, place your legs under the pads, and perform a knee flexion movement."
        ),
        Exercise(
            name = "Abductor/Adductor Machine",
            description = "Works the inner and outer thigh muscles (adductors and abductors).",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Adductors"),
            stepsForDoIt = "Sit on the machine, place your legs inside the pads, and perform a movement of opening and closing the legs."
        ),
        Exercise(
            name = "Glute Press Machine",
            description = "Focuses on the glutes and to a lesser extent the thighs.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Glutes", "Thighs"),
            stepsForDoIt = "Adjust the machine, place your feet on the platform, and push backward with your glutes."
        ),
        Exercise(
            name = "Shoulder Press Machine",
            description = "Works the deltoid muscles and secondary muscles such as the triceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Deltoids", "Triceps"),
            stepsForDoIt = "Sit on the machine and push the handles upward, keeping your elbows slightly bent at the top of the movement."
        ),
        Exercise(
            name = "Reverse Pec Deck Machine",
            description = "Works the posterior deltoid and trapezius muscles.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Posterior Deltoids", "Trapezius"),
            stepsForDoIt = "Sit on the machine, place your arms on the pads, and open your arms backward to activate the muscles in the rear of the shoulder."
        ),
        Exercise(
            name = "Lateral Raise Machine",
            description = "Isolates the middle part of the deltoid.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Middle Deltoids"),
            stepsForDoIt = "Adjust the seat of the machine and raise your arms out to the sides, keeping your elbows slightly bent."
        ),
        Exercise(
            name = "Bicep Curl Machine",
            description = "Perfect for isolating the biceps and working the inner part of the arm.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Biceps"),
            stepsForDoIt = "Grab the handles with your palms facing upward and perform a curling movement by bending your elbows."
        ),
        Exercise(
            name = "Tricep Extension Machine",
            description = "Works the posterior part of the arm, specifically the triceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Triceps"),
            stepsForDoIt = "Grab the handles with your palms facing down and extend your arms until they are fully straight."
        ),
        Exercise(
            name = "Preacher Curl Machine",
            description = "For bicep isolation.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Biceps"),
            stepsForDoIt = "Place your arms on the bench and perform a curl movement, maintaining proper form."
        ),
        Exercise(
            name = "Ab Crunch Machine",
            description = "Targets the rectus abdominis.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Abdominals"),
            stepsForDoIt = "Sit on the machine, adjust the pad, and perform a torso flexion movement to engage the abdominal muscles."
        ),
        Exercise(
            name = "Oblique Machine",
            description = "Ideal for working the lateral abdominal muscles.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Obliques"),
            stepsForDoIt = "Place your body in the machine and perform a twisting movement of your torso to target the obliques."
        ),
        Exercise(
            name = "Cable Crunch Machine",
            description = "Provides a more intense focus on the core.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Abdominals"),
            stepsForDoIt = "Grab the handles and perform a torso flexion movement downward, contracting the abdominal muscles."
        ),
        Exercise(
            name = "Back Extension Machine",
            description = "Targets the lower back and lumbar area.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Lower Back"),
            stepsForDoIt = "Adjust the machine, place your legs in the pads, and perform a back extension movement."
        ),
        Exercise(
            name = "Trapezius Machine",
            description = "Works the upper trapezius with movements such as shoulder shrugs.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedMuscles = listOf("Trapezius"),
            stepsForDoIt = "Perform a shoulder shrug movement, focusing on contracting the trapezius muscles."
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
            stepsForDoIt = "1. Get into a plank position with your hands under your shoulders. " +
                    "2. Lower your body until your chest lightly touches the floor. " +
                    "3. Push your body back up to the starting position."
        ),
        Exercise(
            name = "Dips",
            description = "Dips on parallel bars to target the chest and triceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Chest", "Triceps", "Shoulders"),
            stepsForDoIt = "1. Position yourself between the parallel bars with your hands on them. " +
                    "2. Lower your body until your elbows form a 90-degree angle. " +
                    "3. Push yourself back up to full arm extension."
        ),
        Exercise(
            name = "Clap Push-ups",
            description = "More advanced push-ups that include a hand clap in the air.",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Chest", "Triceps", "Shoulders"),
            stepsForDoIt = "1. Perform a standard push-up. " +
                    "2. As you push up, generate enough force to clap your hands in the air. " +
                    "3. Land softly and repeat."
        ),
        // Upper Body (Pull)
        Exercise(
            name = "Pull-ups",
            description = "Basic exercise to target the back, shoulders, and biceps using a bar.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Lats", "Biceps", "Traps"),
            stepsForDoIt = "1. Grip the bar with your hands at shoulder width. " +
                    "2. Pull your body upwards until your chin is above the bar. " +
                    "3. Lower yourself slowly back down to the starting position."
        ),
        Exercise(
            name = "Chin-ups",
            description = "Pull-ups with a supine grip (palms facing you), targeting the biceps more.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Biceps", "Lats"),
            stepsForDoIt = "1. Grip the bar with your palms facing you. " +
                    "2. Pull your body up until your chin is above the bar. " +
                    "3. Slowly lower yourself."
        ),
        Exercise(
            name = "Negative Pull-ups",
            description = "If you can't do a full pull-up yet, start at the top and lower yourself slowly.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Lats", "Biceps"),
            stepsForDoIt = "1. Jump or climb to the top position of the pull-up bar. " +
                    "2. Lower yourself slowly, keeping your body under control. " +
                    "3. Repeat the movement as many times as possible."
        ),
        // Legs
        Exercise(
            name = "Squats",
            description = "Bodyweight squats to work the legs and glutes.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Quads", "Glutes", "Hamstrings"),
            stepsForDoIt = "1. Stand with your feet shoulder-width apart. " +
                    "2. Lower your hips as if sitting in a chair, keeping your back straight. " +
                    "3. Lower until your thighs are parallel to the ground, then push back up."
        ),
        Exercise(
            name = "Jump Squats",
            description = "Explosive squats with a jump to work strength and power.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Quads", "Glutes", "Hamstrings"),
            stepsForDoIt = "1. Perform a regular squat. " +
                    "2. As you rise, explosively jump upwards. " +
                    "3. Land softly and repeat."
        ),
        Exercise(
            name = "Lunges",
            description = "Lunges to target quads, glutes, and stability.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Quads", "Glutes", "Hamstrings"),
            stepsForDoIt = "1. Step forward with one leg. " +
                    "2. Lower your back knee toward the ground. " +
                    "3. Return to the starting position and repeat with the other leg."
        ),
        Exercise(
            name = "Step-ups",
            description = "Step onto an elevated platform (such as a bench or park structure) alternating legs.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Quads", "Glutes", "Hamstrings"),
            stepsForDoIt = "1. Position yourself in front of an elevated platform. " +
                    "2. Step up with one leg, pushing off with the leg that steps up. " +
                    "3. Lower yourself with control and repeat with the other leg."
        ),
        Exercise(
            name = "Calf Raises",
            description = "Exercise to target the calves, done on an elevated structure or flat ground.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Calves"),
            stepsForDoIt = "1. Stand with your feet on an elevated surface (e.g., a step). " +
                    "2. Raise your heels as high as possible, contracting your calves. " +
                    "3. Lower slowly until your heels are below your toes."
        ),
        // Core (Abs)
        Exercise(
            name = "Plank",
            description = "An isometric exercise to strengthen the core.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Abs", "Core"),
            stepsForDoIt = "1. Get into a plank position, resting on your forearms and toes. " +
                    "2. Keep your body straight and engage your core. " +
                    "3. Hold the position for 30-60 seconds."
        ),
        Exercise(
            name = "Crunches",
            description = "Classic exercise to target the abdominal muscles.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Abs"),
            stepsForDoIt = "1. Lie on your back with your knees bent. " +
                    "2. Perform a small lift of your torso, contracting your abs. " +
                    "3. Lower yourself back slowly."
        ),
        Exercise(
            name = "Leg Raises",
            description = "Exercise for the lower abs, done while hanging from a bar.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Lower Abs", "Core"),
            stepsForDoIt = "1. Hang from a bar with your legs straight. " +
                    "2. Raise your legs towards your chest while keeping them straight. " +
                    "3. Slowly lower them back down."
        ),
        Exercise(
            name = "Russian Twists",
            description = "Exercise for the obliques, performed seated with a twisting motion.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Obliques", "Core"),
            stepsForDoIt = "1. Sit on the ground with your legs slightly bent. " +
                    "2. Lean slightly back and rotate your torso side to side, touching the ground with your hands."
        ),
        Exercise(
            name = "Mountain Climbers",
            description = "Cardio exercise that also works the abs and core.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.CARDIO,
            trainedMuscles = listOf("Abs", "Core"),
            stepsForDoIt = "1. Start in a plank position. " +
                    "2. Drive one knee towards your chest, then quickly alternate legs."
        ),
        Exercise(
            name = "Toe Touches",
            description = "Exercise for the abs, performed by touching your toes with your hands.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedMuscles = listOf("Upper Abs"),
            stepsForDoIt = "1. Lie on your back with your legs extended towards the ceiling. " +
                    "2. Lift your torso and touch your toes with your hands."
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
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
        """
        )
    )

    val tensRoutine = listOf(
        TensExercise("Tens exercise combos without getting off the parallel bar", TypeTensExercise.ALL),
        TensExercise("Press plank: go up to handstand and return to plank position. Use a resistance band", TypeTensExercise.PLANK),
        TensExercise("Push-up plank: lower as much as possible and return to plank. Use a resistance band", TypeTensExercise.PLANK),
        TensExercise("Maltese plank push-up: lower as much as possible and return to maltese plank position", TypeTensExercise.PLANK),
        TensExercise("Handstand push-up: lower until your head touches the floor, then return to handstand position", TypeTensExercise.PLANK),
        TensExercise("Front lever press: lift legs to touch the bar, then return to front lever. Use a resistance band", TypeTensExercise.FRONT_LEVEL),
        TensExercise("Front lever pull-up: pull up to touch the bar, then return to front lever. Use a resistance band", TypeTensExercise.FRONT_LEVEL),
        TensExercise("Maltese front lever press: lift legs to touch the bar, then return to front lever", TypeTensExercise.FRONT_LEVEL),
        TensExercise("Pull-up hold: pull until your chin is above the bar, hold for 3 seconds, then lower. Do it without a band if possible", TypeTensExercise.FRONT_LEVEL))
}