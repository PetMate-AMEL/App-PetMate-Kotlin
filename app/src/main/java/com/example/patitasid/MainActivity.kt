package com.example.patitasid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.patitasid.ui.navigation.NavGraph
import com.example.patitasid.ui.theme.PetMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetMateTheme {
                NavGraph()
            }
        }
    }
}
