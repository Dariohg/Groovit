package com.example.groovit

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

fun obtenerToken() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val token = task.result
            Log.d("Token", "Token: $token")
        } else {
            Log.e("Token", "Error al obtener el token: ${task.exception}")
        }
    }
}