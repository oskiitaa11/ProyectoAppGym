package com.example.proyectoappgym.db.db_users

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import com.example.proyectoappgym.db.db_routines.AllExercises
import com.example.proyectoappgym.db.db_routines.AllRoutines
import com.example.proyectoappgym.db.retrofit.entity.ChatMessage
import com.example.proyectoappgym.db.retrofit.entity.ChatRequest
import com.example.proyectoappgym.db.retrofit.entity.ChatResponse
import com.example.proyectoappgym.db.retrofit.entity.ExercisesName
import com.example.proyectoappgym.db.retrofit.entity.OpenAiApi
import com.example.proyectoappgym.db.retrofit.entity.Routines
import com.example.proyectoappgym.entity.Avatars
import com.example.proyectoappgym.entity.DayOfWeek
import com.example.proyectoappgym.entity.Exercise
import com.example.proyectoappgym.entity.ExerciseLevel
import com.example.proyectoappgym.entity.GroupMuscles
import com.example.proyectoappgym.entity.Muscles
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.RealizationExercise
import com.example.proyectoappgym.entity.TrainingRoutine
import com.example.proyectoappgym.entity.TypeExercise
import com.example.proyectoappgym.entity.TypeTensExercise
import com.example.proyectoappgym.entity.User
import com.example.proyectoappgym.ui.screens.ExerciseScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.asDeferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.annotation.meta.When
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.filterKeys
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.toMutableList
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.jvm.java


class UserDatabase: RepositoryUserDatabase {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var uidLoggedUser: String? = null
    val currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    private val api: OpenAiApi = retrofit.create(OpenAiApi::class.java)
    private val apiKey = "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"
    private val allRoutines = AllRoutines
    private val allExercises = AllExercises

