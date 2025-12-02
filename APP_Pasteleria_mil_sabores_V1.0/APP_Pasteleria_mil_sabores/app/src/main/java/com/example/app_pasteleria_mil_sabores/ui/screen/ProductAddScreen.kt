package com.example.app_pasteleria_mil_sabores.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.app_pasteleria_mil_sabores.data.entities.ProductEntity
// Se ha eliminado la dependencia de FormValidation
import com.example.app_pasteleria_mil_sabores.viewmodel.ProductState
import org.tensorflow.lite.support.label.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    productState: ProductState,
    userId: Int,
    onAddProduct: (ProductEntity) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estados del formulario
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    // Estados de error
    var nameError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }
    var stockError by remember { mutableStateOf<String?>(null) }
    var showErrors by remember { mutableStateOf(false) }

    // Estado del Snackbar
    val snackbarHostState = remember { SnackbarHostState() }



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agregar Nuevo Producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Campo Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del Producto") },
                // CORRECCIÓN: Usamos Person, que es un icono de altísima compatibilidad.
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = nameError != null && showErrors,
                supportingText = {
                    if (nameError != null && showErrors) {
                        Text(nameError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo Descripción
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                // CORRECCIÓN: Usamos Subject, que es básico.
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = descriptionError != null && showErrors,
                supportingText = {
                    if (descriptionError != null && showErrors) {
                        Text(descriptionError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo Precio
            OutlinedTextField(
                value = price,
                onValueChange = { price = it.replace(",", ".") }, // Permite coma como separador decimal
                label = { Text("Precio (USD)") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = priceError != null && showErrors,
                supportingText = {
                    if (priceError != null && showErrors) {
                        Text(priceError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo Stock
            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock / Cantidad") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = stockError != null && showErrors,
                supportingText = {
                    if (stockError != null && showErrors) {
                        Text(stockError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))


            // Botón Agregar Producto
            Button(
                onClick = {
                    showErrors = true

                    // VALIDACIÓN SIMPLE Y SEGURA
                    nameError = if (name.isBlank()) "El nombre es obligatorio." else null
                    descriptionError = if (description.isBlank()) "La descripción es obligatoria." else null

                    // Validación de Precio con parsing seguro
                    val parsedPrice = price.toDoubleOrNull()
                    priceError = when {
                        price.isBlank() -> "El precio es obligatorio."
                        parsedPrice == null || parsedPrice <= 0 -> "El precio debe ser un número válido (> 0)."
                        else -> null
                    }

                    // Validación de Stock con parsing seguro
                    val parsedStock = stock.toIntOrNull()
                    stockError = when {
                        stock.isBlank() -> "El stock es obligatorio."
                        parsedStock == null || parsedStock < 0 -> "El stock debe ser un número entero válido (>= 0)."
                        else -> null
                    }

                    if (nameError == null && descriptionError == null &&
                        priceError == null && stockError == null) {

                        // Los valores parsedPrice y parsedStock son definitivamente no nulos aquí
                        val product = ProductEntity(
                            id = 0, // Se asume que la base de datos asignará el ID
                            name = name,
                            description = description,
                            price = parsedPrice!!,
                            stock = parsedStock!!,
                            imageUrl = imageUrl,
                        )
                        onAddProduct(product)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !productState.isLoading
            ) {
                if (productState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar Producto")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}