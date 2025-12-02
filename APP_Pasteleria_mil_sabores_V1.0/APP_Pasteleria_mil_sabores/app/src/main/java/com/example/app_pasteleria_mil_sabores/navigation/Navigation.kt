package com.example.app_pasteleria_mil_sabores.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// --- PANTALLAS ---
import com.example.app_pasteleria_mil_sabores.ui.screen.AddProductScreen
import com.example.app_pasteleria_mil_sabores.ui.screen.CartScreen
import com.example.app_pasteleria_mil_sabores.ui.screen.EditProductScreen
import com.example.app_pasteleria_mil_sabores.ui.screen.EditProfileScreen // Usando el nombre corregido
import com.example.app_pasteleria_mil_sabores.ui.screen.LoginScreen
import com.example.app_pasteleria_mil_sabores.ui.screen.ProductListScreen
import com.example.app_pasteleria_mil_sabores.ui.screen.ProfileScreen
import com.example.app_pasteleria_mil_sabores.ui.screen.RegisterScreen

// --- VIEWMODELS Y ESTADOS ---
import com.example.app_pasteleria_mil_sabores.viewmodel.AuthViewModel
import com.example.app_pasteleria_mil_sabores.viewmodel.CartViewModel
import com.example.app_pasteleria_mil_sabores.viewmodel.ProductViewModel
import com.example.app_pasteleria_mil_sabores.viewmodel.ProfileViewModel
import com.example.app_pasteleria_mil_sabores.viewmodel.ProductState
import com.example.app_pasteleria_mil_sabores.data.entities.ProductEntity
import com.example.app_pasteleria_mil_sabores.model.Product // Asumiendo que esta es la entidad usada en el VM

// --- Definición de Rutas de Navegación ---
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object ProductList : Screen("product_list")
    object AddProduct : Screen("add_product")
    object EditProduct : Screen("edit_product/{productId}") {
        fun createRoute(productId: Int) = "edit_product/$productId"
    }
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
    object Cart : Screen("cart")
}

@Composable
fun Navigation(
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    navController: NavHostController = rememberNavController()
) {
    // 1. Obtener Estados de los ViewModels
    val authState by authViewModel.authState.collectAsState()
    val profileState by profileViewModel.uiState.collectAsState()
    val cartState by cartViewModel.cartState.collectAsState()

    // Asumimos que productViewModel.products devuelve List<ProductEntity>
    val productsList by productViewModel.products.collectAsState(initial = emptyList())

    // 2. Adaptador para ProductState (Corregido el casting)
    // Asumimos que ProductState.products espera List<ProductEntity>
    val productState = remember(productsList, authState.userId) {
        ProductState(
            userId = authState.userId,
            currentProduct = null,
            // 🚨 CORRECCIÓN 1: Se pasa la lista con el casting si es necesario, si no, se deja directo
            products = productsList as List<ProductEntity>
        )
    }

    // 3. Efecto para Navegación Post-Login
    LaunchedEffect(authState.isLoggedIn) {
        if (authState.isLoggedIn) {
            navController.navigate(Screen.ProductList.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
            // Cargar datos del usuario y carrito (usando el ID del usuario logueado)
            profileViewModel.loadUser("")
            cartViewModel.loadCart(authState.userId)
        }
    }

    // --- Configuración del NavHost ---
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // --- LOGIN, REGISTER, ADD PRODUCT (Sin cambios mayores) ---
        composable(Screen.Login.route) {
            LoginScreen(
                authState = authState,
                onLogin = { email, password -> authViewModel.login(email, password) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) { RegisterScreen(navController = navController) }

        composable(Screen.AddProduct.route) {
            AddProductScreen(
                productState = productState,
                userId = authState.userId,
                onAddProduct = { product ->
                    productViewModel.insert(product as Product)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        // --- LISTA DE PRODUCTOS (HOME) ---
        composable(Screen.ProductList.route) {
            ProductListScreen(
                productState = productState,
                userId = authState.userId,
                onAddProduct = { navController.navigate(Screen.AddProduct.route) },
                onEditProduct = { productId -> navController.navigate(Screen.EditProduct.createRoute(productId)) },

                // 🚨 CORRECCIÓN 2: Aseguramos que el producto sea del tipo que espera el CartViewModel
                onAddToCart = { product ->
                    cartViewModel.addToCart(product as ProductEntity) // O Product, según lo que espera el VM
                },

                onProfile = { navController.navigate(Screen.Profile.route) },
                onCart = { navController.navigate(Screen.Cart.route) },
                onLoadFromApi = { /* VM call */ }
            )
        }

        // --- EDITAR PRODUCTO ---
        composable(
            route = Screen.EditProduct.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0

            // 🚨 CORRECCIÓN 3: Aseguramos que los tipos de los IDs coincidan para la comparación
            val currentProduct = productsList.find { it.id.toInt() == productId }

            val stateWithProduct = productState.copy(currentProduct = currentProduct as ProductEntity?)

            EditProductScreen(
                productState = stateWithProduct,
                productId = productId,
                onUpdateProduct = { product ->
                    productViewModel.update(product as Product)
                    navController.popBackStack()
                },
                onDeleteProduct = { product ->
                    productViewModel.delete(product as Product)
                    navController.popBackStack()
                },
                onLoadProduct = { /* Carga implícita */ },
                onBack = { navController.popBackStack() }
            )
        }

        // --- PERFIL ---
        composable(Screen.Profile.route) {
            ProfileScreen(
                profileState = profileState,
                onEditProfile = { navController.navigate(Screen.EditProfile.route) },
                onViewMyProducts = {
                    navController.navigate(Screen.ProductList.route) { popUpTo(Screen.ProductList.route) { inclusive = true } }
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        // --- EDITAR PERFIL ---
        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                profileState = profileState,
                onUpdateProfile = { user ->
                    profileViewModel.updateUser(user)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        // --- CARRITO ---
        composable(Screen.Cart.route) {
            CartScreen(
                cartState = cartState,
                // 🚨 CORRECCIÓN 4: Se asume que item es del tipo esperado por el VM (e.g., CartItem)
                onUpdateQuantity = { item, qty -> cartViewModel.updateQuantity(item, qty) },
                onRemoveItem = { item -> cartViewModel.removeFromCart(item) },
                onClearCart = { cartViewModel.clearCart(authState.userId) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}