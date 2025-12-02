package com.example.app_pasteleria_mil_sabores.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app_pasteleria_mil_sabores.ui.screen.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("edit_profile") { EditProfileScreen(navController) }
        composable("products") { ProductListScreen(navController) }
        composable("product_add") { ProductAddScreen(navController) }
        composable("product_edit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0L
            ProductEditScreen(navController, id)
        }
        composable("cart") { CartScreen(navController) }
    }
}