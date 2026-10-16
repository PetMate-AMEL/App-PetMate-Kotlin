package com.example.patitasid.ui.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.patitasid.ui.components.PetCard
import com.example.patitasid.ui.components.PetPrimaryButton
import com.example.patitasid.ui.theme.PetMateTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(onBack: () -> Unit, viewModel: HomeViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(state = state, onBack = onBack, onRetry = viewModel::refresh)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(state: HomeUiState, onBack: () -> Unit, onRetry: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inicio") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PetCard {
                Text("Estado del servidor", style = MaterialTheme.typography.titleMedium)
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    state.message?.let {
                        Text(
                            text = it,
                            color = if (state.isOnline) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error,
                            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                        )
                    }
                    state.lastCheck?.let { Text("Última revisión: $it", style = MaterialTheme.typography.bodyMedium) }
                }
                PetPrimaryButton(text = "Reintentar", onClick = onRetry, enabled = !state.isLoading)
            }
        }
    }
}

@Preview
@Composable
private fun HomeOfflinePreview() {
    PetMateTheme {
        HomeContent(
            state = HomeUiState(message = HomeViewModel.NETWORK_MESSAGE, lastCheck = "16/10/2026"),
            onBack = {},
            onRetry = {}
        )
    }
}
