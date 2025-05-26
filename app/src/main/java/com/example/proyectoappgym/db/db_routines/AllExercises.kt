package com.example.proyectoappgym.db.db_routines

import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.Exercise
import com.example.proyectoappgym.entity.ExerciseLevel
import com.example.proyectoappgym.entity.Muscles
import com.example.proyectoappgym.entity.TensExercise
import com.example.proyectoappgym.entity.TypeExercise
import com.example.proyectoappgym.entity.TypeTensExercise

object AllExercises {
    val machinesRoutines = listOf(
        Exercise(
            name = "Chest Press Machine",
            nameVideo = "chest_press_machine",
            description = "To work the pectorals, primarily the pectoralis major.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.PECTORALS),
            trainedSecondaryMuscles = listOf(Muscles.FRONT_DELTOID, Muscles.TRICEPS),
            stepsForDoIt = """
        1. Sit on the machine and adjust the seat so that the handles are at chest height.
        2. Place your hands on the handles with a firm grip.
        3. Push the handles forward until your arms are almost fully extended, keeping a slight bend in your elbows.
        4. Slowly return to the starting position, controlling the movement.
        5. Repeat the desired number of repetitions.
    """.trimIndent(),
            idCoverImage = R.drawable.chest_press_machine,
            idImageMuscles = R.drawable.muscles_chest_press_machine
        ),
        Exercise(
            name = "Peck Deck Machine",
            nameVideo = "peck_deck_machine",
            description = "Excellent for working the inner part of the pectorals and focusing on muscle contraction.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.PECTORALS),
            trainedSecondaryMuscles = listOf(Muscles.FRONT_DELTOID, Muscles.TRICEPS),
            stepsForDoIt = """
        1. Sit on the machine with your back straight and adjust the seat height.
        2. Place your forearms on the pads or your hands on the handles, depending on the model.
        3. Bring your arms together toward the center of your chest, contracting your pectorals.
        4. Pause for a second at the peak contraction.
        5. Slowly return to the starting position.
        6. Repeat the exercise according to your scheduled repetitions.
    """.trimIndent(),
            idCoverImage = R.drawable.peck_deck_machine,
            idImageMuscles = R.drawable.muscles_peck_deck_machine
        ),
        Exercise(
            name = "Incline Chest Press",
            nameVideo = "incline_chest_press",
            description = "Works the upper part of the pectoral muscle.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.UPPER_PECTORALS),
            trainedSecondaryMuscles = listOf(Muscles.FRONT_DELTOID, Muscles.TRICEPS),
            stepsForDoIt = """
        1. Adjust the seat so that the handles align with the upper part of your chest.
        2. Sit with your back supported by the seat.
        3. Grip the handles firmly.
        4. Push upward and slightly forward, extending your arms.
        5. Stop the movement without locking your elbows.
        6. Slowly return to the starting position.
    """.trimIndent(),
            idCoverImage = R.drawable.incline_chest_press,
            idImageMuscles = R.drawable.muscles_incline_chest_press
        ),
        Exercise(
            name = "Lat Pulldown Machine",
            nameVideo = "lat_pulldown_machine",
            description = "Ideal for working the lat muscles (latissimus dorsi).",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.LATISSIMUS_DORSI),
            trainedSecondaryMuscles = listOf(
                Muscles.BRACHIALIS,
                Muscles.BRACHIORADIALIS,
                Muscles.DELTOIDS,
                Muscles.INFRASPINATUS,
                Muscles.TERES_MAJOR,
                Muscles.TERES_MINOR,
                Muscles.TRAPEZIUS
            ),
            stepsForDoIt = """
        1. Sit down and adjust the pad over your thighs.
        2. Grip the bar with a wide and firm grip.
        3. Pull the bar down until it touches the upper part of your chest.
        4. Keep your torso straight without leaning back.
        5. Slowly return the bar to the starting position.
    """.trimIndent(),
            idCoverImage = R.drawable.lat_pulldown_machine,
            idImageMuscles = R.drawable.muscles_lat_pulldown_machine
        ),
        Exercise(
            name = "Seated Row Machine",
            nameVideo = "seated_row_machine",
            description = "Works the middle back and trapezius.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(
                Muscles.TRAPEZIUS,
                Muscles.LATISSIMUS_DORSI,
                Muscles.TERES_MAJOR,
                Muscles.TERES_MINOR,
                Muscles.INFRASPINATUS
            ),
            trainedSecondaryMuscles = listOf(
                Muscles.BRACHIALIS,
                Muscles.BRACHIORADIALIS,
                Muscles.DELTOIDS
            ),
            stepsForDoIt = """
        1. Sit down with your feet planted on the platform.
        2. Grip the handles with both hands.
        3. Pull the handles toward your abdomen while keeping your back straight.
        4. Squeeze your shoulder blades together at the end of the movement.
        5. Slowly return to the starting position without letting the weight drop.
    """.trimIndent(),
            idCoverImage = R.drawable.seated_row_machine,
            idImageMuscles = R.drawable.muscles_seated_row_machine
        ),
        Exercise(
            name = "Pull Over Machine",
            nameVideo = "pull_over_machine",
            description = "Works the lats and upper back area.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(
                Muscles.INFRASPINATUS,
                Muscles.LATISSIMUS_DORSI,
                Muscles.TERES_MAJOR,
                Muscles.TERES_MINOR,
                Muscles.TRAPEZIUS
            ),
            trainedSecondaryMuscles = listOf(
                Muscles.DELTOIDS,
                Muscles.LATISSIMUS_DORSI,
                Muscles.TERES_MAJOR,
                Muscles.TRICEPS
            ),
            stepsForDoIt = """
        1. Sit on the machine and adjust the backrest and arm height.
        2. Grip the handles with your arms extended upward.
        3. Lower your arms in an arc toward your waist, keeping your elbows slightly bent.
        4. Contract your lats at the end of the movement.
        5. Slowly return to the starting position.
    """.trimIndent(),
            idCoverImage = R.drawable.pull_over_machine,
            idImageMuscles = R.drawable.muscles_pull_over_machine
        ),
        Exercise(
            name = "Leg Press Machine",
            nameVideo = "leg_press_machine",
            description = "Primarily works the quadriceps, glutes, and to a lesser extent the hamstrings.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.QUADRICEPS, Muscles.GLUTEUS, Muscles.HAMSTRINGS),
            trainedSecondaryMuscles = listOf(Muscles.GLUTEUS, Muscles.QUADRICEPS),
            stepsForDoIt = """
        1. Sit on the machine and place your feet on the platform shoulder-width apart.
        2. Release the safety locks if necessary.
        3. Push the platform with your feet until your legs are almost fully extended.
        4. Avoid locking your knees at the extension.
        5. Slowly return to the starting position with control.
    """.trimIndent(),
            idCoverImage = R.drawable.leg_press_machine,
            idImageMuscles = R.drawable.muscles_leg_press_machine
        ),
        Exercise(
            name = "Leg Extension Press Machine",
            nameVideo = "leg_extension_press_machine",
            description = "Ideal for isolating the quadriceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.QUADRICEPS),
            trainedSecondaryMuscles = emptyList(),
            stepsForDoIt = """
        1. Sit on the leg extension machine.
        2. Adjust the pads so they rest just above your ankles.
        3. Hold the side handles for stability.
        4. Extend your legs forward until they are fully straight.
        5. Hold the position briefly at the top.
        6. Slowly return to the starting position.
    """.trimIndent(),
            idCoverImage = R.drawable.leg_extension_press_machine,
            idImageMuscles = R.drawable.muscles_leg_extension_press_machine
        ),
        Exercise(
            name = "Leg Curl Machine",
            nameVideo = "leg_curl_machine",
            description = "Works the hamstrings.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.HAMSTRINGS),
            trainedSecondaryMuscles = listOf(Muscles.GASTROCNEMIUS, Muscles.CALVES),
            stepsForDoIt = """
    1. Lie face down on the leg curl machine.
    2. Place your legs under the padded rollers.
    3. Hold the handles for stability if available.
    4. Curl your legs by bending your knees, bringing the pads toward your glutes.
    5. Squeeze your hamstrings at the top of the movement.
    6. Slowly return to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.leg_curl_machine,
            idImageMuscles = R.drawable.muscles_leg_curl_machine
        ),
        Exercise(
            name = "Abductor Machine",
            nameVideo = "abductor_machine",
            description = "Works the inner and outer thigh muscles (adductors and abductors).",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.ADDUCTORS),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
    1. Sit on the machine and adjust the pads to your desired position.
    2. Place your legs inside or outside the pads depending on the targeted muscles.
    3. Hold the side handles to maintain control.
    4. Push outward or inward with your legs against the machine's resistance.
    5. Perform the movement slowly and controlled.
    6. Return to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.abductor_machine,
            idImageMuscles = R.drawable.muscles_abductor_machine
        ),
        Exercise(
            name = "Cable Pull Through",
            nameVideo = "cable_pull_through",
            description = "This exercise is excellent for improving hip hinge mechanics, strengthening the glutes, " +
                    "and reducing strain on the lower back, making it a great alternative to free-weight exercises " +
                    "like the deadlift.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.GLUTEUS),
            trainedSecondaryMuscles = listOf(Muscles.HAMSTRINGS, Muscles.LOWER_BACK),
            stepsForDoIt = """
    1. Set up the cable machine: Attach a rope handle to the lowest pulley setting. Choose a 
        moderate weight that allows for full control without compromising form.
    2. Position yourself: Stand facing away from the machine, feet flat and slightly wider than hip-width apart.
    3. Grab the rope: Reach between your legs and hold the rope handles with both hands, arms straight, 
       and palms facing each other.
    4. Maintain a neutral spine: Keep your back straight, core engaged, and knees slightly bent.
    5. Hinge at the hips: Push your hips back as far as possible, allowing the cable to pull your torso downward. 
       This movement should be controlled, focusing on muscle tension and time under tension.
    6. Drive through your feet: Push through your entire foot and squeeze your glutes as you pull the cable 
       back up, extending your hips fully without overextending your spine.
""".trimIndent(),
            idCoverImage = R.drawable.cable_pull_through,
            idImageMuscles = R.drawable.muscles_cable_pull_through
        ),
        Exercise(
            name = "Shoulder Press Machine",
            nameVideo = "shoulder_press_machine",
            description = "Works the deltoid muscles and secondary muscles such as the triceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.FRONT_DELTOID),
            trainedSecondaryMuscles = listOf(Muscles.TRICEPS, Muscles.MIDDLE_DELTOID),
            stepsForDoIt = """
    1. Sit on the machine and adjust the seat so the handles are at shoulder height.
    2. Grip the handles firmly.
    3. Press the handles upward until your arms are almost fully extended.
    4. Avoid locking your elbows at the top.
    5. Slowly lower the handles back to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.shoulder_press_machine,
            idImageMuscles = R.drawable.muscles_shoulder_press_machine
        ),
        Exercise(
            name = "Lever Seated Row",
            nameVideo = "lever_seated_row",
            description = "It is an excellent movement for developing a thicker and stronger back, while minimizing lower back strain due to the seated and supported position.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(
                Muscles.INFRASPINATUS,
                Muscles.LATISSIMUS_DORSI,
                Muscles.TERES_MAJOR,
                Muscles.TERES_MINOR,
                Muscles.TRAPEZIUS
            ),
            trainedSecondaryMuscles = listOf(
                Muscles.BRACHIALIS,
                Muscles.BRACHIORADIALIS,
                Muscles.DELTOIDS
            ),
            stepsForDoIt = """
            1. Adjust the machine seat so that the chest pad rests comfortably against your chest and the handles are at shoulder level.
            2. Sit down on the seat and place your feet on the footrests, keeping your knees slightly bent.
            3. Grab the handles with a neutral or overhand grip (depending on the machine), keeping your arms fully extended without locking your elbows.
            4. Brace your core and maintain a straight spine with your chest pressed against the pad.
            5. Pull the handles back toward your torso by retracting your shoulder blades (scapular retraction), then bending your elbows. Focus on squeezing your shoulder blades together at the end of the movement.
            6. Hold briefly at the peak contraction, feeling the tension in your mid-back.
            7. Slowly return the handles to the starting position, fully extending your arms in a controlled motion.
        """.trimIndent(),
            idCoverImage = R.drawable.lever_seated_row,
            idImageMuscles = R.drawable.muscles_lever_seated_row
        ),
        Exercise(
            name = "Lateral Raise Machine",
            description = "Isolates the middle part of the deltoid.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.MIDDLE_DELTOID),
            trainedSecondaryMuscles = listOf(
                Muscles.BRACHIALIS,
                Muscles.BRACHIORADIALIS,
                Muscles.DELTOIDS,
                Muscles.LOWER_PECTORAL
            ),
            stepsForDoIt = """
    1. Adjust the seat so the handles are at shoulder height.
    2. Sit with your back straight against the pad.
    3. Grab the handles with your elbows slightly bent.
    4. Raise your arms to the sides until they reach shoulder level.
    5. Slowly lower back to the starting position.
""".trimIndent(),
            nameVideo = "lateral_raise_machine",
            idCoverImage = R.drawable.lateral_raise_machine,
            idImageMuscles = R.drawable.muscles_lateral_raise_machine
        ),
        Exercise(
            name = "Bicep Curl Machine",
            description = "Perfect for isolating the biceps and working the inner part of the arm.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.BICEPS),
            trainedSecondaryMuscles = listOf(Muscles.BRACHIALIS),
            stepsForDoIt = """
    1. Adjust the seat so your arms align with the machine's pivot point.
    2. Sit down and rest your arms on the padded support.
    3. Grab the handles with your palms facing up.
    4. Curl the handles upward by bending your elbows.
    5. Slowly lower the weight back to the starting position.
""".trimIndent(),
            nameVideo = "bicep_curl_machine",
            idCoverImage = R.drawable.bicep_curl_machine,
            idImageMuscles = R.drawable.muscles_bicep_curl_machine
        ),
        Exercise(
            name = "Triceps Extension Machine",
            description = "Works the posterior part of the arm, specifically the triceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.TRICEPS),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
    1. Adjust the seat so your elbows are aligned with the machine's axis.
    2. Sit down and grab the handles with your palms facing down.
    3. Start with your elbows bent at a 90-degree angle.
    4. Extend your arms fully downward.
    5. Slowly return to the starting position.
""".trimIndent(),
            nameVideo = "tricep_extension_machine",
            idCoverImage = R.drawable.triceps_extensions_machine,
            idImageMuscles = R.drawable.muscles_triceps_extension_machine
        ),
        Exercise(
            name = "Preacher Curl Machine",
            description = "For bicep isolation.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.BRACHIALIS),
            trainedSecondaryMuscles = listOf(Muscles.BRACHIORADIALIS, Muscles.BICEPS),
            stepsForDoIt = """
    1. Adjust the seat so your armpits rest just above the bench pad.
    2. Fully extend your arms on the sloped pad.
    3. Grab the bar or handles with a supinated grip.
    4. Curl the weight up toward your shoulders.
    5. Lower it slowly back to the starting position.
""".trimIndent(),
            nameVideo = "preacher_curl_machine",
            idCoverImage = R.drawable.preacher_curl_machine,
            idImageMuscles = R.drawable.muscles_preacher_curl_machine
        ),
        Exercise(
            name = "Ab Crunch Machine",
            description = "Targets the rectus abdominis.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.ABS),
            trainedSecondaryMuscles = listOf(Muscles.OBLIQUES),
            stepsForDoIt = """
    1. Adjust the seat and pads to fit your body properly.
    2. Sit down and place your feet under the footpads.
    3. Grab the handles and keep your back straight.
    4. Contract your abs and curl your torso forward.
    5. Slowly return to the starting position.
""".trimIndent(),
            nameVideo = "ab_crunch_machine",
            idCoverImage = R.drawable.ab_crunches_machines,
            idImageMuscles = R.drawable.muscles_ab_crunch_machine
        ),
        Exercise(
            name = "Oblique Machine",
            description = "Ideal for working the lateral abdominal muscles.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.OBLIQUES),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
    1. Adjust the machine to work one side of the body.
    2. Sit and position your arms and torso correctly according to the machine design.
    3. Twist your torso to the opposite side in a controlled motion.
    4. Hold the contraction for a second.
    5. Slowly return to the starting position.
""".trimIndent(),
            nameVideo = "oblique_machine",
            idCoverImage = R.drawable.obliques_machine,
            idImageMuscles = R.drawable.muscles_oblique_machine
        ),
        Exercise(
            name = "Cable Crunch Machine",
            description = "Provides a more intense focus on the core.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.ABS),
            trainedSecondaryMuscles = listOf(Muscles.OBLIQUES),
            stepsForDoIt = """
    1. Adjust the cable height and select the appropriate weight.
    2. Kneel down and grab the rope handles above your head.
    3. Curl your torso downward, contracting your abdominal muscles.
    4. Hold the contraction briefly.
    5. Return slowly to the starting position.
""".trimIndent(),
            nameVideo = "cable_crunch_machine",
            idCoverImage = R.drawable.cable_crunch_machine,
            idImageMuscles = R.drawable.muscles_cable_crunch_machine
        ),
        Exercise(
            name = "Back Extension Machine",
            description = "Targets the lower back and lumbar area.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.LOWER_BACK),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
    1. Adjust the machine so the pads sit just above your ankles.
    2. Place your thighs on the padded support and cross your arms over your chest.
    3. Lower your upper body slowly from the waist.
    4. Extend your torso back up without hyperextending.
    5. Keep the movement controlled throughout.
""".trimIndent(),
            nameVideo = "back_extension_machine",
            idCoverImage = R.drawable.back_extension_machine,
            idImageMuscles = R.drawable.muscles_back_extension_machine
        ),
        Exercise(
            name = "Trapezius Machine",
            description = "Works the upper trapezius with movements such as shoulder shrugs.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.TRAPEZIUS),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
    1. Adjust the machine to your height.
    2. Grab the handles with your arms at your sides.
    3. Shrug your shoulders upward toward your ears, engaging the trapezius.
    4. Hold the contraction for a second.
    5. Slowly lower your shoulders to the starting position.
""".trimIndent(),
            nameVideo = "trapezius_machine",
            idCoverImage = R.drawable.trapezius_machine,
            idImageMuscles = R.drawable.muscles_trapezius_machine
        )
    )

    val exercisesCalisthenicBasics = listOf(
        // Upper Body (Push)
        Exercise(
            name = "Push-ups",
            description = "Standard push-ups that target the chest, triceps, and shoulders.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.PECTORALS, Muscles.TRICEPS, Muscles.DELTOIDS),
            trainedSecondaryMuscles = listOf(Muscles.LOWER_BACK),
            stepsForDoIt = """
    1. Get into a plank position with your hands under your shoulders.
    2. Lower your body until your chest lightly touches the floor.
    3. Push your body back up to the starting position.
""".trimIndent(),
            nameVideo = "push_ups",
            idCoverImage = R.drawable.push_up,
            idImageMuscles = R.drawable.muscles_push_ups
        ),
        Exercise(
            name = "Dips",
            description = "Dips on parallel bars to target the chest and triceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.PECTORALS),
            trainedSecondaryMuscles = listOf(
                Muscles.BRACHIORADIALIS,
                Muscles.TRICEPS,
                Muscles.FRONT_DELTOID
            ),
            stepsForDoIt = """
    1. Position yourself between the parallel bars with your hands on them.
    2. Lower your body until your elbows form a 90-degree angle.
    3. Push yourself back up to full arm extension.
""".trimIndent(),
            nameVideo = "dips",
            idCoverImage = R.drawable.dips,
            idImageMuscles = R.drawable.muscles_dips
        ),
        Exercise(
            name = "Closed-Grip Push-ups",
            description = "Push-ups performed with hands closer together to target the triceps more intensively.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.TRICEPS),
            trainedSecondaryMuscles = listOf(
                Muscles.FRONT_DELTOID,
                Muscles.FRONT_DELTOID,
                Muscles.PECTORALS
            ),
            stepsForDoIt = """
    1. Get into a standard push-up position but place your hands close together under your chest, forming a triangle or diamond shape with your thumbs and index fingers.
    2. Lower your body by bending your elbows, keeping them close to your torso.
    3. Push back up to the starting position, focusing on engaging your triceps.
    4. Repeat for the desired number of repetitions.
""".trimIndent(),
            nameVideo = "closed_grip_push_ups",
            idCoverImage = R.drawable.closed_grip_push_ups,
            idImageMuscles = R.drawable.muscles_closed_grip_push_ups
        ),

        // Upper Body (Pull)
        Exercise(
            name = "Pull-ups",
            description = "Basic exercise to target the back, shoulders, and biceps using a bar.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(
                Muscles.LATISSIMUS_DORSI,
                Muscles.TERES_MAJOR,
                Muscles.TERES_MINOR,
                Muscles.TRAPEZIUS,
                Muscles.BICEPS
            ),
            trainedSecondaryMuscles = listOf(
                Muscles.BRACHIALIS,
                Muscles.BRACHIORADIALIS,
                Muscles.DELTOIDS,
                Muscles.INFRASPINATUS,
                Muscles.PECTORALS
            ),
            stepsForDoIt = """
    1. Grip the bar with your hands at shoulder width.
    2. Pull your body upwards until your chin is above the bar.
    3. Lower yourself slowly back down to the starting position.
""".trimIndent(),
            nameVideo = "pull_ups",
            idCoverImage = R.drawable.pull_ups,
            idImageMuscles = R.drawable.muscles_pull_ups
        ),
        Exercise(
            name = "Chin-ups",
            description = "Pull-ups with a supine grip (palms facing you), targeting the biceps more.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.BICEPS, Muscles.LATISSIMUS_DORSI),
            trainedSecondaryMuscles = listOf(
                Muscles.TRAPEZIUS,
                Muscles.BRACHIALIS,
                Muscles.BRACHIORADIALIS,
                Muscles.DELTOIDS,
                Muscles.PECTORALS,
                Muscles.TERES_MAJOR
            ),
            stepsForDoIt = """
    1. Grip the bar with your palms facing you.
    2. Pull your body up until your chin is above the bar.
    3. Slowly lower yourself.
""".trimIndent(),
            nameVideo = "chin_ups",
            idCoverImage = R.drawable.chin_ups,
            idImageMuscles = R.drawable.muscles_chin_ups
        ),
        Exercise(
            name = "Negative Pull-ups",
            description = "If you can't do a full pull-up yet, start at the top and lower yourself slowly.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(
                Muscles.LATISSIMUS_DORSI,
                Muscles.TERES_MAJOR,
                Muscles.TERES_MINOR,
                Muscles.TRAPEZIUS
            ),
            trainedSecondaryMuscles = listOf(
                Muscles.BRACHIALIS,
                Muscles.BRACHIORADIALIS,
                Muscles.DELTOIDS,
                Muscles.INFRASPINATUS,
                Muscles.PECTORALS
            ),
            stepsForDoIt = """
    1. Jump or climb to the top position of the pull-up bar.
    2. Lower yourself slowly, keeping your body under control.
    3. Repeat the movement as many times as possible.
""".trimIndent(),
            nameVideo = "negative_pull_ups",
            idCoverImage = R.drawable.negative_pull_ups,
            idImageMuscles = R.drawable.muscles_negative_pull_ups
        ),

        // Legs
        Exercise(
            name = "Squats",
            description = "Bodyweight squats to work the legs and glutes.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.QUADRICEPS, Muscles.GLUTEUS),
            trainedSecondaryMuscles = listOf(Muscles.CALVES, Muscles.ABS, Muscles.LOWER_BACK),
            stepsForDoIt = """
    1. Stand with your feet shoulder-width apart.
    2. Lower your hips as if sitting in a chair, keeping your back straight.
    3. Lower until your thighs are parallel to the ground, then push back up.
""".trimIndent(),
            nameVideo = "squats",
            idCoverImage = R.drawable.squats,
            idImageMuscles = R.drawable.muscles_squats
        ),
        Exercise(
            name = "Jump Squats",
            description = "Explosive squats with a jump to work strength and power.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.QUADRICEPS, Muscles.GLUTEUS),
            trainedSecondaryMuscles = emptyList(),
            stepsForDoIt = """
    1. Perform a regular squat.
    2. As you rise, explosively jump upwards.
    3. Land softly and repeat.
""".trimIndent(),
            nameVideo = "jump_squats",
            idCoverImage = R.drawable.jump_squats,
            idImageMuscles = R.drawable.muscles_jump_squats
        ),
        Exercise(
            name = "Lunges",
            description = "Lunges to target quads, glutes, and stability.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.QUADRICEPS, Muscles.GLUTEUS),
            trainedSecondaryMuscles = emptyList(),
            stepsForDoIt = """
    1. Step forward with one leg.
    2. Lower your back knee toward the ground.
    3. Return to the starting position and repeat with the other leg.
""".trimIndent(),
            nameVideo = "lunges",
            idCoverImage = R.drawable.lunges,
            idImageMuscles = R.drawable.muscles_lunges
        ),
        Exercise(
            name = "Step-ups",
            description = "Step onto an elevated platform (such as a bench or park structure) alternating legs.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.HAMSTRINGS),
            trainedSecondaryMuscles = emptyList(),
            stepsForDoIt = """
    1. Position yourself in front of an elevated platform.
    2. Step up with one leg, pushing off with the leg that steps up.
    3. Lower yourself with control and repeat with the other leg.
""".trimIndent(),
            nameVideo = "step_ups",
            idCoverImage = R.drawable.step_ups,
            idImageMuscles = R.drawable.muscles_step_ups
        ),
        Exercise(
            name = "Calf Raises",
            description = "Exercise to target the calves, done on an elevated structure or flat ground.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.CALVES),
            trainedSecondaryMuscles = listOf(Muscles.ABS, Muscles.HAMSTRINGS),
            stepsForDoIt = """
    1. Stand with your feet on an elevated surface (e.g., a step).
    2. Raise your heels as high as possible, contracting your calves.
    3. Lower slowly until your heels are below your toes.
""".trimIndent(),
            nameVideo = "calf_raises",
            idCoverImage = R.drawable.calf_raises,
            idImageMuscles = R.drawable.muscles_calf_raises
        ),

        // Core (Abs)
        Exercise(
            name = "Plank",
            description = "An isometric exercise to strengthen the core.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.ABS),
            trainedSecondaryMuscles = listOf(
                Muscles.FRONT_DELTOID,
                Muscles.GLUTEUS,
                Muscles.OBLIQUES,
                Muscles.SARTORIUS,
                Muscles.TENSOR_FASCIAE_FEMORIS
            ),
            stepsForDoIt = """
    1. Get into a plank position, resting on your forearms and toes.
    2. Keep your body straight and engage your core.
    3. Hold the position for 30-60 seconds.
""".trimIndent(),
            nameVideo = "plank",
            idCoverImage = R.drawable.plank,
            idImageMuscles = R.drawable.muscles_plank
        ),
        Exercise(
            name = "Crunches",
            description = "Classic exercise to target the abdominal muscles.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.UPPER_ABS, Muscles.MIDDLE_ABS),
            trainedSecondaryMuscles = listOf(Muscles.OBLIQUES),
            stepsForDoIt = """
    1. Lie on your back with your knees bent.
    2. Perform a small lift of your torso, contracting your abs.
    3. Lower yourself back slowly.
""".trimIndent(),
            nameVideo = "crunches",
            idCoverImage = R.drawable.crunches,
            idImageMuscles = R.drawable.muscles_crunches
        ),
        Exercise(
            name = "Leg Raises",
            description = "Exercise for the lower abs, done while hanging from a bar.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.LOWER_ABS),
            trainedSecondaryMuscles = listOf(
                Muscles.GLUTEUS,
                Muscles.ILIOPSOAS,
                Muscles.QUADRICEPS,
                Muscles.SARTORIUS,
                Muscles.TENSOR_FASCIAE_FEMORIS
            ),
            stepsForDoIt = """
    1. Hang from a bar with your legs straight.
    2. Raise your legs towards your chest while keeping them straight.
    3. Slowly lower them back down.
""".trimIndent(),
            nameVideo = "leg_raises",
            idCoverImage = R.drawable.leg_raises,
            idImageMuscles = R.drawable.muscles_leg_raises
        ),
        Exercise(
            name = "Russian Twists",
            description = "Exercise for the obliques, performed seated with a twisting motion.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.OBLIQUES),
            trainedSecondaryMuscles = emptyList(),
            stepsForDoIt = """
    1. Sit on the ground with your legs slightly bent.
    2. Lean slightly back and rotate your torso side to side, touching the ground with your hands.
""".trimIndent(),
            nameVideo = "russian_twists",
            idCoverImage = R.drawable.russian_twists,
            idImageMuscles = R.drawable.muscles_russian_twists
        ),
        Exercise(
            name = "Mountain Climbers",
            description = "Cardio exercise that also works the abs and core.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.CARDIO,
            trainedPrimaryMuscles = listOf(Muscles.ABS),
            trainedSecondaryMuscles = listOf(
                Muscles.ILIOPSOAS,
                Muscles.QUADRICEPS,
                Muscles.TENSOR_FASCIAE_FEMORIS
            ),
            stepsForDoIt = """
    1. Start in a plank position.
    2. Drive one knee towards your chest, then quickly alternate legs.
""".trimIndent(),
            nameVideo = "mountain_climbers",
            idCoverImage = R.drawable.mountain_climbers,
            idImageMuscles = R.drawable.muscles_mountain_climbers
        ),
        Exercise(
            name = "Toe Touches",
            description = "Exercise for the abs, performed by touching your toes with your hands.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.ABS),
            trainedSecondaryMuscles = listOf(Muscles.OBLIQUES),
            stepsForDoIt = """
    1. Lie on your back with your legs extended towards the ceiling.
    2. Lift your torso and touch your toes with your hands.
""".trimIndent(),
            nameVideo = "toe_touches",
            idCoverImage = R.drawable.toe_touches,
            idImageMuscles = R.drawable.muscles_toe_touches
        )
    )


    val weightlifting = listOf(
        // Chest Exercises
        Exercise(
            name = "Bench Press",
            description = "One of the fundamental exercises to work the chest.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.PECTORALS),
            trainedSecondaryMuscles = listOf(Muscles.FRONT_DELTOID, Muscles.TRICEPS),
            stepsForDoIt = "1. Lie on a flat bench. \n2. Grip the barbell at shoulder width. \n3. Lower it to the chest. \n4. Push it back up.",
            nameVideo = "bench_press",
            idCoverImage = R.drawable.bench_press,
            idImageMuscles = R.drawable.muscles_bench_press
        ),
        Exercise(
            name = "Incline Bench Press with Barbell or Dumbbells",
            description = "Focuses more on the upper part of the chest.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.UPPER_PECTORALS),
            trainedSecondaryMuscles = listOf(Muscles.FRONT_DELTOID, Muscles.TRICEPS),
            stepsForDoIt = "1. Lie on an incline bench. \n2. Grip the barbell or dumbbells. \n3. Lower them to chest level. \n4. Push them back up.",
            nameVideo = "incline_bench_press_with_barbell_or_dumbbells",
            idCoverImage = R.drawable.incline_bench_press_with_barbell_or_dumbbells,
            idImageMuscles = R.drawable.muscles_incline_bench_press_with_barbell_or_dumbbells
        ),
        Exercise(
            name = "Weighted_Dips",
            description = "With the torso leaning forward, it targets the chest more.",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.PECTORALS),
            trainedSecondaryMuscles = listOf(Muscles.FRONT_DELTOID, Muscles.TRICEPS),
            stepsForDoIt = "1. Hold onto the parallel bars. \n2. Lower your body controlled until your elbows are at 90 degrees. \n3. Push back up.",
            nameVideo = "weighted_dips",
            idCoverImage = R.drawable.weighted_dips,
            idImageMuscles = R.drawable.muscles_weighted_dips
        ),
        Exercise(
            name = "Flat Bench Dumbbell Flyes",
            description = "Targets the outer chest.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.PECTORALS),
            trainedSecondaryMuscles = listOf(Muscles.FRONT_DELTOID, Muscles.BICEPS),
            stepsForDoIt = "1. Lie on a flat bench. \n2. Hold the dumbbells with slightly bent elbows. \n3. Open your arms to the sides. \n4. Bring them together.",
            nameVideo = "flat_bench_dumbbell_flyes",
            idCoverImage = R.drawable.flat_bench_dumbbell_flyes,
            idImageMuscles = R.drawable.muscles_flat_bench_dumbbell_flyes
        ),
        Exercise(
            name = "Incline Dumbbell Flyes",
            description = "Focuses on the upper chest.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.UPPER_PECTORALS),
            trainedSecondaryMuscles = listOf(Muscles.FRONT_DELTOID, Muscles.BICEPS),
            stepsForDoIt = "1. Lie on an incline bench. \n2. Perform the flyes with dumbbells. \n3. Focus on the upper chest.",
            nameVideo = "incline_dumbbell_flyes",
            idCoverImage = R.drawable.incline_dumbbell_flyes,
            idImageMuscles = R.drawable.muscles_incline_dumbbell_flyes
        ),

        // Back Exercises
        Exercise(
            name = "Deadlift",
            description = "Fundamental for working the lower back, glutes, and legs.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.LOWER_BACK, Muscles.GLUTEUS, Muscles.HAMSTRINGS),
            trainedSecondaryMuscles = listOf(Muscles.QUADRICEPS, Muscles.SOLEUS),
            stepsForDoIt = "1. Place your feet at hip width. \n2. Grip the barbell with your hands by your legs. \n3. Lower the bar while keeping your back straight. \n4. Lift it back up.",
            nameVideo = "deadlift",
            idCoverImage = R.drawable.deadlift,
            idImageMuscles = R.drawable.muscles_deadlift
        ),
        Exercise(
            name = "Weighted Pull-Ups",
            description = "A classic exercise mainly working the back, though it also involves other muscles, using a weighted.",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.LATISSIMUS_DORSI, Muscles.BICEPS),
            trainedSecondaryMuscles = listOf(Muscles.BRACHIORADIALIS, Muscles.DELTOIDS),
            stepsForDoIt = "1. Grab the bar with your hands at shoulder width. \n2. Pull your body up until your chin passes the bar. \n3. Lower back down.",
            nameVideo = "weighted_pull_ups",
            idCoverImage = R.drawable.weighted_pull_ups,
            idImageMuscles = R.drawable.muscles_weighted_pull_ups
        ),
        Exercise(
            name = "Barbell Row",
            description = "Targets the mid-back.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(
                Muscles.LATISSIMUS_DORSI,
                Muscles.TRAPEZIUS,
                Muscles.RHOMBOIDS
            ),
            trainedSecondaryMuscles = listOf(Muscles.BRACHIALIS, Muscles.BRACHIORADIALIS),
            stepsForDoIt = "1. Bend forward, keeping your back straight. \n2. Grip the barbell at shoulder width. \n3. Row it towards your abdomen.",
            nameVideo = "barbell_row",
            idCoverImage = R.drawable.barbell_row,
            idImageMuscles = R.drawable.muscles_barbell_row
        ),
        Exercise(
            name = "One-Arm Dumbbell Row",
            description = "Excellent for isolating back muscles unilaterally.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.LATISSIMUS_DORSI, Muscles.TRAPEZIUS),
            trainedSecondaryMuscles = listOf(Muscles.BICEPS),
            stepsForDoIt = "1. Place one knee and hand on a bench. \n2. Grip a dumbbell with the other hand. \n3. Row the elbow back, focusing on the contraction of the back.",
            nameVideo = "one_arm_dumbbell_row",
            idCoverImage = R.drawable.one_arm_dumbbell_row,
            idImageMuscles = R.drawable.muscles_one_arm_dumbbell_row
        ),
        Exercise(
            name = "Dumbbell Pullover",
            description = "Isolates the upper back area.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.LATISSIMUS_DORSI, Muscles.PECTORALS),
            trainedSecondaryMuscles = listOf(Muscles.TRICEPS),
            stepsForDoIt = "1. Lie on a bench. \n2. Hold the dumbbell with both hands. \n3. Lower it behind your head. \n4. Bring it back up.",
            nameVideo = "dumbbell_pullover",
            idCoverImage = R.drawable.dumbbell_pullover,
            idImageMuscles = R.drawable.muscles_dumbbell_pullover
        ),
        Exercise(
            name = "Lat Pulldown Machine",
            description = "Similar to pull-ups but using a cable machine.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.LATISSIMUS_DORSI),
            trainedSecondaryMuscles = listOf(Muscles.BICEPS),
            stepsForDoIt = "1. Grip the lat pulldown bar with a wide grip. \n2. Pull the bar down to chest level.",
            nameVideo = "lat_pulldown_machine",
            idCoverImage = R.drawable.lat_pulldown_machine,
            idImageMuscles = R.drawable.muscles_lat_pulldown_machine
        ),

        // Leg Exercises
        Exercise(
            name = "Barbell Squats",
            description = "The king of leg exercises.",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.QUADRICEPS, Muscles.GLUTEUS, Muscles.HAMSTRINGS),
            trainedSecondaryMuscles = listOf(Muscles.CALVES),
            stepsForDoIt = "1. Place the barbell on your traps. \n2. Squat down keeping your back straight until your thighs are parallel to the floor. \n3. Push back up.",
            nameVideo = "barbell_squats",
            idCoverImage = R.drawable.barbell_squats,
            idImageMuscles = R.drawable.muscles_barbell_squats
        ),
        Exercise(
            name = "Leg Press Machine",
            description = "Similar to squats but with a different angle and load.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.QUADRICEPS, Muscles.GLUTEUS),
            trainedSecondaryMuscles = listOf(Muscles.HAMSTRINGS),
            stepsForDoIt = "1. Sit in the leg press machine. \n2. Place your feet on the platform. \n3. Push upward extending your legs.",
            nameVideo = "leg_press_machine",
            idCoverImage = R.drawable.leg_press_machine,
            idImageMuscles = R.drawable.muscles_leg_press_machine
        ),
        Exercise(
            name = "Romanian Deadlift",
            description = "Targets the hamstrings and glutes.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.HAMSTRINGS, Muscles.GLUTEUS),
            trainedSecondaryMuscles = emptyList(),
            stepsForDoIt = "1. With the barbell in front of your legs, bend forward while keeping your legs almost straight. \n2. Lower the barbell below your knees. \n3. Lift it back up.",
            nameVideo = "romanian_deadlift",
            idCoverImage = R.drawable.romanian_deadlift,
            idImageMuscles = R.drawable.muscles_romanian_deadlift
        ),
        Exercise(
            name = "Dumbbell Lunges",
            description = "Isolates each leg.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.QUADRICEPS, Muscles.GLUTEUS, Muscles.HAMSTRINGS),
            trainedSecondaryMuscles = listOf(Muscles.CALVES),
            stepsForDoIt = "1. Take a long step forward. \n2. Lower your front leg to 90 degrees. \n3. Push back up.",
            nameVideo = "dumbbell_lunges",
            idCoverImage = R.drawable.dumbbell_lunges,
            idImageMuscles = R.drawable.muscles_dumbbell_lunges
        ),
        Exercise(
            name = "Standing or Seated Calf Raises",
            description = "Targets the calves.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.CALVES),
            trainedSecondaryMuscles = emptyList(),
            stepsForDoIt = "1. Stand on a platform or bench. \n2. Raise your heels as high as possible. \n3. Lower back down in a controlled manner.",
            nameVideo = "standing_or_seated_calf_raises",
            idCoverImage = R.drawable.standing_or_seated_calf_raises,
            idImageMuscles = R.drawable.muscles_standing_or_seated_calf_raises
        ),
        Exercise(
            name = "Military Press with Barbell or Dumbbells",
            nameVideo = "military_press_with_barbell_or_dumbbells",
            description = "To work the deltoids.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.FRONT_DELTOID),
            trainedSecondaryMuscles = listOf(Muscles.TRICEPS, Muscles.UPPER_PECTORALS, Muscles.MIDDLE_DELTOID),
            stepsForDoIt = """
    1. Hold the barbell or dumbbells at shoulder height.
    2. Press the dumbbells or barbell overhead until your arms are fully extended.
    3. Lower back down in a controlled manner to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.military_press_with_barbell_or_dumbbells,
            idImageMuscles = R.drawable.muscles_military_press_with_barbell_or_dumbbells
        ),
        Exercise(
            name = "Lateral Raises with Dumbbells",
            nameVideo = "lateral_raises_with_dumbbells",
            description = "For the middle deltoid.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.MIDDLE_DELTOID),
            trainedSecondaryMuscles = listOf(Muscles.FRONT_DELTOID),
            stepsForDoIt = """
    1. Hold a dumbbell in each hand with your arms slightly bent.
    2. Raise the dumbbells to the sides until your arms are parallel to the ground.
    3. Lower them slowly back to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.lateral_raises_with_dumbbells,
            idImageMuscles = R.drawable.muscles_lateral_raises_with_dumbbells
        ),
        Exercise(
            name = "Front Raises with Dumbbells or Barbell",
            nameVideo = "front_raises_with_dumbbells_or_barbell",
            description = "For the anterior deltoid.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.FRONT_DELTOID),
            trainedSecondaryMuscles = listOf(Muscles.UPPER_PECTORALS, Muscles.MIDDLE_DELTOID),
            stepsForDoIt = """
    1. Hold a dumbbell in each hand or the barbell with an overhand grip.
    2. Raise the dumbbells or barbell to the front until your arms are parallel to the ground.
    3. Lower back down slowly to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.front_raises_with_dumbbells_or_barbell,
            idImageMuscles = R.drawable.muscles_front_raises_with_dumbbells_or_barbell
        ),
        Exercise(
            name = "Reverse Fly with Dumbbells",
            nameVideo = "reverse_fly_with_dumbbells",
            description = "To target the rear deltoids.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.POSTERIOR_DELTOID, Muscles.MIDDLE_DELTOID),
            trainedSecondaryMuscles = listOf(Muscles.TRAPEZIUS, Muscles.TERES_MINOR),
            stepsForDoIt = """
    1. Bend forward at the waist while keeping your back straight.
    2. Hold a dumbbell in each hand with your arms extended down.
    3. Raise the dumbbells out to the sides, keeping your elbows slightly bent.
    4. Lower back down in a controlled manner to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.reverse_fly_with_dumbbells,
            idImageMuscles = R.drawable.muscles_reverse_fly_with_dumbbells
        ),
        Exercise(
            name = "Barbell Curl",
            nameVideo = "barbell_curl",
            description = "A basic exercise for the biceps.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.BICEPS),
            trainedSecondaryMuscles = listOf(Muscles.BRACHIALIS, Muscles.BRACHIORADIALIS),
            stepsForDoIt = """
    1. Hold the barbell with an underhand grip (palms facing up).
    2. Keep the bar close to your body and curl it toward your shoulders.
    3. Lower it back down in a controlled manner to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.barbell_curl,
            idImageMuscles = R.drawable.muscles_barbell_curl
        ),
        Exercise(
            name = "Dumbbell Curl",
            nameVideo = "dumbbell_curl",
            description = "Can be done alternately or simultaneously.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.BICEPS),
            trainedSecondaryMuscles = listOf(Muscles.BRACHIALIS, Muscles.BRACHIORADIALIS),
            stepsForDoIt = """
    1. Hold a dumbbell in each hand with your elbows close to your body.
    2. Curl the dumbbells toward your shoulders.
    3. Lower them slowly back to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.dumbbell_curl,
            idImageMuscles = R.drawable.muscles_dumbbell_curl
        ),
        Exercise(
            name = "Hammer Curl with Dumbbells",
            nameVideo = "hammer_curl_with_dumbbells",
            description = "Also works the forearms and outer biceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.BRACHIORADIALIS),
            trainedSecondaryMuscles = listOf(Muscles.BICEPS),
            stepsForDoIt = """
    1. Hold the dumbbells with palms facing each other.
    2. Curl the dumbbells toward your shoulders, keeping your elbows close to your body.
    3. Lower them back down in a controlled manner to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.hammer_curl_with_dumbbells,
            idImageMuscles = R.drawable.muscles_hammer_curl_with_dumbbells
        ),
        Exercise(
            name = "Concentration Curl",
            nameVideo = "concentration_curl",
            description = "To isolate the biceps and maximize contraction.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.BRACHIALIS),
            trainedSecondaryMuscles = listOf(Muscles.BICEPS, Muscles.BRACHIORADIALIS),
            stepsForDoIt = """
    1. Sit on a bench and rest your elbow on your thigh.
    2. Curl the dumbbell toward your shoulder, focusing the effort on the biceps.
    3. Lower back down slowly to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.concentration_curl,
            idImageMuscles = R.drawable.muscles_concentration_curl
        ),
        Exercise(
            name = "Preacher Curl",
            nameVideo = "preacher_curl",
            description = "Completely isolates the biceps, eliminating momentum.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.BRACHIALIS),
            trainedSecondaryMuscles = listOf(Muscles.BICEPS, Muscles.BRACHIORADIALIS),
            stepsForDoIt = """
    1. Rest your arms on the preacher curl pad.
    2. Hold the bar or dumbbells with palms facing up.
    3. Curl the weight toward your shoulders.
    4. Lower it back down in a controlled manner to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.preacher_curl,
            idImageMuscles = R.drawable.muscles_preacher_curl
        ),
        Exercise(
            name = "Bench Dips",
            nameVideo = "bench_dips",
            description = "A great exercise for the triceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.TRICEPS),
            trainedSecondaryMuscles = listOf(
                Muscles.FRONT_DELTOID,
                Muscles.LATISSIMUS_DORSI,
                Muscles.UPPER_PECTORALS
            ),
            stepsForDoIt = """
    1. Place your hands on the benches and your heels on the ground.
    2. Lower your torso in a controlled manner by bending your elbows to about 90 degrees.
    3. Push back up to the starting position, fully extending your arms.
""".trimIndent(),
            idCoverImage = R.drawable.bench_dips,
            idImageMuscles = R.drawable.muscles_bench_dips
        ),
        Exercise(
            name = "Overhead Triceps Extension with Dumbbell",
            nameVideo = "overhead_triceps_extension_with_dumbbell",
            description = "To target the long head of the triceps.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.TRICEPS),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
    1. Hold a dumbbell with both hands and extend your arms overhead.
    2. Lower the dumbbell behind your head in a controlled manner.
    3. Extend your elbows to press the dumbbell back overhead.
""".trimIndent(),
            idCoverImage = R.drawable.overhead_triceps_extension_with_dumbbell,
            idImageMuscles = R.drawable.muscles_overhead_triceps_extension_with_dumbbell
        ),
        Exercise(
            name = "French Press with Barbell or Dumbbells",
            nameVideo = "french_press_with_barbell_or_dumbbells",
            description = "A classic triceps exercise.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.TRICEPS),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
    1. Hold the barbell or dumbbells with an overhand grip, keeping your hands close together.
    2. Lower the weight toward your forehead in a controlled manner.
    3. Extend your elbows to press the weight back up.
""".trimIndent(),
            idCoverImage = R.drawable.french_press_with_barbell_or_dumbbells,
            idImageMuscles = R.drawable.muscles_french_press_with_barbell_or_dumbbells
        ),
        Exercise(
            name = "Triceps Extension Machine",
            nameVideo = "triceps_extension_machine",
            description = "To isolate the triceps more effectively.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.MACHINES,
            trainedPrimaryMuscles = listOf(Muscles.TRICEPS),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
    1. Grip the rope on the high pulley with palms facing down.
    2. Push the rope downward by extending your elbows.
    3. Return to the starting position in a controlled manner.
""".trimIndent(),
            idCoverImage = R.drawable.triceps_extensions_machine,
            idImageMuscles = R.drawable.muscles_triceps_extension_machine
        ),
        Exercise(
            name = "Triceps Kickback with Dumbbell",
            nameVideo = "triceps_kickback_with_dumbbell",
            description = "Isolating the triceps at the back of the arm.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.TRICEPS),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
    1. Lean forward while keeping your back straight.
    2. Hold a dumbbell with one hand, with your elbow bent.
    3. Extend your arm backward until it is fully straight.
    4. Lower the dumbbell back to the starting position slowly.
""".trimIndent(),
            idCoverImage = R.drawable.triceps_kickback_with_dumbbell,
            idImageMuscles = R.drawable.muscles_triceps_kickback_with_dumbbell
        ),
        Exercise(
            name = "Weighted Crunch",
            nameVideo = "weighted_crunch",
            description = "You can add a barbell or dumbbell to your chest.",
            exerciseLevel = ExerciseLevel.BEGINNER,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.MIDDLE_ABS, Muscles.UPPER_ABS),
            trainedSecondaryMuscles = listOf(Muscles.OBLIQUES, Muscles.LOWER_ABS),
            stepsForDoIt = """
    1. Lie on a bench and hold the weight on your chest.
    2. Perform a crunch by lifting your torso towards your knees.
    3. Lower slowly back to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.weighted_crunch,
            idImageMuscles = R.drawable.muscles_weighted_crunch
        ),
        Exercise(
            name = "Leg Raises with Weight",
            nameVideo = "leg_raises_with_weight",
            description = "To target the lower abs.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.LOWER_ABS, Muscles.ILIOPSOAS),
            trainedSecondaryMuscles = listOf(Muscles.QUADRICEPS, Muscles.MIDDLE_ABS),
            stepsForDoIt = """
    1. Lie on a bench or on the floor, holding a weight between your feet.
    2. Raise your legs until they form a 90-degree angle.
    3. Slowly lower your legs back to the starting position.
""".trimIndent(),
            idCoverImage = R.drawable.leg_raises_with_weight,
            idImageMuscles = R.drawable.muscles_leg_raises_with_weight
        ),
        Exercise(
            name = "Russian Twist with Dumbbell or Plate",
            nameVideo = "russian_twist_with_dumbbell_or_plate",
            description = "To target the obliques.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.WEIGHTLIFTING,
            trainedPrimaryMuscles = listOf(Muscles.OBLIQUES),
            trainedSecondaryMuscles = listOf(Muscles.ILIOPSOAS, Muscles.QUADRICEPS),
            stepsForDoIt = """
    1. Sit with your legs elevated and your torso upright.
    2. Twist your torso from side to side while holding the weight with both hands.
    3. Maintain control throughout the movement, avoiding jerky motions.
""".trimIndent(),
            idCoverImage = R.drawable.russian_twist_with_dumbbell_or_plate,
            idImageMuscles = R.drawable.muscles_russian_twist_with_dumbbell_or_plate
        ),
        Exercise(
            name = "Weighted Plank",
            nameVideo = "weighted_plank",
            description = "To strengthen the core in general.",
            exerciseLevel = ExerciseLevel.INTERMEDIATE,
            type = TypeExercise.BASIC,
            trainedPrimaryMuscles = listOf(Muscles.ABS),
            trainedSecondaryMuscles = listOf(
                Muscles.FRONT_DELTOID,
                Muscles.GLUTEUS,
                Muscles.OBLIQUES,
                Muscles.SARTORIUS,
                Muscles.TENSOR_FASCIAE_FEMORIS
            ),
            stepsForDoIt = """
    1. Get into a plank position with your elbows under your shoulders.
    2. Place a weight on your back if desired for added intensity.
    3. Hold the position for as long as you can while maintaining proper form.
""".trimIndent(),
            idCoverImage = R.drawable.weighted_plank,
            idImageMuscles = R.drawable.muscles_weighted_plank
        )
    )

    val tensRoutine = listOf(
        Exercise(
            name = "Combos Tension",
            nameVideo = "combos_tension",
            description = "Tens exercise combos without getting off the parallel bar",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.TENS,
            trainedPrimaryMuscles = emptyList<Muscles>(),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
        1. Get into a parallel bar or pull-up bar.
        2. Perform an exercise without getting down from the bar.
        3. Then, continue with forward exercises—ending with the one you can’t complete.
    """.trimIndent(),
            typeTensExercise = TypeTensExercise.ALL,
            idCoverImage = 0,//R.drawable.combos_tension,
            idImageMuscles = 0
        ),
        Exercise(
            name = "Press Plank",
            nameVideo = "press_plank",
            description = "Do press plank using a resistance band",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.TENS,
            trainedPrimaryMuscles = emptyList<Muscles>(),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
        1. Get into the plank position.
        2. Raise your legs to transition into a handstand.
        3. Then, lower your legs back down to return to the plank position.
    """.trimIndent(),
            typeTensExercise = TypeTensExercise.PLANK,
            idCoverImage = 0,//R.drawable.press_plank,
            idImageMuscles = 0
        ),
        Exercise(
            name = "Push-up Plank",
            nameVideo = "push_up_plank",
            description = "Do press plank using a resistance band",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.TENS,
            trainedPrimaryMuscles = emptyList<Muscles>(),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
        1. Get into the plank position.
        2. Lower your body while maintaining scapular protraction.
        3. Then, return to the plank position.
    """.trimIndent(),
            typeTensExercise = TypeTensExercise.PLANK,
            idCoverImage = 0, //R.drawable.push_up_plank,
            idImageMuscles = 0
        ),
        Exercise(
            name = "Maltese Plank push-up",
            nameVideo = "maltese_plank_push_up",
            description = "Perform a plank with a wider arm position, incorporating scapular retraction and protraction.",
            exerciseLevel = ExerciseLevel.ELITE,
            type = TypeExercise.TENS,
            trainedPrimaryMuscles = emptyList<Muscles>(),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
        1. Leave about 4 foot-lengths of space between the parallel bars.
        2. Get into the plank position.
        3. Lower your body while performing scapular retraction.
        4. Then, return to scapular protraction.
    """.trimIndent(),
            typeTensExercise = TypeTensExercise.PLANK,
            idCoverImage = 0,//R.drawable.maltese_plank_push_up,
            idImageMuscles = 0
        ),
        Exercise(
            name = "Handstand push-up",
            nameVideo = "handstand_push_up",
            description = "Perform a handstand and do push ups.",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.TENS,
            trainedPrimaryMuscles = emptyList<Muscles>(),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
        1. Get into a handstand (lean against a wall if needed).
        2. Lower your body forward until it touches the floor.
        3. Raise your body forward with your arms outstretched.
    """.trimIndent(),
            typeTensExercise = TypeTensExercise.PLANK,
            idCoverImage = 0,//R.drawable.handstand_push_up,
            idImageMuscles = 0
        ),
        Exercise(
            name = "Press front-lever",
            nameVideo = "press_front_lever",
            description = "Front lever press: lift legs to touch the bar, then return to front lever. Use a resistance band",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.TENS,
            trainedPrimaryMuscles = emptyList<Muscles>(),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
        1. Get into the front-lever position.
        2. Raise your legs while maintaining scapular retraction.
        3. Then, lower the legs down to the front lever position.
    """.trimIndent(),
            typeTensExercise = TypeTensExercise.FRONT_LEVEL,
            idCoverImage = 0,//R.drawable.press_front_lever,
            idImageMuscles = 0
        ),
        Exercise(
            name = "Pull-up front-lever",
            nameVideo = "pull_up_front_lever",
            description = "Front lever pull-up: pull up to touch the bar, then return to front lever. Use a resistance band",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.TENS,
            trainedPrimaryMuscles = emptyList<Muscles>(),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
        2. Get into the front-lever position.
        3. Raise your legs while performing scapular retraction.
        4. Then, lower the legs down to the front lever position.
    """.trimIndent(),
            typeTensExercise = TypeTensExercise.FRONT_LEVEL,
            idCoverImage = 0,//R.drawable.pull_up_front_lever,
            idImageMuscles = 0
        ),
        Exercise(
            name = "Maltese front lever press",
            nameVideo = "maltese_front_lever_press",
            description = "Perform front lever press, with a wider arm position. Use a resistance band",
            exerciseLevel = ExerciseLevel.ELITE,
            type = TypeExercise.TENS,
            trainedPrimaryMuscles = emptyList<Muscles>(),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
        1. Leave about 4 foot-lengths of space between the hands in the parallel bars.
        2. Get into the front lever position.
        3. Raise your legs while performing scapular retraction.
        4. Then, lower the legs down to front lever position.
    """.trimIndent(),
            typeTensExercise = TypeTensExercise.FRONT_LEVEL,
            idCoverImage = 0,//R.drawable.maltese_front_lever_press,
            idImageMuscles = 0
        ),
        Exercise(
            name = "Pull-up holding up",
            nameVideo = "pull_up_holding_up",
            description = "Perform a pull-ups, but holding up. Use a resistance band if necessary",
            exerciseLevel = ExerciseLevel.ADVANCED,
            type = TypeExercise.TENS,
            trainedPrimaryMuscles = emptyList<Muscles>(),
            trainedSecondaryMuscles = emptyList<Muscles>(),
            stepsForDoIt = """
        2. Get into the pull-up position.
        3. Upper your body with the chin over the bar.
        4. Holding up 3s and lower body down to pull-up position.
    """.trimIndent(),
            typeTensExercise = TypeTensExercise.FRONT_LEVEL,
            idCoverImage = 0,//R.drawable.pull_up_holding_up,
            idImageMuscles = 0
        ),
    )
    //TensExercise("Pull-up hold: pull until your chin is above the bar, hold for 3 seconds, then lower. Do it without a band if possible", TypeTensExercise.FRONT_LEVEL))
}