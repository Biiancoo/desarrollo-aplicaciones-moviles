package com.example.app_pasteleria_mil_sabores.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_pasteleria_mil_sabores.viewmodel.AuthState
import com.example.app_pasteleria_mil_sabores.viewmodel.AuthViewModel


@Composable
fun RegisterScreen(navController: NavController) {

    val viewModel: AuthViewModel = viewModel()
    val uiState by viewModel.authState.collectAsState()

    // Campos
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Validación simple
    val isValid = name.isNotBlank() &&
            email.isNotBlank() &&
            password.length >= 4

    // Éxito → redirección segura
    LaunchedEffect(key1 = uiState.success) {
        if (uiState.success) {
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }


    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                "Crear cuenta",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña (mínimo 4 caracteres)") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(26.dp))

            Button(
                onClick = {
                    if (isValid) {
                        viewModel.register(email, password, name)
                    }
                },
                enabled = isValid && !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text("Registrar")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            TextButton(
                onClick = { navController.navigate("login") }
            ) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }

            AnimatedVisibility(
                visible = uiState.error != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 18.dp)
                )
            }
        }
    }
}
