package com.example.app_pasteleria_mil_sabores.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_pasteleria_mil_sabores.R
import com.example.app_pasteleria_mil_sabores.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
    val email = prefs.getString("current_user_email", "") ?: ""

    val viewModel: ProfileViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(email) {
        if (email.isNotEmpty()) {
            viewModel.loadUser(email)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Perfil") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("edit_profile") }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val user = uiState.user
            if (user != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Text("Nombre: ${user.name}", style = MaterialTheme.typography.bodyLarge)
                    Text("Email: ${user.email}", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontró el perfil")
                }
            }
        }
    }
}