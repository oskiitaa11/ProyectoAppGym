package com.example.proyectoappgym.db_users

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.proyectoappgym.entity.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserDatabase: RepositoryUserDatabase {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    fun initializerApp() {
        auth = Firebase.auth
        db = Firebase.firestore
    }

    @SuppressLint("RestrictedApi")
    override suspend fun addUser(user: User): Boolean {
        //val userMap = convertUserToMap(user)
        var userFirebase: FirebaseUser?
        var password = user.password
        user.password = ""; //Para que la contraseña no se vea desde el archivo de la database

        return suspendCoroutine { continuation ->

           userFirebase = auth.createUserWithEmailAndPassword(user.email, password)
               .result.user

            if(userFirebase != null) {
                db.collection("Users").document(userFirebase.uid).set(user)
                    .addOnCompleteListener { documentReference ->
                        continuation.resume(true)
                    }.addOnFailureListener {
                        continuation.resume(false)
                    }
            } else {
                continuation.resume(false)
            }

        }
    }



    override suspend fun signIn(email: String, password: String): Int {
        /* El suspendCoroutine suspende la suspend funtion hasta que la task ha
           terminado de ejecutarse, devolviendo el valor pasado en resume()*/
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
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

    override suspend fun emailExist(email: String): Boolean {
        /*return suspendCoroutine { continuation ->
            db.collection("Users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener {
                    continuation.resume(!it.isEmpty)
                }.addOnFailureListener {
                    continuation.resume(false)
                }
        }*/
        var emailExist = false

        db.collection("Users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener {
                emailExist = !it.isEmpty
            }.addOnFailureListener {
                emailExist = false
            }.asDeferred().join()

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

    override suspend fun userExist(username: String): Boolean {
        var isExist = false

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

    override suspend fun isCorrectPassword(email: String, password: String): Boolean {
        return true

    }
}




