package com.example.app_pasteleria_mil_sabores.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.app_pasteleria_mil_sabores.R
import com.example.app_pasteleria_mil_sabores.viewmodel.CartViewModel
import com.example.app_pasteleria_mil_sabores.viewmodel.ProductViewModel
import androidx.compose.material.icons.filled.ShoppingCart


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(navController: NavController) {
    val productViewModel: ProductViewModel = hiltViewModel()
    val cartViewModel: CartViewModel = hiltViewModel()
    val products by productViewModel.products.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Productos") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("product_add") }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir producto")
            }
        },
        content = { padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onEdit = { navController.navigate("product_edit/${product.id}") },
                        onAddToCart = { cartViewModel.addToCart(product) }
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    )
}

@Composable
fun ProductCard(
    product: com.example.app_pasteleria_mil_sabores.data.model.Product,
    onEdit: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                modifier = Modifier.size(100.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text("${product.price} €", style = MaterialTheme.typography.bodyMedium)
                Text(
                    product.description,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onAddToCart) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Añadir al carrito")
            }
        }
    }
}