package com.example.proyectoappgym.db.db_users

import android.R.attr.apiKey
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.LinkAnnotation
import androidx.core.text.util.LocalePreferences
import coil.util.CoilUtils.result
import com.example.proyectoappgym.db.retrofit.entity.ChatMessage
import com.example.proyectoappgym.db.retrofit.entity.ChatRequest
import com.example.proyectoappgym.db.retrofit.entity.OpenAiApi
import com.example.proyectoappgym.db.retrofit.entity.Routines
import com.example.proyectoappgym.entity.DayOfWeek
import com.example.proyectoappgym.entity.Exercise2
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.TensExercise
import com.example.proyectoappgym.entity.TrainingRoutine
import com.example.proyectoappgym.entity.TypeExercise
import com.example.proyectoappgym.entity.TypeTensExercise
import com.example.proyectoappgym.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.asDeferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.map
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
                   var c = it.message
                   continuation.resume(null)
               }
        }

        return suspendCoroutine { continuation ->
            if(userFirebase != null) {
                db.collection("Users").document(userFirebase.uid).set(user)
                    .addOnSuccessListener {
                        continuation.resume(true) //2 es true
                    }.addOnFailureListener {
                        var c = it.message
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

    private fun convertUserToMap(user: User) {
        linkedMapOf(
            "name" to user.name,
            "username" to user.username,
            "email" to user.email,
            "password" to user.password,
            "birthdate" to user.birthdate,
            "gender" to user.gender.toString(),
            "allQuestionsWithAnswered" to user.allQuestionsAnswered
        )
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

    override suspend fun updateAvatarProfile(newAvatar: Int) {
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
                .addOnSuccessListener {
                    var c = 1
                }.addOnFailureListener {
                    var c = it.message
                }
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

    override suspend fun saveUserTrainingRoutinesGpt(questions: Map<String, List<String>>, emailUser: String): Boolean {
        var isSuccess = false
        val anyTensionExercises = questions["What types of calisthenics exercises do you focus on or want to focus on?"]?.any { it == "Tension exercises" } ?: false
        val trainingRoutinesTens = if(anyTensionExercises) implementCalisthenicRoutine(questions)
        else null
        var message = buildFromAnsweredQuestions(questions, trainingRoutinesTens)
        val request = ChatRequest(
            messages = listOf(
                ChatMessage(role = "user", content = message)
            )
        )
        val response = api.getChatResponse("Bearer $apiKey", request)
        val receivedMessage = response.choices[0].message.content
        val json = receivedMessage.substringAfter("```json").substringBefore("```")
        val trainingRoutines = Gson().fromJson<Routines>(json, object : TypeToken<Routines>() {}.type).trainingRoutines

        isSuccess = suspendCoroutine<Boolean> { continuation ->
            db.collection("Users").whereEqualTo("email", emailUser).get().addOnSuccessListener { result ->
                result.documents[0].reference.update("trainingRoutines", trainingRoutines)
                continuation.resume(true)
            }
        }

        return isSuccess
    }

    private suspend fun implementCalisthenicRoutine(questions: Map<String, List<String>>): List<TrainingRoutine> {
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

        when (daysForTensTraining) {
            0 -> return emptyList<TrainingRoutine>()
            1 -> return listOf(
                TrainingRoutine(
                    dayOfWeekForTraining[0], "Exercises calisthenic tens", listOf(
                        Exercise2(
                            "Combinations of tension exercises",
                            "Tens exercise combos without getting off the parallel bar",
                            TypeExercise.TENS,
                            emptyList(),
                            3,
                            5,
                            2,
                            TypeTensExercise.ALL
                        ),
                        Exercise2(
                            "Press plank",
                            "Press plank: go up to handstand and return to plank position. Use a resistance band",
                            TypeExercise.TENS,
                            emptyList(),
                            3,
                            5,
                            2,
                            TypeTensExercise.PLANK
                        ),
                        Exercise2(
                            "Push-up plank",
                            "Push-up plank: lower as much as possible and return to plank. Use a resistance band",
                            TypeExercise.TENS,
                            emptyList(),
                            3,
                            5,
                            2,
                            TypeTensExercise.PLANK
                        ),
                        Exercise2(
                            "Front lever press",
                            "Front lever press: lift legs to touch the bar, then return to front lever. Use a resistance band",
                            TypeExercise.TENS,
                            emptyList(),
                            3,
                            5,
                            2,
                            TypeTensExercise.FRONT_LEVEL
                        ),
                        Exercise2(
                            "Front lever pull-up",
                            "Front lever pull-up: pull up to touch the bar, then return to front lever. Use a resistance band",
                            TypeExercise.TENS,
                            emptyList(),
                            3,
                            5,
                            2,
                            TypeTensExercise.FRONT_LEVEL
                        ),
                    )
                )
            )

            else -> return listOf(
                TrainingRoutine(
                    dayOfWeekForTraining[0], "Exercises calisthenic tens", listOf(
                        Exercise2(
                            "Combinations of tension exercises",
                            "Tens exercise combos without getting off the parallel bar",
                            TypeExercise.TENS,
                            emptyList(),
                            3,
                            5,
                            2,
                            TypeTensExercise.ALL
                        ),
                        Exercise2(
                            "Press plank",
                            "Press plank: go up to handstand and return to plank position. Use a resistance band",
                            TypeExercise.TENS,
                            emptyList(),
                            3,
                            5,
                            2,
                            TypeTensExercise.PLANK
                        ),
                        Exercise2(
                            "Push-up plank",
                            "Push-up plank: lower as much as possible and return to plank. Use a resistance band",
                            TypeExercise.TENS,
                            emptyList(),
                            3,
                            5,
                            2,
                            TypeTensExercise.PLANK
                        ),
                    )
                ),
                TrainingRoutine(
                    dayOfWeekForTraining[1], "Exercises calisthenic tens", listOf(
                        Exercise2(
                            "Combinations of tension exercises",
                            "Tens exercise combos without getting off the parallel bar",
                            TypeExercise.TENS,
                            emptyList(),
                            3,
                            5,
                            2,
                            TypeTensExercise.ALL
                        ),
                        Exercise2(
                            "Front lever press",
                            "Front lever press: lift legs to touch the bar, then return to front lever. Use a resistance band",
                            TypeExercise.TENS,
                            emptyList(),
                            3,
                            5,
                            2,
                            TypeTensExercise.FRONT_LEVEL
                        ),
                        Exercise2(
                            "Front lever pull-up",
                            "Front lever pull-up: pull up to touch the bar, then return to front lever. Use a resistance band",
                            TypeExercise.TENS,
                            emptyList(),
                            3,
                            5,
                            2,
                            TypeTensExercise.FRONT_LEVEL
                        ),
                    )
                )
            )
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
    private suspend fun buildFromAnsweredQuestions(answeredQuestions: Map<String, List<String>>, trainingRoutineTens: List<TrainingRoutine>?): String {
        var stringQuestions = ""
        var stringResponses = ""
        var trainingRoutineTensString = trainingRoutineTens?.toString() ?: ""

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
                    "    val exercises: List<Exercise>\n" +
                    ") y donde cada ejercicio data class Exercise(\n" +
                    "    val name: String,\n" +
                    "    val description: String,\n" +
                    "    val type: TypeExercise,\n" +
                    "    val trainedMuscles: List<String>,\n" +
                    "    val series: Int,\n" +
                    "    val repetitions: Int,\n" +
                    "    val restBetweenSeries: Int\n" +
                    "). La propiedad type debe coger los siguientes valores MACHINES, WEIGHTLIFTING, BASIC, CARDIO. E implementa la rutina creada a esta rutina ya hecha para despues juntar las dos, sin cambiar la que te he pasado $trainingRoutineTensString"
        } else {
            return "Hazme un json de rutinas de entrenamiento por dia en base a estas preguntas respondidas.\n $answeredQuestions. La lista de rutinas deber ser serializable para esta clase data class RoutinesResponse(\n" +
                    "    val routines: List<TrainingRoutine>\n" +
                    ") Cada rutina de cada dia debe ser serializable para esta clase data class TrainingRoutine(\n" +
                    "    val dayOfWeek: DayOfWeek,\n" +
                    "    val name: String,\n" +
                    "    val exercises: List<Exercise>\n" +
                    ") y donde cada ejercicio data class Exercise(\n" +
                    "    val name: String,\n" +
                    "    val description: String,\n" +
                    "    val type: TypeExercise,\n" +
                    "    val trainedMuscles: List<String>,\n" +
                    "    val series: Int,\n" +
                    "    val repetitions: Int,\n" +
                    "    val restBetweenSeries: Int\n" +
                    "). La propiedad type debe coger los siguientes valores MACHINES, WEIGHTLIFTING, BASIC, CARDIO"
        }
    }

    suspend fun getNumberOfDays(responsesCalisthenic: List<String>, responsesGym: List<String>, daysForTraining: Int): Int = coroutineScope {
        var daysForTrainingNotTens = 0

        responsesCalisthenic.forEach { if(it == "Basic exercises") daysForTrainingNotTens++ }
        responsesGym.forEach { if(it == "Machine exercises") daysForTrainingNotTens++ }
        responsesGym.forEach { if(it == "Weightlifting exercises") daysForTrainingNotTens++ }

        return@coroutineScope daysForTraining - daysForTrainingNotTens
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




