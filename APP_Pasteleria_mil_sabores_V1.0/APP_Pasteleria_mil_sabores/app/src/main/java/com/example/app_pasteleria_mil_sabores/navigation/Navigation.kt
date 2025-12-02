package com.example.ecommerce.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app_pasteleria_mil_sabores.ui.screen.EditProfileScreen
import com.example.ecommerce.ui.screens.*
import com.example.ecommerce.ui.viewmodels.*

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
    val authState by authViewModel.authState.collectAsState()
    val profileState by profileViewModel.profileState.collectAsState()
    val productState by productViewModel.productState.collectAsState()
    val cartState by cartViewModel.cartState.collectAsState()

    LaunchedEffect(authState.isLoggedIn) {
        if (authState.isLoggedIn) {
            navController.navigate(Screen.ProductList.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
            profileViewModel.loadUserProfile(authState.userId)
            productViewModel.loadAllProducts()
            cartViewModel.loadCart(authState.userId)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                authState = authState,
                onLogin = { email, password ->
                    authViewModel.login(email, password)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                authState = authState,
                onRegister = { email, password, name ->
                    authViewModel.register(email, password, name)
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                    authViewModel.clearMessages()
                }
            )
        }

        composable(Screen.ProductList.route) {
            LaunchedEffect(Unit) {
                productViewModel.loadAllProducts()
            }

            ProductListScreen(
                productState = productState,
                userId = authState.userId,
                onAddProduct = {
                    navController.navigate(Screen.AddProduct.route)
                },
                onEditProduct = { productId ->
                    navController.navigate(Screen.EditProduct.createRoute(productId))
                },
                onAddToCart = { product ->
                    cartViewModel.addToCart(
                        product.id,
                        product.name,
                        product.price,
                        authState.userId
                    )
                },
                onProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onLoadFromApi = {
                    productViewModel.loadProductsFromApi(authState.userId)
                }
            )
        }

        composable(Screen.AddProduct.route) {
            AddProductScreen(
                productState = productState,
                userId = authState.userId,
                onAddProduct = { product ->
                    productViewModel.addProduct(product)
                },
                onBack = {
                    navController.popBackStack()
                    productViewModel.clearMessages()
                }
            )
        }

        composable(
            route = Screen.EditProduct.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0

            EditProductScreen(
                productState = productState,
                productId = productId,
                onUpdateProduct = { product ->
                    productViewModel.updateProduct(product)
                },
                onDeleteProduct = { product ->
                    productViewModel.deleteProduct(product)
                },
                onLoadProduct = { id ->
                    productViewModel.loadProductById(id)
                },
                onBack = {
                    navController.popBackStack()
                    productViewModel.clearMessages()
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                profileState = profileState,
                onEditProfile = {
                    navController.navigate(Screen.EditProfile.route)
                },
                onViewMyProducts = {
                    // Could navigate to a filtered product list
                    navController.popBackStack()
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                profileState = profileState,
                onUpdateProfile = { user ->
                    profileViewModel.updateProfile(user)
                },
                onBack = {
                    navController.popBackStack()
                    profileViewModel.clearMessages()
                }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                cartState = cartState,
                onUpdateQuantity = { cartItem, newQuantity ->
                    cartViewModel.updateQuantity(cartItem, newQuantity)
                },
                onRemoveItem = { cartItem ->
                    cartViewModel.removeFromCart(cartItem)
                },
                onClearCart = {
                    cartViewModel.clearCart(authState.userId)
                },
                onBack = {
                    navController.popBackStack()
                    cartViewModel.clearMessages()
                }
            )
        }
    }
}