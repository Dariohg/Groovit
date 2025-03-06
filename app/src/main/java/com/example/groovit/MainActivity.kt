package com.example.groovit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.groovit.core.navigation.NavigationWrapper
import com.example.groovit.ui.theme.GroovitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GroovitTheme {
                NavigationWrapper()
            }
        }
    }
}