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

    @SuppressLint("RestrictedApi")
    override suspend fun addUser(user: User): Boolean {
        //val userMap = convertUserToMap(user)
        val userFirebase: FirebaseUser?
        val password = user.password
        user.password = ""; //Para que la contraseña no se vea desde el archivo de la database

        userFirebase = suspendCoroutine { continuation ->

           auth.createUserWithEmailAndPassword(user.email, password)
               .addOnSuccessListener {  result ->
                   continuation.resume(result.user)
               }.addOnFailureListener {
                   continuation.resume(null)
               }
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

    override suspend fun changeWeeklyRoutine(
        oldAnsweredQuestions: Map<String, List<String>>,
        newAnsweredQuestion: Map<String, List<String>>,
        actualRoutines: List<TrainingRoutine>,
        question: String,
        newResponses: List<String>,
    ): Boolean {
        val isCalisthenicTensionResponse = question == "What types of calisthenics exercises do you focus on or want to focus on?" && newResponses.any { it == "Tension exercises" }
        val isCalisthenicsBasicResponse = question == "What types of calisthenics exercises do you focus on or want to focus on?" && newResponses.any { it == "Basic exercises" }
        val isMachineExercisesResponse = question == "What types of calisthenics exercises do you focus on or want to focus on?" && newResponses.any { it == "Machine exercises" }
        val isWeightlifting = question == "What types of calisthenics exercises do you focus on or want to focus on?" && newResponses.any { it == "Weightlifting exercises" }
        var routines = mutableListOf<TrainingRoutine>()
        var trainingRoutines = emptyList<TrainingRoutine>()
        var mapWithDifferences = makeMapWithDifferencesOfTwoMaps(oldAnsweredQuestions, newAnsweredQuestion)
        var removedExercisesType = getRemovedExercisesType(oldAnsweredQuestions, newAnsweredQuestion)
        var addedExerciseType = getAddedExerciseType(oldAnsweredQuestions, newAnsweredQuestion)

        if(removedExercisesType.isNotEmpty()) removedDaysRoutines(removedExercisesType, actualRoutines)
        if(addedExerciseType.isNotEmpty()) {
            if(isCalisthenicTensionResponse) routines.addAll(implementCalisthenicTensionRoutine(newAnsweredQuestion))
            //if(isCalisthenicsBasicResponse)
        }


        return suspendCoroutine<Boolean> { continuation ->
            db.collection("Users").document(uidLoggedUser ?: "").update("trainingRoutines", trainingRoutines).addOnSuccessListener { result ->
                continuation.resume(true)
            }.addOnFailureListener {
                continuation.resume(false)
            }
        }
    }

    override suspend fun saveUserTrainingRoutinesGpt(questions: Map<String, List<String>>, emailUser: String): Boolean {
        var isSuccess = false
        val anyTensionExercises = questions["What types of calisthenics exercises do you focus on or want to focus on?"]?.any { it == "Tension exercises" } ?: false
        val trainingRoutinesTens = if(anyTensionExercises) implementCalisthenicTensionRoutine(questions)
        else null
        var message = makeMessageFromAnsweredQuestions(questions, trainingRoutinesTens)
        val request = ChatRequest(
            messages = listOf(
                ChatMessage(role = "user", content = message)
            )
        )
        val response = api.getChatResponse("Bearer $apiKey", request)
        val receivedMessage = response.choices[0].message.content
        val json = receivedMessage.substringAfter("```json").substringBefore("```")
        val trainingRoutines = Gson().fromJson<Routines>(json, object : TypeToken<Routines>() {}.type).trainingRoutines.toMutableList()

        isSuccess = suspendCoroutine<Boolean> { continuation ->
            db.collection("Users").whereEqualTo("email", emailUser).get().addOnSuccessListener { result ->
                result.documents[0].reference.update("trainingRoutines", trainingRoutines)
                continuation.resume(true)
            }
        }

        return isSuccess
    }

    private suspend fun implementCalisthenicTensionRoutine(questions: Map<String, List<String>>): List<TrainingRoutine> {
        val dayOfWeekForTraining =
            questions["Which days of the week can/do you want to train?"]?.map { dayOfWeek ->
                DayOfWeek.fromString(dayOfWeek)
            } ?: emptyList()
        val daysForTensTraining = getNumberOfDays(
            questions["What types of calisthenics exercises do you focus on or want to focus on?"]
                ?: emptyList(),
            questions["What types of gym exercises do you focus on or want to focus on?"]
                ?: emptyList(),
            dayOfWeekForTraining.size
        )
        val isNoob = questions["How long have you been training?"]?.any { it != "I've been training for a year or more" } ?: false
        val trainingRoutineOneTrainingDay = TrainingRoutine(
            dayOfWeekForTraining[0], "Exercises calisthenic tens", listOf(
            RealizationExercise(ExercisesName.COMBOS_TENSION, 0, 0, 0),
            RealizationExercise(ExercisesName.PRESS_PLANK, 3, 5, 2),
            RealizationExercise(ExercisesName.PUSH_UP_PLANK, 3, 5, 2),
            RealizationExercise(ExercisesName.PULL_UP_FRONT_LEVER, 3, 5, 2),
            RealizationExercise(ExercisesName.PRESS_FRONT_LEVER, 3, 5, 2)
            )
        )
        val trainingRoutineTwoTrainingDays = listOf(
            TrainingRoutine(
                dayOfWeekForTraining[0], "Exercises calisthenic tens", listOf(
                    RealizationExercise(ExercisesName.COMBOS_TENSION, 0, 0, 0),
                    RealizationExercise(ExercisesName.PRESS_PLANK, 3, 5, 2),
                    RealizationExercise(ExercisesName.PUSH_UP_PLANK, 3, 5, 2),
                    if(isNoob) RealizationExercise(ExercisesName.HANDSTAND_PUSH_UP, 3, 5, 2) else RealizationExercise(ExercisesName.MALTESE_PLANK_PUSH_UP, 3, 5, 2)
                )
            ),
            TrainingRoutine(
                dayOfWeekForTraining[1], "Exercises calisthenic tens", listOf(
                    RealizationExercise(ExercisesName.COMBOS_TENSION, 3, 5, 2),
                    RealizationExercise(ExercisesName.PRESS_FRONT_LEVER, 3, 5, 2),
                    RealizationExercise(ExercisesName.PULL_UP_FRONT_LEVER, 3, 5, 2),
                    if(isNoob) RealizationExercise(ExercisesName.PULL_UP_HOLDING_UP, 3, 5, 2) else RealizationExercise(ExercisesName.MALTESE_FRONT_LEVER_PRESS, 3, 5, 2)
                )
            )
        )

        return when (daysForTensTraining) {
            0 -> emptyList<TrainingRoutine>()
            1 -> listOf(trainingRoutineOneTrainingDay)
            else -> trainingRoutineTwoTrainingDays
        }
    }

    private suspend fun makeMessageForUpdateRoutine(oldAnsweredQuestions: Map<String, List<String>>, question: String, newResponses: List<String>, isCalisthenicsTension: Boolean, actualRoutine: List<TrainingRoutine>, routineWithTension: List<TrainingRoutine>, exercisesName: List<ExercisesName>): String {
        var newResponsesWithoutTension = newResponses.toMutableList().apply { remove("Tension exercises") }

        if(isCalisthenicsTension) {
            return "Con estas preguntas $oldAnsweredQuestions me has dado esta rutina $actualRoutine. La pregunta $question ha cambiado de respuesta " +
                    "a $newResponsesWithoutTension. Creame un json cambiandome la antigua rutina, adaptandola a las nuevas respuestas y juntala " +
                    "junto con esta rutina  $routineWithTension, que sea serializable para estas clases data class TrainingRoutine(\\n\" +\n" +
                    "                    \"    val dayOfWeek: DayOfWeek,\\n\" +\n" +
                    "                    \"    val name: String,\\n\" +\n" +
                    "                    \"    val exercises: List<RealizationExercise>\\n\" +\n" +
                    "                    \"), donde cada ejercicio data class RealizationExercise(\\n\" +\n" +
                    "                    \"    val exercise: ExercisesName,\\n\" +\n" +
                    "                    \"    val series: Int,\\n\" +\n" +
                    "                    \"    val repetitions: Int,\\n\" +\n" +
                    "                    \"    val restBetweenSeries: Int\\n\" +\n" +
                    "                    \") y donde ExerciseName pueda coger los siguientes valores $exercisesName"
        } else {
            return "Con estas preguntas $oldAnsweredQuestions me has dado esta rutina $actualRoutine. La pregunta $question ha cambiado de " +
                    "respuesta a $newResponsesWithoutTension. Creame un json dandome una nueva rutina cambiando la actual solo modificando " +
                    "lo necesario para que se cumpla la nueva de respuesta del usuario, que sea serializable para estas clases " +
                    "data class TrainingRoutine(\\n\" +\n" +
                    "                    \"    val dayOfWeek: DayOfWeek,\\n\" +\n" +
                    "                    \"    val name: String,\\n\" +\n" +
                    "                    \"    val exercises: List<RealizationExercise>\\n\" +\n" +
                    "                    \"), donde cada ejercicio data class RealizationExercise(\\n\" +\n" +
                    "                    \"    val exercise: ExercisesName,\\n\" +\n" +
                    "                    \"    val series: Int,\\n\" +\n" +
                    "                    \"    val repetitions: Int,\\n\" +\n" +
                    "                    \"    val restBetweenSeries: Int\\n\" +\n" +
                    "                    \") y donde ExerciseName pueda coger los siguientes valores $exercisesName"
        }
    }

    /*private suspend fun makeQuestionsForRequest(questions: Map<String, List<String>>): Map<String, List<String>> {
        val newQuestions = questions.toMutableMap()
        val calisthenicQuestionsResponses = questions["What types of calisthenics exercises do you focus on or want to focus on?"] ?: emptyList()
        val responsesDaysOfWeek = questions["Which days of the week can/do you want to train?"] ?: emptyList()
        val numberOfDaysForTraining = responsesDaysOfWeek.size

        newQuestions["What types of calisthenics exercises do you focus on or want to focus on?"] = calisthenicQuestionsResponses.filter { it != "Tension exercises" }
        newQuestions["Which days of the week can/do you want to train?"] = if(numberOfDaysForTraining < 4) responsesDaysOfWeek.toMutableList().apply { removeAt(0) } else responsesDaysOfWeek.toMutableList().apply {
            removeAt(1)
            removeAt(0)
        }

        return newQuestions
    }
*/
    private suspend fun makeMessageFromAnsweredQuestions(answeredQuestions: Map<String, List<String>>, trainingRoutineTens: List<TrainingRoutine>?): String {
        var stringQuestions = ""
        var stringResponses = ""
        var trainingRoutineTensString = trainingRoutineTens?.toString() ?: ""
        var exercisesForSend = makeExercisesListForSend(answeredQuestions)

        answeredQuestions.filterValues { it.isNotEmpty() }.forEach { (question, responses) ->
            stringResponses = responses.joinToString("\n")
            stringQuestions += "$question\n$stringResponses\n"
        }

        if(trainingRoutineTens != null) {
            return "Hazme un json de rutinas de entrenamiento por dia en base a estas preguntas respondidas.\n $answeredQuestions. La lista de rutinas deber ser serializable para esta clase data class RoutinesResponse(\n" +
                    "    val routines: List<TrainingRoutine>\n" +
                    ") Cada rutina de cada dia debe ser serializable para esta clase data class TrainingRoutine(\n" +
                    "    val dayOfWeek: DayOfWeek,\n" +
                    "    val name: String,\n" +
                    "    val exercises: List<RealizationExercise>\n" +
                    ") y donde cada ejercicio data class RealizationExercise(\n" +
                    "    val exercise: ExercisesName,\n" +
                    "    val series: Int,\n" +
                    "    val repetitions: Int,\n" +
                    "    val restBetweenSeries: Int\n" +
                    "). La propiedad exercise debe coger los siguientes valores $exercisesForSend. Se tiene que ejercitar todos los musculos entre los dias de la semana dado." +
                    " E implementa la rutina creada a esta rutina " +
                    "ya hecha para despues juntar las dos, sin cambiar la que te he pasado $trainingRoutineTensString"
        } else {
            return "Hazme un json de rutinas de entrenamiento por dia en base a estas preguntas respondidas.\n $answeredQuestions. La lista de rutinas deber ser serializable para esta clase data class RoutinesResponse(\n" +
                    "    val routines: List<TrainingRoutine>\n" +
                    ") Cada rutina de cada dia debe ser serializable para esta clase data class TrainingRoutine(\n" +
                    "    val dayOfWeek: DayOfWeek,\n" +
                    "    val name: String,\n" +
                    "    val exercises: List<RealizationExercise>\n" +
                    ") y donde cada ejercicio @Serializable\n" +
                    "data class RealizationExercise(\n" +
                    "    val exercise: ExercisesName,\n" +
                    "    val series: Int,\n" +
                    "    val repetitions: Int,\n" +
                    "    val restBetweenSeries: Int\n" +
                    "). La propiedad exercise debe coger los siguientes valores $exercisesForSend. Se tiene que ejercitar todos los musculos entre los dias de la semana dado."
        }
    }

    private suspend fun makeExercisesListForSend(answeredQuestions: Map<String, List<String>>): List<ExercisesName> {
        val responsesChooseTraining = answeredQuestions["Are you more into calisthenics or gym workouts?"]
        val chooseCalisthenicsExercisesForTraining = answeredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"]
        var exercisesList = mutableStateListOf<ExercisesName>()
        val chooseGymExercisesForTraining = answeredQuestions["What types of gym exercises do you focus on or want to focus on?"]
        val isBasicExercises = chooseCalisthenicsExercisesForTraining?.any { it == "Basic exercises" } ?: false
        val isMachinesExercises = chooseGymExercisesForTraining?.any { it == "Machine exercises" } ?: false
        val isWeightliftingExercises = chooseGymExercisesForTraining?.any { it == "Weightlifting exercises" } ?: false
        val allExercises = ExercisesName.entries

        when(responsesChooseTraining!![0]) {
            "Calisthenics" -> if(isBasicExercises) exercisesList.addAll(allExercises.subList(22, 39))
            "Gym" -> {
                if(isMachinesExercises) exercisesList.addAll(allExercises.subList(0, 22))
                if(isWeightliftingExercises) exercisesList.addAll(allExercises.subList(39, 73))
            }
            "Both" -> {
                if(isMachinesExercises) exercisesList.addAll(allExercises.subList(0, 22))
                if(isWeightliftingExercises) exercisesList.addAll(allExercises.subList(39, 73))
                if(isBasicExercises) exercisesList.addAll(allExercises.subList(22, 39))
            }
        }

        return exercisesList
    }

    private suspend fun getNumberOfDays(responsesCalisthenic: List<String>, responsesGym: List<String>, daysForTraining: Int): Int = coroutineScope {
        var daysForTrainingNotTens = 0

        responsesCalisthenic.forEach { if(it == "Basic exercises") daysForTrainingNotTens++ }
        responsesGym.forEach { if(it == "Machine exercises") daysForTrainingNotTens++ }
        responsesGym.forEach { if(it == "Weightlifting exercises") daysForTrainingNotTens++ }

        return@coroutineScope daysForTraining - daysForTrainingNotTens
    }

    private suspend fun makeMapWithDifferencesOfTwoMaps(oldAnsweredQuestions: Map<String, List<String>>, newAnsweredQuestion: Map<String, List<String>>): Map<String, List<String>> {
        var oldAnsweredExercisesTypeQuestions = filterMapForTheExercisesTypeQuestions(oldAnsweredQuestions)
        var newAnsweredExercisesTypeQuestions = filterMapForTheExercisesTypeQuestions(newAnsweredQuestion)
        var differentMap = mutableMapOf<String, List<String>>()

        oldAnsweredExercisesTypeQuestions.forEach { (question, selectedResponses) ->
            newAnsweredExercisesTypeQuestions[question]?.forEach {
                if(!selectedResponses.contains(it)) {
                    if(!differentMap.contains(question)) differentMap.put(question, emptyList())
                    differentMap[question] = differentMap[question]?.toMutableList().apply { this!!.add(it) } ?: emptyList<String>()
                }
            } ?: differentMap.put(question, emptyList())
        }

        differentMap.put("Are you more into calisthenics or gym workouts?", newAnsweredQuestion["Are you more into calisthenics or gym workouts?"] ?: emptyList())
        return differentMap
    }

    private suspend fun filterMapForTheExercisesTypeQuestions(answeredQuestions: Map<String, List<String>>): Map<String, List<String>> {
       return answeredQuestions.filterKeys {
                    it == "What types of gym exercises do you focus on or want to focus on?" ||
                    it == "What types of calisthenics exercises do you focus on or want to focus on?"
        }
    }

    private suspend fun getAddedExerciseType(oldAnsweredQuestions: Map<String, List<String>>, newAnsweredQuestion: Map<String, List<String>>): List<String> {
        var oldAnsweredExercisesTypeQuestions = filterMapForTheExercisesTypeQuestions(oldAnsweredQuestions)
        var newAnsweredExercisesTypeQuestions = filterMapForTheExercisesTypeQuestions(newAnsweredQuestion)
        var addedExercisesType = mutableListOf<String>()

        oldAnsweredExercisesTypeQuestions.forEach { (question, selectedResponses) ->
            newAnsweredExercisesTypeQuestions[question]?.forEach {
                if(!selectedResponses.contains(it)) {
                    addedExercisesType.add(it)
                }
            }
        }

        return addedExercisesType
    }

    private suspend fun getRemovedExercisesType(oldAnsweredQuestions: Map<String, List<String>>, newAnsweredQuestion: Map<String, List<String>>): List<String> {
        var oldAnsweredExercisesTypeQuestions = filterMapForTheExercisesTypeQuestions(oldAnsweredQuestions)
        var newAnsweredExercisesTypeQuestions = filterMapForTheExercisesTypeQuestions(newAnsweredQuestion)
        var removedExercisesType = mutableListOf<String>()

        newAnsweredExercisesTypeQuestions.forEach { (question, newSelectedResponses) ->
            oldAnsweredExercisesTypeQuestions[question]?.forEach {
                if(!newSelectedResponses.contains(it)) {
                    //if(!differentMap.contains(question)) differentMap.put(question, emptyList())
                    removedExercisesType.add(it)
                }
            }
        }

        return removedExercisesType
    }

    private suspend fun removedDaysRoutines(removedExercisesType: List<String>, actualRoutine: List<TrainingRoutine>): List<TrainingRoutine> {
        val newActualRoutine = actualRoutine.toMutableList()

        actualRoutine.forEach { trainingRoutine ->
             trainingRoutine.exercises = trainingRoutine.exercises.filter { realizationExercise ->
                removedExercisesType.any { realizationExercise.exercise.exercise.type.nameType == it }
            }
        }

        return actualRoutine
    }

    private suspend fun implementRoutine(newAnsweredQuestion: Map<String, List<String>>, newRoutines: List<TrainingRoutine>, exerciseTypeForAdd: List<String>) {
        val daysForTrain = newAnsweredQuestion["Which days of the week can/do you want to train?"]?.count() ?: 0
        val emptyDays = newRoutines.filter { it.exercises.isEmpty() || it.exercises.size < 5 }
        val trainingMuscles = newRoutines.flatMap { trainingRoutine ->
            trainingRoutine.exercises.map { it.exercise.exercise.trainedPrimaryMuscles }
        }.flatten()
        val musclesForTrain = Muscles.entries.filter { muscle ->
            trainingMuscles.any { muscle == it }
        }

        if(daysForTrain == exerciseTypeForAdd.size)
            exerciseTypeForAdd.forEach {
                when(it) {
                    TypeExercise.BASIC.nameType -> {} //getCalisthenicBasicRoutine()
                    TypeExercise.WEIGHTLIFTING.nameType -> {} //getWeightliftingRoutine()
                    TypeExercise.MACHINES.nameType -> {}//getMachinesExercisesRoutine()
                }
            }
    }

    private suspend fun getCalisthenicBasicRoutine(musclesForTrain: List<Muscles>, daysForTrain: Int, daysLeftForTrain: Int, daysOfWeekForTrain: List<DayOfWeek>, goal: String) {
        val isDayPullUp = musclesForTrain.any { it == Muscles.LATISSIMUS_DORSI || it == Muscles.TRICEPS || Muscles.TRAPEZIUS == it }
        val isDayPushUp = musclesForTrain.any { it == Muscles.BICEPS ||it == Muscles.PECTORALS || it == Muscles.DELTOIDS }
        val isDayCore = musclesForTrain.any { it == Muscles.ABS }
        val isDayLegs = musclesForTrain.any { it == Muscles.ADDUCTORS || it == Muscles.CALVES || it == Muscles.GLUTEUS || it == Muscles.QUADRICEPS }
        val exercisesPullUp = listOf(ExercisesName.PULL_UPS, ExercisesName.NEGATIVE_PULL_UPS,
            ExercisesName.CHIN_UPS, ExercisesName.PULL_UP_HOLDING_UP)
        val exercisesPushUp = listOf(ExercisesName.PUSH_UPS, ExercisesName.CLOSED_GRIP_PUSH_UPS,
            ExercisesName.CHIN_UPS, ExercisesName.DIPS)
        val exercisesCore = listOf(ExercisesName.CRUNCHES, ExercisesName.PLANK, ExercisesName.CRUNCHES,
            ExercisesName.RUSSIAN_TWISTS, ExercisesName.MOUNTAIN_CLIMBERS)
        val exercisesLegs = listOf(ExercisesName.SQUATS, ExercisesName.JUMP_SQUATS, ExercisesName.LUNGES,
            ExercisesName.CALF_RAISES, ExercisesName.STEP_UPS)
        val numberRepetitions = when(goal) {
            "Gain more strength" -> 6
            "Increase endurance" -> 20
            "Build more muscle" -> 12
            else -> 0
        }
        val restBetweenSeries = when(goal) {
            "Gain more strength", "Build more muscle" -> 2
            "Increase endurance" -> 1
            else -> 0
        }
        val realizationExercises = if(isDayPullUp)
            exercisesToRealizationExercise(exercisesPullUp, numberRepetitions, restBetweenSeries) else
                if(isDayPushUp) exercisesToRealizationExercise(exercisesPushUp, numberRepetitions, restBetweenSeries) else
                    if(isDayCore) exercisesToRealizationExercise(exercisesCore, numberRepetitions, restBetweenSeries) else
                        exercisesToRealizationExercise(exercisesLegs, numberRepetitions, restBetweenSeries)

        when(daysForTrain) {
            1 -> TrainingRoutine(daysOfWeekForTrain[0],
                if(isDayPullUp) "Pull-up day" else if(isDayPushUp) "Push-up day" else "Calisthenic basic day",
                realizationExercises
            )
        }
    }

    private suspend fun exercisesToRealizationExercise(exercises: List<ExercisesName>, repetitions: Int, restBetweenSeries: Int): List<RealizationExercise> {
       return exercises.map { RealizationExercise(it, 3, repetitions, restBetweenSeries) }
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override suspend fun createRoutine(answeredQuestions: Map<String, List<String>>) {
        var numberOfDaysForTraining = answeredQuestions["Which days of the week can/do you want to train?"]?.count() ?: 0
        val numberOfTypeExercises = (answeredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.count() ?: 0) + (answeredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"]?.count() ?: 0)
        val typeExercisesGym = answeredQuestions["What types of gym exercises do you focus on or want to focus on?"] ?: emptyList()
        val typeExercisesCalisthenics = answeredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"] ?: emptyList()
        val allTypeExercisesForTrain = typeExercisesGym.toMutableList().apply { addAll(typeExercisesCalisthenics) }
        val allTypeDuplicatesExercises = getAllTypeExercises(allTypeExercisesForTrain, numberOfDaysForTraining, numberOfTypeExercises).toMutableList()
        var weeklyRoutines = mutableListOf<TrainingRoutine>()
        var numberTrainingDays = 4
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
        var c = 9

        if(numberOfDaysForTraining == 5) {
            excludedExercises = mutableListOf(
                ExercisesName.DEADLIFT,
                ExercisesName.CONCENTRATION_CURL
            )

            allTypeDuplicatesExercises.forEach {
                addRoutineAccordingType(it, numberTrainingDays, weeklyRoutines, daysOfWeekForTrain.first(), repetitions, restBetweenSeries)
                daysOfWeekForTrain.removeAt(0)
                numberTrainingDays -= 1
            }
            addRoutine(weeklyRoutines, daysOfWeekForTrain.first(), listOf(ExercisesName.RUNNING), 0, 0, 0, "Running day")
            removeExercisesOfRoutine(weeklyRoutines, excludedExercises)
        } else if(numberOfDaysForTraining == 4) {
            excludedExercises = mutableListOf(
                ExercisesName.DEADLIFT,
                ExercisesName.CONCENTRATION_CURL
            )

            allTypeDuplicatesExercises.forEach {
                addRoutineAccordingType(it, numberTrainingDays, weeklyRoutines, daysOfWeekForTrain.first(), repetitions, restBetweenSeries)
                daysOfWeekForTrain.removeAt(0)
                numberTrainingDays -= 1
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
                4,
                { daysOfWeekForTrain.size != 1 }
            )
            /*for(numberOfTrainingDay in 4 downTo 0) {//El bucle se repite 4 veces porque es el numero de movimimiento que se hace, donde cada movimiento ejercita un conjunto de musculos
                actualTypeExercise = if(allTypeDuplicatesExercises.isNotEmpty()) allTypeDuplicatesExercises.removeAt(0) else actualTypeExercise
                addRoutineAccordingType(actualTypeExercise, numberOfTrainingDay, weeklyRoutines, daysOfWeekForTrain.first(), repetitions, restBetweenSeries)
                if(daysOfWeekForTrain.size != 1) daysOfWeekForTrain.removeAt(0)
            }*/

            joinListsWithSameDayOfWeek(weeklyRoutines)

            removeExercisesOfRoutine(weeklyRoutines, excludedExercises)
        } else if(numberOfDaysForTraining == 2) {
            excludedExercises.addAll(listOf(ExercisesName.TRICEP_EXTENSION_MACHINE, ExercisesName.DEADLIFT,
                ExercisesName.ONE_ARM_DUMBBELL_ROW))
            /*for(numberOfTrainingDay in 4 downTo 0 step 2) {//El bucle se repite 4 veces porque es el numero de movimimiento que se hace, donde cada movimiento ejercita un conjunto de musculos
                actualTypeExercise = if(allTypeDuplicatesExercises.isNotEmpty()) allTypeDuplicatesExercises.removeAt(0) else actualTypeExercise
                addRoutineAccordingType(actualTypeExercise, numberOfTrainingDay, weeklyRoutines, daysOfWeekForTrain.first(), repetitions, restBetweenSeries)
            }*/
            addRoutineOfLowDays(
                weeklyRoutines,
                daysOfWeekForTrain,
                mutableListOf(allTypeDuplicatesExercises.first()),
                repetitions,
                restBetweenSeries,
                2,
                4,
                { false }
            )

            daysOfWeekForTrain.removeAt(0)

            addRoutineOfLowDays(
                weeklyRoutines,
                daysOfWeekForTrain,
                mutableListOf(allTypeDuplicatesExercises.last()),
                repetitions,
                restBetweenSeries,
                2,
                3,
                { false }
            )
            /*for(numberOfTrainingDay in 3 downTo 0 step 2) {//El bucle se repite 4 veces porque es el numero de movimimiento que se hace, donde cada movimiento ejercita un conjunto de musculos
                actualTypeExercise = if(allTypeDuplicatesExercises.isNotEmpty()) allTypeDuplicatesExercises.removeAt(0) else actualTypeExercise
                addRoutineAccordingType(actualTypeExercise, numberOfTrainingDay, weeklyRoutines, daysOfWeekForTrain.first(), repetitions, restBetweenSeries)
            }*/

            joinListsWithSameDayOfWeek(weeklyRoutines)

            removeExercisesOfRoutine(weeklyRoutines, excludedExercises)
        } else {
            excludedExercises.addAll(listOf(ExercisesName.FLAT_BARBELL_BENCH_PRESS, ExercisesName.HAMMER_CURL_WITH_DUMBBELLS,
                ExercisesName.RUSSIAN_TWIST_WITH_DUMBBELL_OR_PLATE, ExercisesName.LEG_PRESS_MACHINE))

            addRoutineFullBody(if(typeExercisesCalisthenics.isNotEmpty()) typeExercisesCalisthenics[0] else typeExercisesGym[0], weeklyRoutines, daysOfWeekForTrain.first(), repetitions, restBetweenSeries)

            removeExercisesOfRoutine(weeklyRoutines, excludedExercises)
        }
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

    private suspend fun addRoutineAccordingType(typeExercise: String, numberOfDaysForTraining: Int, weeklyRoutines: MutableList<TrainingRoutine>, dayOfWeekForTrain: String, repetitions: Int, restBetweenSeries: Int) {
        when(typeExercise) {
            TypeExercise.MACHINES.nameType -> {
                when(numberOfDaysForTraining) {
                    4 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineMachineExercisesPushUp, 3, repetitions, restBetweenSeries, "Push day")
                    3 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineMachineExercisesPullUp, 3, repetitions, restBetweenSeries, "Pull day")
                    2 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineMachineExercisesLegs, 3, repetitions, restBetweenSeries, "Legs day")
                    1 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineMachineExercisesCore, 3, repetitions, restBetweenSeries, "Core day")
                }
            }
            TypeExercise.WEIGHTLIFTING.nameType -> {
                when(numberOfDaysForTraining) {
                    4 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineWeightliftingPushUp, 3, repetitions, restBetweenSeries, "Push day")
                    3 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineWeightliftingPullUp, 3, repetitions, restBetweenSeries, "Pull day")
                    2 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineWeightliftingLegs, 3, repetitions, restBetweenSeries, "Legs day")
                    1 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineWeightliftingCore, 3, repetitions, restBetweenSeries, "Core day")
                }
            }
            TypeExercise.BASIC.nameType -> {
                when(numberOfDaysForTraining) {
                    4 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineBasicPushUp, 4, repetitions, restBetweenSeries, "Push day")
                    3 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineBasicPullUp, 4, repetitions, restBetweenSeries, "Pull day")
                    2 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineBasicLegs, 4, repetitions, restBetweenSeries, "Legs day")
                    1 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineBasicCore, 4, repetitions, restBetweenSeries, "Core day")
                }
            }
            TypeExercise.TENS.nameType -> {
                when(numberOfDaysForTraining) {
                    4 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineTensionPushUp, 4, repetitions, restBetweenSeries, "Push day")
                    3 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineTensionPullUp, 4, repetitions, restBetweenSeries, "Pull day")
                    2 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineBasicLegs, 4, repetitions, restBetweenSeries, "Legs day")
                    1 -> addRoutine(weeklyRoutines, dayOfWeekForTrain, allRoutines.routineBasicCore, 4, repetitions, restBetweenSeries, "Core day")
                }
            }
        }
    }

    private suspend fun addRoutineWithTwoDaysForTraining(typeExercise: String, numberOfDaysForTraining: Int, weeklyRoutines: MutableList<TrainingRoutine>, dayOfWeekForTrain: String, repetitions: Int, restBetweenSeries: Int): Unit =
        when(typeExercise) {
            TypeExercise.MACHINES.nameType -> {
                when(numberOfDaysForTraining) {
                    4 -> addRoutine(
                        weeklyRoutines,
                        dayOfWeekForTrain,
                        joinToList(allRoutines.routineMachineExercisesPushUp, allRoutines.routineMachineExercisesCore, listOf(
                        ExercisesName.PECK_DECK_MACHINE, ExercisesName.BACK_EXTENSION_MACHINE)),
                        3,
                        repetitions,
                        restBetweenSeries,
                        "Push-core day"
                    )
                    2 -> addRoutine(
                        weeklyRoutines,
                        dayOfWeekForTrain,
                        joinToList(allRoutines.routineMachineExercisesPullUp, allRoutines.routineMachineExercisesLegs, listOf(
                            ExercisesName.BACK_EXTENSION_MACHINE, ExercisesName.CABLE_PULL_THROUGH)),
                        3,
                        repetitions,
                        restBetweenSeries,
                        "Pull-legs day"
                    )

                    else -> {}
                }
            }
            TypeExercise.WEIGHTLIFTING.nameType -> {
                when(numberOfDaysForTraining) {
                    4 -> addRoutine(
                        weeklyRoutines,
                        dayOfWeekForTrain,
                        joinToList(allRoutines.routineWeightliftingPushUp, allRoutines.routineWeightliftingCore, listOf(
                            ExercisesName.FRENCH_PRESS_WITH_BARBELL_OR_DUMBBELLS, ExercisesName.OVERHEAD_TRICEPS_EXTENSION_WITH_DUMBBELL)),
                        3,
                        repetitions,
                        restBetweenSeries,
                        "Push-core day"
                    )
                    2 -> addRoutine(
                        weeklyRoutines,
                        dayOfWeekForTrain,
                        joinToList(allRoutines.routineWeightliftingPullUp, allRoutines.routineWeightliftingLegs, listOf(
                            ExercisesName.BACK_EXTENSION_MACHINE, ExercisesName.BARBELL_CURL,
                            ExercisesName.DEADLIFT)),
                        3,
                        repetitions,
                        restBetweenSeries,
                        "Pull-legs day"
                    )

                    else -> {}
                }
            }
            TypeExercise.BASIC.nameType -> {
                when(numberOfDaysForTraining) {
                    4 -> addRoutine(
                        weeklyRoutines,
                        dayOfWeekForTrain,
                        joinToList(allRoutines.routineBasicPushUp, allRoutines.routineBasicCore, emptyList()),
                        3,
                        repetitions,
                        restBetweenSeries,
                        "Push-core day"
                    )
                    2 -> addRoutine(
                        weeklyRoutines,
                        dayOfWeekForTrain,
                        joinToList(allRoutines.routineBasicPullUp, allRoutines.routineBasicLegs, emptyList()),
                        3,
                        repetitions,
                        restBetweenSeries,
                        "Pull-legs day"
                    )

                    else -> {}
                }
            }
            TypeExercise.TENS.nameType -> {
                when(numberOfDaysForTraining) {
                    4 -> addRoutine(
                        weeklyRoutines,
                        dayOfWeekForTrain,
                        joinToList(allRoutines.routineTensionPushUp, allRoutines.routineBasicCore, emptyList()),
                        3,
                        repetitions,
                        restBetweenSeries,
                        "Push-core day"
                    )
                    2 -> addRoutine(
                        weeklyRoutines,
                        dayOfWeekForTrain,
                        joinToList(allRoutines.routineTensionPullUp, allRoutines.routineBasicLegs, emptyList()),
                        3,
                        repetitions,
                        restBetweenSeries,
                        "Pull-legs day"
                    )

                    else -> {}
                }
            }

            else -> {}
        }

    private suspend fun addRoutineFullBody(typeExercise: String, weeklyRoutines: MutableList<TrainingRoutine>, dayOfWeekForTrain: String, repetitions: Int, restBetweenSeries: Int) {
        val routineAllExercises: List<ExercisesName>
        
        
        when(typeExercise) {
            TypeExercise.MACHINES.nameType -> {
                routineAllExercises = allRoutines.routineMachineExercisesPushUp.subList(3, 6) + (allRoutines.routineMachineExercisesPullUp - listOf(ExercisesName.BACK_EXTENSION_MACHINE,
                    ExercisesName.LAT_PULLDOWN_MACHINE)) + (allRoutines.routineMachineExercisesCore - ExercisesName.BACK_EXTENSION_MACHINE) + (allRoutines.routineMachineExercisesLegs - ExercisesName.CABLE_PULL_THROUGH)
            }
            TypeExercise.WEIGHTLIFTING.nameType -> {
                routineAllExercises = (allRoutines.routineWeightliftingPushUp - listOf(ExercisesName.FRONT_RAISES_WITH_DUMBBELLS_OR_BARBELL, ExercisesName.FRENCH_PRESS_WITH_BARBELL_OR_DUMBBELLS, ExercisesName.OVERHEAD_TRICEPS_EXTENSION_WITH_DUMBBELL)) + (allRoutines.routineWeightliftingPullUp - listOf(ExercisesName.ONE_ARM_DUMBBELL_ROW,
                    ExercisesName.DEADLIFT, ExercisesName.BARBELL_CURL, ExercisesName.CONCENTRATION_CURL)) + (allRoutines.routineWeightliftingCore - listOf(ExercisesName.AB_CRUNCH_MACHINE, ExercisesName.CRUNCHES)) + (allRoutines.routineWeightliftingLegs - listOf(ExercisesName.CABLE_PULL_THROUGH, ExercisesName.LEG_EXTENSION_MACHINE))
            }
            TypeExercise.BASIC.nameType -> {
                routineAllExercises = allRoutines.routineBasicPushUp + allRoutines.routineBasicPullUp + allRoutines.routineBasicLegs + allRoutines.routineBasicCore
            }
            TypeExercise.TENS.nameType -> {
                routineAllExercises = allRoutines.routineTensionPushUp + allRoutines.routineTensionPullUp + allRoutines.routineBasicLegs + allRoutines.routineBasicCore
            }
            else -> routineAllExercises = emptyList()
        }

        addRoutine(weeklyRoutines, dayOfWeekForTrain, routineAllExercises, 3, repetitions, restBetweenSeries, "Full-body day")
    }

    private suspend fun joinToList(exercises1: List<ExercisesName>, exercises2: List<ExercisesName>, excludedExercises: List<ExercisesName>): List<ExercisesName> {
        val newExercises = exercises1.toMutableList().apply { addAll(exercises2) }

        return newExercises.filterNot { it in excludedExercises }
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

    private suspend fun addRoutineOfLowDays(weeklyRoutines: MutableList<TrainingRoutine>, daysOfWeekForTrain: MutableList<String>, allTypeDuplicatesExercises: MutableList<String>, repetitions: Int, restBetweenSeries: Int, steps: Int, idMusclesForTraining: Int, isStop: () -> Boolean) {
        var actualTypeExercise: String = ""

        for(numberOfTrainingDay in idMusclesForTraining downTo 0 step steps) {//El bucle se repite 4 veces porque es el numero de movimimiento que se hace, donde cada movimiento ejercita un conjunto de musculos
            actualTypeExercise = if(allTypeDuplicatesExercises.isNotEmpty()) allTypeDuplicatesExercises.removeAt(0) else actualTypeExercise
            addRoutineAccordingType(actualTypeExercise, numberOfTrainingDay, weeklyRoutines, daysOfWeekForTrain.first(), repetitions, restBetweenSeries)
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




