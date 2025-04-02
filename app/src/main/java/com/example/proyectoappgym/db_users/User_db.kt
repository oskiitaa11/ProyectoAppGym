package com.example.proyectoappgym.db_users

import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.FirestoreClient
import com.google.firebase.firestore.firestore
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths

private var db = Firebase.firestore

/*
private fun establishConnection() {
    val file: Path = Paths.get("src", "main", "resources", "adminSdkFirebase.json")

    try {
        FileInputStream(file.toFile()).use { serviceAccount ->
            val options: FirebaseOptions = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build()
            FirebaseApp.initializeApp(options)
        }
    } catch (e: IOException) {
        System.err.printf("Error de entrada/salida: %s", e.message)
    }
}*/


