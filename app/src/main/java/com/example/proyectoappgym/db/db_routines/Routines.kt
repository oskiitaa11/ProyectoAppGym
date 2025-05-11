package com.example.proyectoappgym.db.db_routines

import com.example.proyectoappgym.entity.DayOfWeek
import com.example.proyectoappgym.entity.Exercise
import com.example.proyectoappgym.entity.TensExercise
import com.example.proyectoappgym.entity.TypeExercise
import com.example.proyectoappgym.entity.TypeTensExercise

object Routines {
    /*val weightliftingRoutines = mapOf(
        DayOfWeek.MONDAY to listOf(
            Exercise("Flat Barbell Bench Press", "Compound exercise for chest", TypeExercise.WEIGHTLIFTING, listOf("Chest", "Triceps", "Front deltoids"), DayOfWeek.MONDAY),
            Exercise("Incline Dumbbell Press", "Inclined variation for upper chest", TypeExercise.WEIGHTLIFTING, listOf("Upper chest", "Triceps"), DayOfWeek.MONDAY),
            Exercise("Dumbbell Flyes", "Isolation movement for the chest", TypeExercise.WEIGHTLIFTING, listOf("Chest"), DayOfWeek.MONDAY),
            Exercise("Dips", "Bodyweight or weighted pushing exercise", TypeExercise.WEIGHTLIFTING, listOf("Chest", "Triceps"), DayOfWeek.MONDAY),
            Exercise("Triceps Rope Pushdown", "Isolation movement for triceps", TypeExercise.WEIGHTLIFTING, listOf("Triceps"), DayOfWeek.MONDAY),
            Exercise("Triceps Kickback", "Triceps extension with dumbbell", TypeExercise.WEIGHTLIFTING, listOf("Triceps"), DayOfWeek.MONDAY)
        ),
        DayOfWeek.TUESDAY to listOf(
            Exercise("Pull-ups", "Vertical pulling bodyweight movement", TypeExercise.WEIGHTLIFTING, listOf("Back", "Biceps"), DayOfWeek.TUESDAY),
            Exercise("Barbell Row", "Horizontal pulling exercise", TypeExercise.WEIGHTLIFTING, listOf("Back", "Biceps"), DayOfWeek.TUESDAY),
            Exercise("Lat Pulldown", "Pulldown alternative to pull-ups", TypeExercise.WEIGHTLIFTING, listOf("Back"), DayOfWeek.TUESDAY),
            Exercise("One-arm Dumbbell Row", "Unilateral row for balance", TypeExercise.WEIGHTLIFTING, listOf("Back"), DayOfWeek.TUESDAY),
            Exercise("Barbell Curl", "Classic biceps curl", TypeExercise.WEIGHTLIFTING, listOf("Biceps"), DayOfWeek.TUESDAY),
            Exercise("Hammer Curl", "Focus on brachialis and forearms", TypeExercise.WEIGHTLIFTING, listOf("Biceps", "Forearms"), DayOfWeek.TUESDAY)
        ),
        DayOfWeek.WEDNESDAY to listOf(
            Exercise("Barbell Squats", "Basic compound leg exercise", TypeExercise.WEIGHTLIFTING, listOf("Quads", "Glutes", "Core"), DayOfWeek.WEDNESDAY),
            Exercise("Leg Press", "Machine-based leg press movement", TypeExercise.WEIGHTLIFTING, listOf("Quads", "Glutes"), DayOfWeek.WEDNESDAY),
            Exercise("Romanian Deadlift", "Posterior chain pull", TypeExercise.WEIGHTLIFTING, listOf("Hamstrings", "Glutes"), DayOfWeek.WEDNESDAY),
            Exercise("Lying Leg Curl", "Isolates hamstrings on machine", TypeExercise.WEIGHTLIFTING, listOf("Hamstrings"), DayOfWeek.WEDNESDAY),
            Exercise("Calf Raises", "Targeting the calves", TypeExercise.WEIGHTLIFTING, listOf("Calves"), DayOfWeek.WEDNESDAY)
        ),
        DayOfWeek.THURSDAY to listOf(
            Exercise("Military Press", "Overhead barbell press", TypeExercise.WEIGHTLIFTING, listOf("Shoulders", "Triceps"), DayOfWeek.THURSDAY),
            Exercise("Lateral Raises", "Isolation for side deltoids", TypeExercise.WEIGHTLIFTING, listOf("Lateral deltoids"), DayOfWeek.THURSDAY),
            Exercise("Front Raises", "Targeting front delts", TypeExercise.WEIGHTLIFTING, listOf("Front deltoids"), DayOfWeek.THURSDAY),
            Exercise("Reverse Flyes", "Posterior deltoid isolation", TypeExercise.WEIGHTLIFTING, listOf("Rear deltoids"), DayOfWeek.THURSDAY),
            Exercise("Barbell Shrugs", "Trap development", TypeExercise.WEIGHTLIFTING, listOf("Trapezius"), DayOfWeek.THURSDAY),
            Exercise("Machine Crunch", "Abdominal contraction on machine", TypeExercise.WEIGHTLIFTING, listOf("Abdominals"), DayOfWeek.THURSDAY),
            Exercise("Leg Raises", "Lower abdominals", TypeExercise.WEIGHTLIFTING, listOf("Abdominals"), DayOfWeek.THURSDAY)
        ),
        DayOfWeek.FRIDAY to listOf(
            Exercise("Front Squat", "Core and quad-dominant squat", TypeExercise.WEIGHTLIFTING, listOf("Quads", "Core"), DayOfWeek.FRIDAY),
            Exercise("Conventional Deadlift", "Full-body posterior chain lift", TypeExercise.WEIGHTLIFTING, listOf("Back", "Glutes", "Legs"), DayOfWeek.FRIDAY),
            Exercise("Barbell Bench Press", "Main chest pressing lift", TypeExercise.WEIGHTLIFTING, listOf("Chest", "Triceps"), DayOfWeek.FRIDAY),
            Exercise("Weighted Pull-ups", "Pull-ups with added resistance", TypeExercise.WEIGHTLIFTING, listOf("Back", "Biceps"), DayOfWeek.FRIDAY),
            Exercise("Dips", "Triceps and chest pushing movement", TypeExercise.WEIGHTLIFTING, listOf("Triceps", "Chest"), DayOfWeek.FRIDAY)
        ),
        DayOfWeek.SATURDAY to listOf(
            Exercise("Hip Thrust", "Glute-focused hip extension", TypeExercise.WEIGHTLIFTING, listOf("Glutes"), DayOfWeek.SATURDAY),
            Exercise("Romanian Deadlift", "Hamstring and glute dominant pull", TypeExercise.WEIGHTLIFTING, listOf("Hamstrings", "Glutes"), DayOfWeek.SATURDAY),
            Exercise("Walking Lunges", "Unilateral leg training", TypeExercise.WEIGHTLIFTING, listOf("Legs", "Glutes"), DayOfWeek.SATURDAY),
            Exercise("Hip Abduction Machine", "Glute medius isolation", TypeExercise.WEIGHTLIFTING, listOf("Glutes"), DayOfWeek.SATURDAY),
            Exercise("Seated Leg Curl", "Hamstring isolation", TypeExercise.WEIGHTLIFTING, listOf("Hamstrings"), DayOfWeek.SATURDAY),
            Exercise("Plank", "Isometric core hold", TypeExercise.WEIGHTLIFTING, listOf("Abdominals", "Core"), DayOfWeek.SATURDAY)
        ),
        DayOfWeek.SUNDAY to listOf(
            Exercise("Incline Dumbbell Curl", "Stretched bicep curl", TypeExercise.WEIGHTLIFTING, listOf("Biceps"), DayOfWeek.SUNDAY),
            Exercise("Concentration Curl", "Focused bicep contraction", TypeExercise.WEIGHTLIFTING, listOf("Biceps"), DayOfWeek.SUNDAY),
            Exercise("Overhead Triceps Extension", "Targets long head of the triceps", TypeExercise.WEIGHTLIFTING, listOf("Triceps"), DayOfWeek.SUNDAY),
            Exercise("Arnold Press", "Full shoulder press variation", TypeExercise.WEIGHTLIFTING, listOf("Shoulders"), DayOfWeek.SUNDAY),
            Exercise("Lateral Raises", "Side delt isolation", TypeExercise.WEIGHTLIFTING, listOf("Shoulders"), DayOfWeek.SUNDAY),
            Exercise("Russian Twists", "Rotational core exercise", TypeExercise.WEIGHTLIFTING, listOf("Obliques", "Abdominals"), DayOfWeek.SUNDAY)
        )
    )

    val machinesRoutines = mapOf(
        DayOfWeek.MONDAY to listOf(
            Exercise("Machine Chest Press", "Compound machine exercise for chest", TypeExercise.MACHINES, listOf("Chest", "Triceps", "Front deltoids"), DayOfWeek.MONDAY),
            Exercise("Pec Deck (Chest Fly Machine)", "Isolation movement for inner chest", TypeExercise.MACHINES, listOf("Chest"), DayOfWeek.MONDAY),
            Exercise("Lat Pulldown", "Vertical pulling exercise on machine", TypeExercise.MACHINES, listOf("Back", "Biceps"), DayOfWeek.MONDAY),
            Exercise("Seated Row Machine", "Horizontal pulling for back", TypeExercise.MACHINES, listOf("Back", "Trapezius", "Rhomboids"), DayOfWeek.MONDAY)
        ),

        DayOfWeek.TUESDAY to listOf(
            Exercise("Leg Press", "Heavy compound leg press machine", TypeExercise.MACHINES, listOf("Quads", "Glutes"), DayOfWeek.TUESDAY),
            Exercise("Leg Extension Machine", "Isolates quadriceps", TypeExercise.MACHINES, listOf("Quadriceps"), DayOfWeek.TUESDAY),
            Exercise("Lying Leg Curl Machine", "Isolates hamstrings", TypeExercise.MACHINES, listOf("Hamstrings"), DayOfWeek.TUESDAY),
            Exercise("Standing Calf Raise Machine", "Builds calves", TypeExercise.MACHINES, listOf("Calves"), DayOfWeek.TUESDAY)
        ),

        DayOfWeek.WEDNESDAY to listOf(
            Exercise("Shoulder Press Machine", "Overhead shoulder press", TypeExercise.MACHINES, listOf("Deltoids", "Triceps"), DayOfWeek.WEDNESDAY),
            Exercise("Lateral Raise Machine", "Isolation for side deltoids", TypeExercise.MACHINES, listOf("Lateral deltoids"), DayOfWeek.WEDNESDAY),
            Exercise("Machine Crunch", "Ab isolation exercise", TypeExercise.MACHINES, listOf("Abdominals"), DayOfWeek.WEDNESDAY),
            Exercise("Hanging Leg Raise Machine", "Targets lower abs", TypeExercise.MACHINES, listOf("Lower abdominals"), DayOfWeek.WEDNESDAY)
        ),

        DayOfWeek.THURSDAY to listOf(
            Exercise("Lat Pulldown", "Vertical pull to target back", TypeExercise.MACHINES, listOf("Lats"), DayOfWeek.THURSDAY),
            Exercise("Seated Row Machine", "Pulling movement for mid-back", TypeExercise.MACHINES, listOf("Back", "Rhomboids"), DayOfWeek.THURSDAY),
            Exercise("Biceps Curl Machine", "Isolates the biceps", TypeExercise.MACHINES, listOf("Biceps"), DayOfWeek.THURSDAY),
            Exercise("Cable Bicep Curl", "Cable variation for constant tension", TypeExercise.MACHINES, listOf("Biceps"), DayOfWeek.THURSDAY)
        ),

        DayOfWeek.FRIDAY to listOf(
            Exercise("Machine Chest Press", "Push movement for chest", TypeExercise.MACHINES, listOf("Chest", "Triceps"), DayOfWeek.FRIDAY),
            Exercise("Pec Deck", "Chest fly machine for pec isolation", TypeExercise.MACHINES, listOf("Chest"), DayOfWeek.FRIDAY),
            Exercise("Triceps Pushdown Machine", "Isolates the triceps", TypeExercise.MACHINES, listOf("Triceps"), DayOfWeek.FRIDAY),
            Exercise("Assisted Triceps Dips Machine", "Triceps-focused dip variation", TypeExercise.MACHINES, listOf("Triceps"), DayOfWeek.FRIDAY)
        ),

        DayOfWeek.SATURDAY to listOf(
            Exercise("Hip Thrust Machine", "Glute-focused movement", TypeExercise.MACHINES, listOf("Glutes"), DayOfWeek.SATURDAY),
            Exercise("Romanian Deadlift with Smith Machine", "Hamstring and glute dominant", TypeExercise.MACHINES, listOf("Hamstrings", "Glutes"), DayOfWeek.SATURDAY),
            Exercise("Walking Lunges with Smith Machine", "Unilateral leg work", TypeExercise.MACHINES, listOf("Quads", "Glutes"), DayOfWeek.SATURDAY),
            Exercise("Hip Abduction Machine", "Outer glutes and abductors", TypeExercise.MACHINES, listOf("Gluteus medius"), DayOfWeek.SATURDAY),
            Exercise("Seated Calf Raise Machine", "Isolates calves", TypeExercise.MACHINES, listOf("Calves"), DayOfWeek.SATURDAY)
        ),

        DayOfWeek.SUNDAY to listOf(
            Exercise("Machine Crunch", "Ab crunch using machine", TypeExercise.MACHINES, listOf("Abdominals"), DayOfWeek.SUNDAY),
            Exercise("Leg Raise Machine", "Targets lower abs", TypeExercise.MACHINES, listOf("Lower abdominals"), DayOfWeek.SUNDAY),
            Exercise("Oblique Twist Machine", "Rotational movement for obliques", TypeExercise.MACHINES, listOf("Obliques"), DayOfWeek.SUNDAY),
            Exercise("Light Seated Row Machine", "Active recovery for back", TypeExercise.MACHINES, listOf("Back", "Biceps"), DayOfWeek.SUNDAY),
            Exercise("Unilateral Leg Press (Light)", "Leg press variation for recovery", TypeExercise.MACHINES, listOf("Legs"), DayOfWeek.SUNDAY)
        )
    )

    val tensRoutine = mapOf(
        DayOfWeek.MONDAY to listOf(
            TensExercise("Tens exercise combos without getting off the parallel bar", TypeTensExercise.ALL),
            TensExercise("Press plank: go up to handstand and return to plank position. Use a resistance band", TypeTensExercise.PLANK),
            TensExercise("Push-up plank: lower as much as possible and return to plank. Use a resistance band", TypeTensExercise.PLANK),
            TensExercise("Maltese plank push-up: lower as much as possible and return to maltese plank position", TypeTensExercise.PLANK),
            TensExercise("Handstand push-up: lower until your head touches the floor, then return to handstand position", TypeTensExercise.PLANK),
            TensExercise("Front lever press: lift legs to touch the bar, then return to front lever. Use a resistance band", TypeTensExercise.FRONT_LEVEL),
            TensExercise("Front lever pull-up: pull up to touch the bar, then return to front lever. Use a resistance band", TypeTensExercise.FRONT_LEVEL),
            TensExercise("Maltese front lever press: lift legs to touch the bar, then return to front lever", TypeTensExercise.FRONT_LEVEL),
            TensExercise("Pull-up hold: pull until your chin is above the bar, hold for 3 seconds, then lower. Do it without a band if possible", TypeTensExercise.FRONT_LEVEL)
        ),

        DayOfWeek.TUESDAY to listOf(
            TensExercise("Tension exercise combos without getting off the parallel bar", TypeTensExercise.ALL),
            TensExercise("Front lever press: lift legs to touch the bar, then return to front lever. Use a resistance band", TypeTensExercise.FRONT_LEVEL),
            TensExercise("Front lever pull-up: pull up to touch the bar, then return to front lever. Use a resistance band", TypeTensExercise.FRONT_LEVEL),
            TensExercise("Maltese front lever press: lift legs to touch the bar, then return to front lever", TypeTensExercise.FRONT_LEVEL),
            TensExercise("Pull-up hold: pull until your chin is above the bar, hold for 3 seconds, then lower. Do it without a band if possible", TypeTensExercise.FRONT_LEVEL)
        ),

        DayOfWeek.WEDNESDAY to listOf(
            TensExercise("Tension exercise combos without getting off the parallel bar", TypeTensExercise.ALL),
            TensExercise("Front lever press on the bar: lift in front lever to touch the bar with your legs, then return to hanging position. Use a resistance band", TypeTensExercise.FRONT_LEVEL),
            TensExercise("Front lever pull-up: pull up to touch the bar and briefly hold, then return to front lever. Use a resistance band", TypeTensExercise.FRONT_LEVEL),
            TensExercise("One-arm pull-up with support: pull with one arm while the other stays extended and holding. Chin goes over the bar, then return to hanging position", TypeTensExercise.FRONT_LEVEL)
        ),

        DayOfWeek.THURSDAY to listOf(
            TensExercise("Tension exercise combos without getting off the parallel bar", TypeTensExercise.ALL),
            TensExercise("Press plank: go up to handstand and return to plank position. Use a resistance band", TypeTensExercise.PLANK),
            TensExercise("Push-up plank: lower and return to plank. Use a resistance band", TypeTensExercise.FRONT_LEVEL),
            TensExercise("One-arm push-up with support: lower while holding with the extended arm, then return to plank position", TypeTensExercise.FRONT_LEVEL)
        )
    )*/

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

