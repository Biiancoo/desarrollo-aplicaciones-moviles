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
// Importante: Usar hiltViewModel para que funcione la inyección del repositorio
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.app_pasteleria_mil_sabores.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    // Inyectamos el ViewModel automáticamente con Hilt
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.authState.collectAsState()

    // Campos del formulario
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Validación simple local
    val isValid = name.isNotBlank() &&
            email.isNotBlank() &&
            password.length >= 4

    // Efecto para manejar el éxito del registro
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            // Esperar un momento para que el usuario vea el mensaje (opcional) o navegar directo
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
            viewModel.clearMessages() // Limpiar estado para la próxima vez
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
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Campo Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña (mínimo 4 caracteres)") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Botón de Registro
            Button(
                onClick = {
                    if (isValid) {
                        viewModel.register(email, password, name)
                    }
                },
                enabled = isValid && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Registrarme")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón ir a Login
            TextButton(
                onClick = {
                    viewModel.clearMessages()
                    navController.navigate("login")
                }
            ) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }

            // Mensajes de Error
            AnimatedVisibility(
                visible = uiState.errorMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = uiState.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}