    fun initializerApp() {
        auth = Firebase.auth
        db = Firebase.firestore
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    @SuppressLint("RestrictedApi")
    override suspend fun addUser(user: User): Boolean {
        //val userMap = convertUserToMap(user)
        val userFirebase: FirebaseUser?
        val password = user.password
        val routines: List<TrainingRoutine>
        user.password = ""; //Para que la contraseña no se vea desde el archivo de la database

        userFirebase = suspendCoroutine { continuation ->

           auth.createUserWithEmailAndPassword(user.email, password)
               .addOnSuccessListener {  result ->
                   continuation.resume(result.user)
               }.addOnFailureListener {
                   continuation.resume(null)
               }
        }

        if(userFirebase != null){
            routines = createRoutine(user.allQuestionsAnswered)
            user.trainingRoutines = routines
        }

        return suspendCoroutine { continuation ->
            if(userFirebase != null) {
                db.collection("Users").document(userFirebase.uid).set(user)
                    .addOnSuccessListener {
                        continuation.resume(true) //2 es true
                    }.addOnFailureListener {
                        continuation.resume(false) //1 es false
                    }
            } else {
                continuation.resume(false)
            }
        }
    }

    override suspend fun authWithGoogle(idToken: String): Boolean {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    continuation.resume(task.isSuccessful)
                }
        }
    }

    override suspend fun signIn(email: String, password: String): Int {
        /* El suspendCoroutine suspende la suspend funtion hasta que la task ha
           terminado de ejecutarse, devolviendo el valor pasado en resume()*/
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener { result ->
                uidLoggedUser = result.user?.uid

                continuation.resume(1)
            }.addOnFailureListener { exception ->
                val intErrorSignIn = when (exception) {
                    is FirebaseAuthInvalidUserException -> 2
                    is FirebaseAuthInvalidCredentialsException -> 3
                    else -> 4
                }

                continuation.resume(intErrorSignIn)
            }
        }

    }

    override suspend fun emailExist(email: String): Boolean? {
        var emailExist: Boolean? = null

        emailExist = suspendCoroutine { continuation ->
            db.collection("Users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener {
                    continuation.resume(!it.isEmpty)
                }.addOnFailureListener {
                    continuation.resume(false)
                }

            }

        return emailExist
    }


    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun deleteUser(user: User): String {
        var textTaskCompleted = ""

        db.collection("Users").document(user.username).delete().addOnCompleteListener { documentReference ->
            textTaskCompleted = "Deleted user"
        }.addOnFailureListener {
            textTaskCompleted = "Failure to the to delete user"
        }

        return textTaskCompleted
    }

    override suspend fun userExist(username: String): Boolean? {
        var isExist: Boolean? = null

        db.collection("Users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener {
                isExist = !it.isEmpty
            }.addOnFailureListener {
                isExist = false
            }.asDeferred().join()

        return isExist
    }

    override suspend fun updateNameCurrentUser(newName: String) {
        suspendCoroutine<Unit> { db.collection("Users").document(uidLoggedUser as String).update("name", newName) }
    }

    override suspend fun updateAvatarProfile(newAvatar: Avatars) {
        suspendCoroutine<Unit> {
            db.collection("Users")
                .document(uidLoggedUser as String)
                .update("profileAvatar", newAvatar)
        }
    }

    override suspend fun updateResponses(question: String, newResponses: List<String>) {
        suspendCoroutine<Unit> {
            db.collection("Users")
                .document(uidLoggedUser as String)
                .update(FieldPath.of("allQuestionsAnswered", question), newResponses)
        }
    }

    override suspend fun removeResponsesOfQuestion(question: String) {
        suspendCoroutine<Unit> {
            db.collection("Users")
                .document(uidLoggedUser as String)
                .update(FieldPath.of("allQuestionsAnswered", question), emptyList<String>())
        }
    }

    override suspend fun updateCurrentUser() {

        if(uidLoggedUser != null) {
            db.collection("Users")
                .document(uidLoggedUser as String)
                .addSnapshotListener { snapshot, error ->
                    if(snapshot != null && snapshot.exists()) currentUser.value = snapshot.toObject<User>()
                }
        }

    }

    suspend fun updateCurrentUserAfterLogin() {
        if (uidLoggedUser != null)
            db.collection("Users")
                .document(uidLoggedUser as String)
                .get()
                .addOnSuccessListener { result ->
                    currentUser.update { result.toObject<User>() }
                }
    }

    override suspend fun getCurrentUser(): MutableStateFlow<User?> {
        return currentUser
    }

    fun updateUidLoggedUser(newUid: String?) {
        uidLoggedUser = newUid
    }

    fun getUidLoggedUser(): String? {
        return uidLoggedUser
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override suspend fun changeWeeklyRoutine(
        newAnsweredQuestion: Map<String, List<String>>
    ): Boolean {
        var newRoutine = createRoutine(newAnsweredQuestion)
        /*var trainingRoutines = actualRoutines
        var mapWithDifferences = makeMapWithDifferencesOfTwoMaps(oldAnsweredQuestions, newAnsweredQuestion)
        var removedExercisesType = getRemovedExercisesType(oldAnsweredQuestions, newAnsweredQuestion)
        var addedExerciseType = getAddedExerciseType(oldAnsweredQuestions, newAnsweredQuestion)

        var c = oldAnsweredQuestions
        var i = newAnsweredQuestion*/
        return suspendCoroutine<Boolean> { continuation ->
            db.collection("Users").document(uidLoggedUser ?: "").update("trainingRoutines", newRoutine).addOnSuccessListener { result ->
                continuation.resume(true)
            }.addOnFailureListener {
                continuation.resume(false)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override suspend fun createRoutine(answeredQuestions: Map<String, List<String>>): List<TrainingRoutine> {
        var numberOfDaysForTraining = answeredQuestions["Which days of the week can/do you want to train?"]?.count() ?: 0
        val numberOfTypeExercises = (answeredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.count() ?: 0) + (answeredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"]?.count() ?: 0)
        val typeExercisesGym = answeredQuestions["What types of gym exercises do you focus on or want to focus on?"] ?: emptyList()
        val typeExercisesCalisthenics = answeredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"] ?: emptyList()
        val allTypeExercisesForTrain = typeExercisesGym.toMutableList().apply { addAll(typeExercisesCalisthenics) }
        val allTypeDuplicatesExercises = getAllTypeExercises(allTypeExercisesForTrain, numberOfDaysForTraining, numberOfTypeExercises).map { TypeExercise.fromString(it) }.sortedBy { it.id }.toMutableList()
        var weeklyRoutines = mutableListOf<TrainingRoutine>()
        var allGroupMuscles = mutableListOf(GroupMuscles.PUSH_UP, GroupMuscles.PULL_UP, GroupMuscles.CORE, GroupMuscles.LEGS)
        val daysOfWeekForTrain = answeredQuestions["Which days of the week can/do you want to train?"]?.toMutableList() ?: mutableListOf()
        val goals = answeredQuestions["What are your goals?"] ?: emptyList()
        val repetitions = when(goals[0]) {
            "Gain more strength" -> 6
            "Increase endurance" -> 20
            "Build more muscle" -> 12
            else -> 0
        }
        val restBetweenSeries = when(goals[0]) {
            "Gain more strength" -> 2
            "Increase endurance" -> 1
            "Build more muscle" -> 2
            else -> 0
        }
        var excludedExercises = mutableListOf(
            ExercisesName.CABLE_PULL_THROUGH,
            ExercisesName.LEG_EXTENSION_MACHINE,
            ExercisesName.AB_CRUNCH_MACHINE,
            ExercisesName.BACK_EXTENSION_MACHINE,
            ExercisesName.FRENCH_PRESS_WITH_BARBELL_OR_DUMBBELLS,
            ExercisesName.OVERHEAD_TRICEPS_EXTENSION_WITH_DUMBBELL,
            ExercisesName.LATERAL_RAISES_WITH_DUMBBELLS,
            ExercisesName.JUMP_SQUATS,
            ExercisesName.BARBELL_CURL,
            ExercisesName.CONCENTRATION_CURL
        )

        if(numberOfDaysForTraining == 5) {
            excludedExercises = mutableListOf(
                ExercisesName.DEADLIFT,
                ExercisesName.CONCENTRATION_CURL,
                ExercisesName.FRONT_RAISES_WITH_DUMBBELLS_OR_BARBELL,
                ExercisesName.FLAT_BENCH_DUMBBELL_FLYES
            )

            allTypeDuplicatesExercises.removeAt(allTypeDuplicatesExercises.size - 1)

            allTypeDuplicatesExercises.forEach {
                addRoutineAccordingType(it, allGroupMuscles.removeAt(0), weeklyRoutines, daysOfWeekForTrain.first(), repetitions, restBetweenSeries)
                daysOfWeekForTrain.removeAt(0)
            }

            addRoutine(weeklyRoutines, daysOfWeekForTrain.first(), listOf(ExercisesName.RUNNING), 0, 0, 0, "Running day")
            removeExercisesOfRoutine(weeklyRoutines, excludedExercises)

        } else if(numberOfDaysForTraining == 4) {
            excludedExercises = mutableListOf(
                ExercisesName.DEADLIFT,
                ExercisesName.CONCENTRATION_CURL,
                ExercisesName.FRONT_RAISES_WITH_DUMBBELLS_OR_BARBELL,
                ExercisesName.FLAT_BENCH_DUMBBELL_FLYES
            )

            allTypeDuplicatesExercises.forEach {
                addRoutineAccordingType(it, allGroupMuscles.removeAt(0), weeklyRoutines, daysOfWeekForTrain.first(), repetitions, restBetweenSeries)
                daysOfWeekForTrain.removeAt(0)
            }

            removeExercisesOfRoutine(weeklyRoutines, excludedExercises)
        } else if(numberOfDaysForTraining == 3) {
            addRoutineOfLowDays(
                weeklyRoutines,
                daysOfWeekForTrain,
                allTypeDuplicatesExercises,
                repetitions,
                restBetweenSeries,
                1,
                allGroupMuscles
            ) { daysOfWeekForTrain.size != 1 }

            joinListsWithSameDayOfWeek(weeklyRoutines)

            removeExercisesOfRoutine(weeklyRoutines, excludedExercises)
        } else if(numberOfDaysForTraining == 2) {
            excludedExercises.addAll(listOf(ExercisesName.TRICEP_EXTENSION_MACHINE, ExercisesName.DEADLIFT,
                ExercisesName.ONE_ARM_DUMBBELL_ROW))
            addRoutineOfLowDays(
                weeklyRoutines,
                daysOfWeekForTrain,
                mutableListOf(allTypeDuplicatesExercises.first()),
                repetitions,
                restBetweenSeries,
                2,
                allGroupMuscles
            ) { false }

            daysOfWeekForTrain.removeAt(0)

            addRoutineOfLowDays(
                weeklyRoutines,
                daysOfWeekForTrain,
                mutableListOf(allTypeDuplicatesExercises.last()),
                repetitions,
                restBetweenSeries,
                2,
                allGroupMuscles.apply { removeAt(0) }
            ) { false }

            joinListsWithSameDayOfWeek(weeklyRoutines)

            removeExercisesOfRoutine(weeklyRoutines, excludedExercises)
        } else {
            excludedExercises.addAll(listOf(ExercisesName.FLAT_BENCH_DUMBBELL_FLYES, ExercisesName.HAMMER_CURL_WITH_DUMBBELLS,
                ExercisesName.RUSSIAN_TWIST_WITH_DUMBBELL_OR_PLATE, ExercisesName.LEG_PRESS_MACHINE))

            addRoutineFullBody(if(typeExercisesCalisthenics.isNotEmpty()) typeExercisesCalisthenics[0] else typeExercisesGym[0], weeklyRoutines, daysOfWeekForTrain.first(), repetitions, restBetweenSeries)

            removeExercisesOfRoutine(weeklyRoutines, excludedExercises)
        }

        return weeklyRoutines.toList()
    }

    private suspend fun addRoutine(routines: MutableList<TrainingRoutine>, dayOfWeek: String, exercises: List<ExercisesName>, sets: Int, repetitions: Int, restBetweenSeries: Int, name: String) {
       val realizationExercises = exercises.map { RealizationExercise(it, sets, repetitions, restBetweenSeries ) }

        routines.add(TrainingRoutine(DayOfWeek.fromString(dayOfWeek), name, realizationExercises))
    }

    private suspend fun getAllTypeExercises(allTypeExercisesForTrain: List<String>, numberOfDaysForTraining: Int, numberOfTypeExercises: Int): List<String> {
        val allTypeDuplicatesExercises = mutableListOf<String>()
        val thereIsTensionExercises = allTypeExercisesForTrain.any { it == "Tension exercises" }
        var typeExerciseForDuplicate: String

        if(numberOfTypeExercises == 1) {
            allTypeExercisesForTrain.forEach { typeExercise ->
                allTypeDuplicatesExercises.addAll(List(numberOfDaysForTraining) { typeExercise })
            }
            return allTypeDuplicatesExercises
        }

        if(numberOfDaysForTraining == 5) {
            when(numberOfTypeExercises) {
                2 -> allTypeExercisesForTrain.forEach { typeExercise ->
                    typeExerciseForDuplicate = if(thereIsTensionExercises) "Tension exercises" else allTypeExercisesForTrain[0]
                    allTypeDuplicatesExercises.addAll(List(if(typeExercise == typeExerciseForDuplicate) 2 else 3) { typeExercise })
                }
                3 -> allTypeExercisesForTrain.forEach { typeExercise ->
                    typeExerciseForDuplicate = if(thereIsTensionExercises) "Tension exercises" else allTypeExercisesForTrain[1]
                    allTypeDuplicatesExercises.addAll(List(if(typeExercise != typeExerciseForDuplicate) 2 else 1) { typeExercise })
                }
                else -> allTypeExercisesForTrain.forEach { typeExercise ->
                    typeExerciseForDuplicate = if(thereIsTensionExercises) "Tension exercises" else allTypeExercisesForTrain.last()
                    allTypeDuplicatesExercises.addAll(List(if(typeExercise == typeExerciseForDuplicate) 2 else 1) { typeExercise })
                }
            }
        } else if(numberOfDaysForTraining == 4) {
            when(numberOfTypeExercises) {
                2 -> allTypeExercisesForTrain.forEach { typeExercise ->
                    allTypeDuplicatesExercises.addAll(List(2) { typeExercise })
                }
                3 -> allTypeExercisesForTrain.forEach { typeExercise ->
                    typeExerciseForDuplicate = if(thereIsTensionExercises) "Tension exercises" else allTypeExercisesForTrain.last()
                    allTypeDuplicatesExercises.addAll(List(if(typeExercise == typeExerciseForDuplicate) 2 else 1) { typeExercise })
                }
                else -> allTypeDuplicatesExercises.addAll(allTypeExercisesForTrain)
            }
        } else if(numberOfDaysForTraining == 3) {
            when(numberOfTypeExercises) {
                2 -> allTypeExercisesForTrain.forEach { typeExercise ->
                    typeExerciseForDuplicate = if(thereIsTensionExercises) "Tension exercises" else allTypeExercisesForTrain[0]
                    allTypeDuplicatesExercises.addAll(List(if(typeExerciseForDuplicate == typeExercise) 2 else 1) { typeExercise })
                }
                else -> allTypeDuplicatesExercises.addAll(allTypeExercisesForTrain)
            }
        } else {
            return allTypeExercisesForTrain
        }

        return allTypeDuplicatesExercises

    }

    private suspend fun addRoutineAccordingType(typeExercise: TypeExercise, groupMuscles: GroupMuscles, weeklyRoutines: MutableList<TrainingRoutine>, dayOfWeekForTrain: String, repetitions: Int, restBetweenSeries: Int) {
        when(typeExercise) {
            TypeExercise.MACHINES -> {
                addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.machineExercises[groupMuscles] ?: emptyList(), 3, repetitions, restBetweenSeries, groupMuscles.nameDay)
                /*when(numberOfDaysForTraining) {
                    4 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineMachineExercisesPushUp, 3, repetitions, restBetweenSeries, "Push day")
                    3 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineMachineExercisesPullUp, 3, repetitions, restBetweenSeries, "Pull day")
                    2 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineMachineExercisesLegs, 3, repetitions, restBetweenSeries, "Legs day")
                    1 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineMachineExercisesCore, 3, repetitions, restBetweenSeries, "Core day")
                }*/
            }
            TypeExercise.WEIGHTLIFTING -> {
                addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.weightliftingExercises[groupMuscles] ?: emptyList(), 3, repetitions, restBetweenSeries, groupMuscles.nameDay)
                /*when(numberOfDaysForTraining) {
                    4 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineWeightliftingPushUp, 3, repetitions, restBetweenSeries, "Push day")
                    3 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineWeightliftingPullUp, 3, repetitions, restBetweenSeries, "Pull day")
                    2 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineWeightliftingLegs, 3, repetitions, restBetweenSeries, "Legs day")
                    1 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineWeightliftingCore, 3, repetitions, restBetweenSeries, "Core day")
                }*/
            }
            TypeExercise.BASIC -> {
                addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.basicExercises[groupMuscles] ?: emptyList(), 3, 20, restBetweenSeries, groupMuscles.nameDay)
                /*when(numberOfDaysForTraining) {
                    4 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineBasicPushUp, 4, repetitions, restBetweenSeries, "Push day")
                    3 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineBasicPullUp, 4, repetitions, restBetweenSeries, "Pull day")
                    2 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineBasicLegs, 4, repetitions, restBetweenSeries, "Legs day")
                    1 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineBasicCore, 4, repetitions, restBetweenSeries, "Core day")
                }*/
            }
            TypeExercise.TENS -> {
                addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.tensionExercises[groupMuscles] ?: emptyList(), 3, repetitions, restBetweenSeries, groupMuscles.nameDay)
                /*when(numberOfDaysForTraining) {
                    4 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineTensionPushUp, 4, repetitions, restBetweenSeries, "Push day")
                    3 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineTensionPullUp, 4, repetitions, restBetweenSeries, "Pull day")
                    2 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineBasicLegs, 4, repetitions, restBetweenSeries, "Legs day")
                    1 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineBasicCore, 4, repetitions, restBetweenSeries, "Core day")
                }*/
            }
            TypeExercise.CARDIO -> addRoutine(weeklyRoutines, dayOfWeekForTrain,  listOf(ExercisesName.RUNNING), 3, repetitions, restBetweenSeries, groupMuscles.nameDay)
        }
    }

    private suspend fun addRoutineFullBody(typeExercise: String, weeklyRoutines: MutableList<TrainingRoutine>, dayOfWeekForTrain: String, repetitions: Int, restBetweenSeries: Int) {
        val routineAllExercises: List<ExercisesName>
        
        
        when(typeExercise) {
            TypeExercise.MACHINES.nameType -> {
                routineAllExercises = (allRoutines.machineExercises[GroupMuscles.PUSH_UP]?.subList(3, 6)?: emptyList()) + ((allRoutines.machineExercises[GroupMuscles.PULL_UP] ?: emptyList()) - listOf(ExercisesName.BACK_EXTENSION_MACHINE,
                    ExercisesName.LAT_PULLDOWN_MACHINE)) + ((allRoutines.machineExercises[GroupMuscles.CORE] ?: emptyList()) - ExercisesName.BACK_EXTENSION_MACHINE) + ((allRoutines.machineExercises[GroupMuscles.PUSH_UP] ?: emptyList()) - ExercisesName.CABLE_PULL_THROUGH)
            }
            TypeExercise.WEIGHTLIFTING.nameType -> {
                routineAllExercises = ((allRoutines.weightliftingExercises[GroupMuscles.PUSH_UP] ?: emptyList()) - listOf(ExercisesName.FRONT_RAISES_WITH_DUMBBELLS_OR_BARBELL, ExercisesName.FRENCH_PRESS_WITH_BARBELL_OR_DUMBBELLS, ExercisesName.OVERHEAD_TRICEPS_EXTENSION_WITH_DUMBBELL)) + ((allRoutines.weightliftingExercises[GroupMuscles.PULL_UP] ?: emptyList()) - listOf(ExercisesName.ONE_ARM_DUMBBELL_ROW,
                    ExercisesName.DEADLIFT, ExercisesName.BARBELL_CURL, ExercisesName.CONCENTRATION_CURL)) + ((allRoutines.weightliftingExercises[GroupMuscles.CORE] ?: emptyList()) - listOf(ExercisesName.AB_CRUNCH_MACHINE, ExercisesName.CRUNCHES)) + ((allRoutines.weightliftingExercises[GroupMuscles.LEGS] ?: emptyList()) - listOf(ExercisesName.CABLE_PULL_THROUGH, ExercisesName.LEG_EXTENSION_MACHINE))
            }
            TypeExercise.BASIC.nameType -> {
                routineAllExercises = (allRoutines.basicExercises[GroupMuscles.PUSH_UP] ?: emptyList()) + (allRoutines.basicExercises[GroupMuscles.PULL_UP] ?: emptyList()) + (allRoutines.basicExercises[GroupMuscles.LEGS] ?: emptyList()) + (allRoutines.basicExercises[GroupMuscles.CORE] ?: emptyList())
            }
            TypeExercise.TENS.nameType -> {
                routineAllExercises = (allRoutines.tensionExercises[GroupMuscles.PUSH_UP] ?: emptyList()) + (allRoutines.tensionExercises[GroupMuscles.PULL_UP] ?: emptyList()) + (allRoutines.basicExercises[GroupMuscles.LEGS] ?: emptyList()) + (allRoutines.basicExercises[GroupMuscles.CORE] ?: emptyList())
            }
            else -> routineAllExercises = emptyList()
        }

        addRoutine(weeklyRoutines, dayOfWeekForTrain, routineAllExercises, 3, repetitions, restBetweenSeries, "Full-body day")
    }

    private suspend fun joinListsWithSameDayOfWeek(weeklyRoutines: MutableList<TrainingRoutine>) {
        val newWeeklyRoutine = weeklyRoutines.groupBy { it.dayOfWeek }.map { (dayOfWeek, trainingRoutines) ->
            if(trainingRoutines.size >= 2) {
                val nameList = trainingRoutines.map { it.name }.map { it.replace("day", "") }
                var name: String = ""
                nameList.forEach { name += it }
                name = name.replace(Regex("([a-z]) ([A-Z])"), "$1-$2") + "day"
                TrainingRoutine(dayOfWeek, name, trainingRoutines.flatMap { it.exercises })
            } else TrainingRoutine(dayOfWeek, trainingRoutines[0].name, trainingRoutines[0].exercises)
        }.toMutableList()

        weeklyRoutines.clear()
        weeklyRoutines.addAll(newWeeklyRoutine)
    }

    private suspend fun removeExercisesOfRoutine(weeklyRoutines: MutableList<TrainingRoutine>, excludedExercises: List<ExercisesName>) {
        weeklyRoutines.forEach { routine ->
            routine.exercises = routine.exercises.filterNot { it.exercise in excludedExercises }
        }
    }

    private suspend fun addRoutineOfLowDays(weeklyRoutines: MutableList<TrainingRoutine>, daysOfWeekForTrain: MutableList<String>, allTypeDuplicatesExercises: MutableList<TypeExercise>, repetitions: Int, restBetweenSeries: Int, steps: Int, groupMusclesForTraining: List<GroupMuscles>, isStop: () -> Boolean) {
        var actualTypeExercise: TypeExercise = TypeExercise.CARDIO

        for(indexGroupMusclesTrainingDay in 0..groupMusclesForTraining.size - 1 step steps) {//El bucle se repite 4 veces porque es el numero de movimimiento que se hace, donde cada movimiento ejercita un conjunto de musculos
            actualTypeExercise = if(allTypeDuplicatesExercises.isNotEmpty()) allTypeDuplicatesExercises.removeAt(0) else actualTypeExercise
            addRoutineAccordingType(actualTypeExercise, groupMusclesForTraining[indexGroupMusclesTrainingDay], weeklyRoutines, daysOfWeekForTrain.first(), repetitions, restBetweenSeries)
            if(isStop()) daysOfWeekForTrain.removeAt(0)

        }
    }
   /* override suspend fun updateProfileAvatar(uri: Uri, currentUser: User) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_images/$uidLoggedUser.jpg")

        suspendCoroutine<Unit> {
            imageRef.putFile(uri)
            imageRef.downloadUrl.addOnSuccessListener {
                currentUser.profileAvatar = it.toString()
                val o = currentUser.profileAvatar
            }.addOnFailureListener {
                val o = it.message
            }
        }
    }*/
}




