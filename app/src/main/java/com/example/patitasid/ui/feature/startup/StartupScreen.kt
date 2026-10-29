package com.example.patitasid.ui.feature.startup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.example.patitasid.ui.theme.PetMateTheme

// Shown only while the saved session is read; it is replaced as soon as the destination is known.
@Composable
fun StartupScreen() {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier.fillMaxSize().background(colors.primary),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = colors.onPrimary,
            modifier = Modifier.semantics { contentDescription = "Abriendo PetMate" }
        )
    }
}

@Preview
@Composable
private fun StartupScreenPreview() {
    PetMateTheme { StartupScreen() }
}
