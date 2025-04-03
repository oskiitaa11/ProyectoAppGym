package com.example.proyectoappgym.db_users

import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


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


