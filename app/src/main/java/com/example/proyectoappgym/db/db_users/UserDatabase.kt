package com.example.proyectoappgym.db.db_users

import android.R.attr.apiKey
import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.ui.text.LinkAnnotation
import coil.util.CoilUtils.result
import com.example.proyectoappgym.db.retrofit.entity.ChatMessage
import com.example.proyectoappgym.db.retrofit.entity.ChatRequest
import com.example.proyectoappgym.db.retrofit.entity.OpenAiApi
import com.example.proyectoappgym.entity.Question
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.asDeferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.collections.component1
import kotlin.collections.component2
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
                   continuation.resume(null)
               }
        }

        return suspendCoroutine { continuation ->
            if(userFirebase != null) {
                db.collection("Users").document(userFirebase.uid).set(user)
                    .addOnSuccessListener {
                        continuation.resume(true)
                    }.addOnFailureListener {
                        continuation.resume(false)
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

    override suspend fun saveUserTrainingRoutinesGpt(questions: Map<String, List<String>>): String {
        var message = buildFromAnsweredQuestions(questions)
        val request = ChatRequest(
            messages = listOf(
                ChatMessage(role = "user", content = message)
            )
        )
        val response = api.getChatResponse("Bearer $apiKey", request)
        val receivedMessage = response.choices[0].message.content


    }

    fun buildFromAnsweredQuestions(answeredQuestions: Map<String, List<String>>): String {
        var stringQuestions = ""
        var stringResponses = ""

        answeredQuestions.filterValues { it.isNotEmpty() }.forEach { (question, responses) ->
            stringResponses = responses.joinToString("\n")

            stringQuestions += "$question\n$stringResponses\n"
        }

        return "En base a estas preguntas respondidas, creame una rutina de entrenamientos $stringQuestions"
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




