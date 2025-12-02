package com.example.app_pasteleria_mil_sabores.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_pasteleria_mil_sabores.viewmodel.CartViewModel

@Composable
fun CartScreen(navController: NavController) {
    val viewModel: CartViewModel = viewModel()
    val cart by viewModel.cart.collectAsState()
    val totalPrice = viewModel.totalPrice

    Scaffold(topBar = { TopAppBar(title = { Text("Carrito") }) }) { padding ->
        if (cart.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("El carrito está vacío")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn {
                    items(cart) { product ->
                        CartItem(product = product) {
                            viewModel.removeFromCart(product)
                        }
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total:", style = MaterialTheme.typography.titleMedium)
                        Text("${String.format("%.2f", totalPrice)} €", style = MaterialTheme.typography.titleMedium)
                    }
                }

                Button(
                    onClick = { /* Procesar compra */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Finalizar compra")
                }
            }
        }
    }
}

@Composable
fun CartItem(product: com.example.app_pasteleria_mil_sabores.data.model.Product, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = product.imageResId),
            contentDescription = product.name,
            modifier = Modifier.size(60.dp)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(product.name, style = MaterialTheme.typography.bodyLarge)
            Text("${product.price} €", style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
        }
    }
}