    /*val basicsRoutine = mapOf(
        DayOfWeek.MONDAY to listOf(
            Exercise("Push-ups", "Basic push-ups", TypeExercise.BASIC, listOf("Chest", "Triceps", "Shoulders"), DayOfWeek.MONDAY),
            Exercise("Diamond push-ups", "Hands together under the chest", TypeExercise.BASIC, listOf("Triceps", "Inner chest"), DayOfWeek.MONDAY),
            Exercise("Decline push-ups", "Feet elevated", TypeExercise.BASIC, listOf("Upper chest", "Shoulders"), DayOfWeek.MONDAY),
            Exercise("Pike push-ups", "Hips elevated, shoulder-focused", TypeExercise.BASIC, listOf("Shoulders"), DayOfWeek.MONDAY),
            Exercise("Dips", "Performed on parallel bars", TypeExercise.BASIC, listOf("Chest", "Triceps", "Shoulders"), DayOfWeek.MONDAY)
        ),

        DayOfWeek.TUESDAY to listOf(
            Exercise("Squats", "Classic bodyweight squats", TypeExercise.BASIC, listOf("Quadriceps", "Glutes"), DayOfWeek.TUESDAY),
            Exercise("Jump squats", "Explosive squat with jump", TypeExercise.BASIC, listOf("Quadriceps", "Glutes", "Calves"), DayOfWeek.TUESDAY),
            Exercise("Lunges", "Alternating steps forward", TypeExercise.BASIC, listOf("Hamstrings", "Glutes"), DayOfWeek.TUESDAY),
            Exercise("Glute bridge", "Lift hips while lying down", TypeExercise.BASIC, listOf("Glutes", "Lower back"), DayOfWeek.TUESDAY),
            Exercise("Calf raises", "Raise heels while standing", TypeExercise.BASIC, listOf("Calves"), DayOfWeek.TUESDAY)
        ),

        DayOfWeek.WEDNESDAY to listOf(
            Exercise("Plank", "Hold a straight body position", TypeExercise.BASIC, listOf("Abs", "Lower back"), DayOfWeek.WEDNESDAY),
            Exercise("Side plank", "Support on one arm sideways", TypeExercise.BASIC, listOf("Obliques"), DayOfWeek.WEDNESDAY),
            Exercise("Crunches", "Classic abdominal crunch", TypeExercise.BASIC, listOf("Upper abs"), DayOfWeek.WEDNESDAY),
            Exercise("Leg raises", "Raise legs while lying down", TypeExercise.BASIC, listOf("Lower abs"), DayOfWeek.WEDNESDAY),
            Exercise("Bicycle crunches", "Alternate elbows and knees in the air", TypeExercise.BASIC, listOf("Full abs", "Obliques"), DayOfWeek.WEDNESDAY)
        ),

        DayOfWeek.THURSDAY to listOf(
            Exercise("Pull-ups", "Lift body on a bar", TypeExercise.BASIC, listOf("Back", "Biceps"), DayOfWeek.THURSDAY),
            Exercise("Inverted rows", "Under a table or low bar", TypeExercise.BASIC, listOf("Lats", "Biceps", "Trapezius"), DayOfWeek.THURSDAY),
            Exercise("Towel bicep curls", "Manual resistance with towel", TypeExercise.BASIC, listOf("Biceps"), DayOfWeek.THURSDAY),
            Exercise("Superman", "Lie on stomach, lift arms and legs", TypeExercise.BASIC, listOf("Lower back"), DayOfWeek.THURSDAY),
            Exercise("Hollow hold", "Hold a 'banana' shape position", TypeExercise.BASIC, listOf("Abs", "Lower back"), DayOfWeek.THURSDAY)
        ),

        DayOfWeek.FRIDAY to listOf(
            Exercise("Jumping jacks", "Jump with arms and legs wide", TypeExercise.BASIC, listOf("Legs", "Shoulders", "Cardio"), DayOfWeek.FRIDAY),
            Exercise("Burpees", "Full-body explosive movement", TypeExercise.BASIC, listOf("Full body", "Cardio"), DayOfWeek.FRIDAY),
            Exercise("Mountain climbers", "Alternate knees to chest", TypeExercise.BASIC, listOf("Abs", "Legs", "Cardio"), DayOfWeek.FRIDAY),
            Exercise("Jump squats", "Explosive version of squat", TypeExercise.BASIC, listOf("Legs", "Glutes"), DayOfWeek.FRIDAY),
            Exercise("Push-ups", "Standard push-ups in the circuit", TypeExercise.BASIC, listOf("Chest", "Triceps"), DayOfWeek.FRIDAY)
        )
    )*/
}