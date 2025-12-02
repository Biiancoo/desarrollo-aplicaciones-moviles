package com.example.app_pasteleria_mil_sabores.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.app_pasteleria_mil_sabores.R
import com.example.app_pasteleria_mil_sabores.data.model.Product
import com.example.app_pasteleria_mil_sabores.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductEditScreen(navController: NavController, productId: Long) {
    val viewModel: ProductViewModel = hiltViewModel()
    var product by remember { mutableStateOf<Product?>(null) }

    val imageMap = mapOf(
        "tortachocolate" to R.drawable.tortachocolate,
        "browniesingluten" to R.drawable.browniesingluten,
        "cheesecakesinzucar" to R.drawable.cheesecakesinzucar,
        "empanadamanzana" to R.drawable.empanadamanzana,
        "galletasveganas" to R.drawable.galletasveganas,
        "moussecocolate" to R.drawable.moussecocolate,
        "pansingluten" to R.drawable.pansingluten,
        "tartasantiago" to R.drawable.tartasantiago,
        "tiramisu" to R.drawable.tiramisu,
        "tortaboda" to R.drawable.tortaboda,
        "tortachocolate" to R.drawable.tortachocolate,
        "tortacumpleanos" to R.drawable.tortacumpleanos
    )
    val imageKeys = imageMap.keys.toList()
    var selectedImageIndex by remember { mutableStateOf(0) }

    LaunchedEffect(productId) {
        viewModel.viewModelScope.launch {
            val found = viewModel.products.value.find { it.id == productId }
            product = found
            if (found != null) {
                val key = imageMap.entries.find { it.value == found.imageResId }?.key
                selectedImageIndex = imageKeys.indexOf(key ?: imageKeys[0])
            }
        }
    }

    var name by remember { mutableStateOf(product?.name ?: "") }
    var price by remember { mutableStateOf("${product?.price ?: ""}") }
    var description by remember { mutableStateOf(product?.description ?: "") }

    LaunchedEffect(product) {
        name = product?.name ?: ""
        price = "${product?.price ?: ""}"
        description = product?.description ?: ""
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Editar Producto") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio (€)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Text("Imagen:")
            DropdownMenuSample(
                items = imageKeys,
                selectedIndex = selectedImageIndex,
                onSelectionChange = { selectedImageIndex = it }
            )

            Image(
                painter = painterResource(id = imageMap[imageKeys[selectedImageIndex]]!!),
                contentDescription = "Preview",
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (name.isNotBlank() && price.isNotBlank() && description.isNotBlank()) {
                            val updated = product?.copy(
                                name = name,
                                price = price.toDoubleOrNull() ?: 0.0,
                                description = description,
                                imageResId = imageMap[imageKeys[selectedImageIndex]]!!
                            )
                            if (updated != null) {
                                viewModel.update(updated)
                                navController.popBackStack()
                            }
                        }
                    },
                    enabled = name.isNotBlank() && price.isNotBlank() && description.isNotBlank()
                ) {
                    Text("Guardar")
                }

                IconButton(onClick = {
                    product?.let { p ->
                        viewModel.delete(p)
                        navController.popBackStack()
                    }
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}