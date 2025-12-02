package com.example.ecommerce.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.app_pasteleria_mil_sabores.ui.screen.LoginScreen
import com.example.ecommerce.ui.viewmodels.AuthState
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysCorrectly() {
        composeTestRule.setContent {
            LoginScreen(
                authState = AuthState(),
                onLogin = { _, _ -> },
                onNavigateToRegister = {}
            )
        }

        composeTestRule
            .onNodeWithText("E-Commerce App")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Email")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Contraseña")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Iniciar Sesión")
            .assertIsDisplayed()
    }

    @Test
    fun loginScreen_showsErrorForInvalidEmail() {
        var loginCalled = false

        composeTestRule.setContent {
            LoginScreen(
                authState = AuthState(),
                onLogin = { _, _ -> loginCalled = true },
                onNavigateToRegister = {}
            )
        }

        composeTestRule
            .onNodeWithText("Email")
            .performTextInput("invalid-email")

        composeTestRule
            .onNodeWithText("Contraseña")
            .performTextInput("password123")

        composeTestRule
            .onNodeWithText("Iniciar Sesión")
            .performClick()

        composeTestRule
            .onNodeWithText("Email inválido")
            .assertIsDisplayed()

        assert(!loginCalled) { "Login should not be called with invalid email" }
    }

    @Test
    fun loginScreen_showsErrorForShortPassword() {
        var loginCalled = false

        composeTestRule.setContent {
            LoginScreen(
                authState = AuthState(),
                onLogin = { _, _ -> loginCalled = true },
                onNavigateToRegister = {}
            )
        }

        composeTestRule
            .onNodeWithText("Email")
            .performTextInput("test@example.com")

        composeTestRule
            .onNodeWithText("Contraseña")
            .performTextInput("123")

        composeTestRule
            .onNodeWithText("Iniciar Sesión")
            .performClick()

        composeTestRule
            .onNodeWithText("La contraseña debe tener al menos 6 caracteres")
            .assertIsDisplayed()

        assert(!loginCalled) { "Login should not be called with short password" }
    }

    @Test
    fun loginScreen_callsLoginWithValidCredentials() {
        var loginEmail = ""
        var loginPassword = ""

        composeTestRule.setContent {
            LoginScreen(
                authState = AuthState(),
                onLogin = { email, password ->
                    loginEmail = email
                    loginPassword = password
                },
                onNavigateToRegister = {}
            )
        }

        composeTestRule
            .onNodeWithText("Email")
            .performTextInput("test@example.com")

        composeTestRule
            .onNodeWithText("Contraseña")
            .performTextInput("password123")

        composeTestRule
            .onNodeWithText("Iniciar Sesión")
            .performClick()

        assert(loginEmail == "test@example.com") { "Email should be test@example.com" }
        assert(loginPassword == "password123") { "Password should be password123" }
    }

    @Test
    fun loginScreen_showsLoadingWhenAuthenticating() {
        composeTestRule.setContent {
            LoginScreen(
                authState = AuthState(isLoading = true),
                onLogin = { _, _ -> },
                onNavigateToRegister = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Iniciar Sesión")
            .assertDoesNotExist()
    }

    @Test
    fun loginScreen_displaysErrorMessage() {
        composeTestRule.setContent {
            LoginScreen(
                authState = AuthState(errorMessage = "Email o contraseña incorrectos"),
                onLogin = { _, _ -> },
                onNavigateToRegister = {}
            )
        }

        composeTestRule
            .onNodeWithText("Email o contraseña incorrectos")
            .assertIsDisplayed()
    }

    @Test
    fun loginScreen_navigatesToRegister() {
        var registerNavigated = false

        composeTestRule.setContent {
            LoginScreen(
                authState = AuthState(),
                onLogin = { _, _ -> },
                onNavigateToRegister = { registerNavigated = true }
            )
        }

        composeTestRule
            .onNodeWithText("¿No tienes cuenta? Regístrate")
            .performClick()

        assert(registerNavigated) { "Should navigate to register screen" }
    }
}