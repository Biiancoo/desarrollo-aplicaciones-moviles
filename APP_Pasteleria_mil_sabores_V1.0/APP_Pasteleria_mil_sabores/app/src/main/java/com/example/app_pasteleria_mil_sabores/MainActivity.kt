package com.example.ecommerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.ecommerce.data.api.ApiService
import com.example.app_pasteleria_mil_sabores.data.db.AppDatabase
import com.example.ecommerce.data.repository.CartRepository
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.data.repository.UserRepository
import com.example.app_pasteleria_mil_sabores.navigation.Navigation
import com.example.ecommerce.ui.viewmodels.AuthViewModel
import com.example.app_pasteleria_mil_sabores.viewmodel.CartViewModel
import com.example.ecommerce.ui.viewmodels.ProductViewModel
import com.example.ecommerce.ui.viewmodels.ProfileViewModel

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private lateinit var authViewModel: AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database
        database = AppDatabase.getDatabase(applicationContext)

        // Initialize repositories
        val userRepository = UserRepository(database.userDao())
        val apiService = ApiService()
        val productRepository = ProductRepository(database.productDao(), apiService)
        val cartRepository = CartRepository(database.cartDao())

        // Initialize ViewModels
        authViewModel = AuthViewModel(userRepository)
        profileViewModel = ProfileViewModel(userRepository)
        productViewModel = ProductViewModel(productRepository)
        cartViewModel = CartViewModel(cartRepository)

        setContent {
            MaterialTheme {
                Surface {
                    Navigation(
                        authViewModel = authViewModel,
                        profileViewModel = profileViewModel,
                        productViewModel = productViewModel,
                        cartViewModel = cartViewModel
                    )
                }
            }
        }
    }
}