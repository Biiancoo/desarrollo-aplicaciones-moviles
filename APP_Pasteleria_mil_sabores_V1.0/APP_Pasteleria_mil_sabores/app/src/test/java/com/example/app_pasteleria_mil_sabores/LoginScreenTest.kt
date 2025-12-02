package com.example.app_pasteleria_mil_sabores

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.example.app_pasteleria_mil_sabores.ui.screen.LoginScreen
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun login_withEmptyEmail_showsError() {
        composeTestRule.setContent {
            LoginScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithTag("LoginButton").performClick()
        composeTestRule.onNodeWithText("Email y contraseña obligatorios").assertExists()
    }
}