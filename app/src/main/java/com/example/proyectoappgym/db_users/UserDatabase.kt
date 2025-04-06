package com.example.proyectoappgym.db_users

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.proyectoappgym.entity.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.asDeferred

class UserDatabase: RepositoryUserDatabase {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    fun initializerApp() {
        auth = Firebase.auth
        db = Firebase.firestore
    }

    override suspend fun addUser(user: User): Boolean {
        //val userMap = convertUserToMap(user)
        var isError = false

        db.collection("Users").document(user.username).set(user).addOnCompleteListener { documentReference ->
            isError = true
        }.addOnFailureListener {
            isError = false
        }

        if(!isError) return false

        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) isError = true
                else isError = false
            }

        return isError
    }

    override suspend fun signIn(email: String, password: String): Int {
        var showedText = 0

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            showedText = 1
        } .addOnFailureListener { exception ->
            showedText = when(exception) {
                is FirebaseAuthInvalidUserException -> 2
                is FirebaseAuthInvalidCredentialsException -> 3
                else -> 4
            }
        }

        return showedText
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

        db.collection("Users").whereEqualTo("username", username).get().addOnSuccessListener {
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




