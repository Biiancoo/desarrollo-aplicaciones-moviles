package com.example.app_pasteleria_mil_sabores.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.app_pasteleria_mil_sabores.R
import com.example.app_pasteleria_mil_sabores.data.model.Product
import com.example.app_pasteleria_mil_sabores.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductAddScreen(navController: NavController) {
    val viewModel: ProductViewModel = hiltViewModel()
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val imageMap = mapOf(
        // Pasteles clásicos
        "tortachocolate" to R.drawable.tortachocolate,
        "tortacumpleanos" to R.drawable.tortacumpleanos,
        "tortaboda" to R.drawable.tortaboda,
        "tortamanjar" to R.drawable.tortamanjar,
        "tortafrutas" to R.drawable.tortafrutas,
        "tortanaranja" to R.drawable.tortanaranja,
        "tortavainilla" to R.drawable.tortavainilla,
        "tortavegana" to R.drawable.tortavegana,

        // Postres especiales
        "cheesecakesinazucar" to R.drawable.cheesecakesinazucar,
        "tiramisu" to R.drawable.tiramisu,
        "tartasantiago" to R.drawable.tartasantiago,
        "moussechocolate" to R.drawable.moussechocolate,

        // Dietéticos / especiales
        "browniesingluten" to R.drawable.browniesingluten,
        "pansingluten" to R.drawable.pansingluten,
        "galletasveganas" to R.drawable.galletasveganas,
        "empanadamanzana" to R.drawable.empanadamanzana
    )
    val imageKeys = imageMap.keys.toList()
    var selectedImageIndex by remember { mutableStateOf(0) }

    Scaffold(topBar = { TopAppBar(title = { Text("Añadir Producto") }) }) { padding ->
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

            Button(
                onClick = {
                    if (name.isNotBlank() && price.isNotBlank() && description.isNotBlank()) {
                        val product = Product(
                            name = name,
                            price = price.toDoubleOrNull() ?: 0.0,
                            description = description,
                            imageResId = imageMap[imageKeys[selectedImageIndex]]!!
                        )
                        viewModel.insert(product)
                        navController.popBackStack()
                    }
                },
                enabled = name.isNotBlank() && price.isNotBlank() && description.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Producto")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuSample(
    items: List<String>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = items[selectedIndex],
            onValueChange = {},
            readOnly = true,
            label = { Text("Selecciona imagen") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onSelectionChange(index)
                        expanded = false
                    }
                )
            }
        }
    }
}