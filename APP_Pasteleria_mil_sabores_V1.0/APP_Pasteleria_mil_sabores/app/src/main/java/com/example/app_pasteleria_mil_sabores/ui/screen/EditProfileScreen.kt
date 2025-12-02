package com.example.app_pasteleria_mil_sabores.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_pasteleria_mil_sabores.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
    val email = prefs.getString("current_user_email", "") ?: ""

    val viewModel: ProfileViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf(uiState.user?.name ?: "") }

    LaunchedEffect(email) {
        if (email.isNotEmpty()) {
            viewModel.loadUser(email)
        }
    }

    LaunchedEffect(uiState.user) {
        name = uiState.user?.name ?: ""
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Editar Perfil") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val updated = uiState.user?.copy(name = name)
                    if (updated != null) {
                        viewModel.updateUser(updated)
                        navController.popBackStack()
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Guardar")
            }
        }
    }
}