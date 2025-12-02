package com.example.app_pasteleria_mil_sabores.ui.screen

import androidx.compose.animation.*
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
import com.example.ecommerce.data.database.entities.ProductEntity
import com.example.ecommerce.ui.components.FormValidation
import com.example.ecommerce.ui.viewmodels.ProductState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    productState: ProductState,
    productId: Int,
    onUpdateProduct: (ProductEntity) -> Unit,
    onDeleteProduct: (ProductEntity) -> Unit,
    onLoadProduct: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }
    var stockError by remember { mutableStateOf<String?>(null) }
    var showErrors by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        onLoadProduct(productId)
    }

    LaunchedEffect(productState.currentProduct) {
        productState.currentProduct?.let { product ->
            name = product.name
            description = product.description
            price = product.price.toString()
            stock = product.stock.toString()
            imageUrl = product.imageUrl
        }
    }

    LaunchedEffect(productState.successMessage) {
        if (productState.successMessage != null) {
            delay(1500)
            onBack()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Producto") },
            text = { Text("¿Estás seguro de que deseas eliminar este producto?") },
            confirmButton = {
                Button(
                    onClick = {
                        productState.currentProduct?.let { onDeleteProduct(it) }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            AnimatedVisibility(
                visible = productState.successMessage != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = productState.successMessage ?: "",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (showErrors) {
                        nameError = FormValidation.validateProductName(it)
                    }
                },
                label = { Text("Nombre del producto") },
                leadingIcon = {
                    Icon(Icons.Default.ShoppingBag, contentDescription = null)
                },
                isError = nameError != null && showErrors,
                supportingText = {
                    if (nameError != null && showErrors) {
                        Text(nameError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    if (showErrors) {
                        descriptionError = FormValidation.validateDescription(it)
                    }
                },
                label = { Text("Descripción") },
                leadingIcon = {
                    Icon(Icons.Default.Info, contentDescription = null)
                },
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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it
                        if (showErrors) {
                            priceError = FormValidation.validatePrice(it)
                        }
                    },
                    label = { Text("Precio") },
                    leadingIcon = {
                        Icon(Icons.Default.AccountBox, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = priceError != null && showErrors,
                    supportingText = {
                        if (priceError != null && showErrors) {
                            Text(priceError!!)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = {
                        stock = it
                        if (showErrors) {
                            stockError = FormValidation.validateStock(it)
                        }
                    },
                    label = { Text("Stock") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = stockError != null && showErrors,
                    supportingText = {
                        if (stockError != null && showErrors) {
                            Text(stockError!!)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL de imagen (opcional)") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    showErrors = true
                    nameError = FormValidation.validateProductName(name)
                    descriptionError = FormValidation.validateDescription(description)
                    priceError = FormValidation.validatePrice(price)
                    stockError = FormValidation.validateStock(stock)

                    if (nameError == null && descriptionError == null &&
                        priceError == null && stockError == null) {
                        productState.currentProduct?.let { product ->
                            onUpdateProduct(
                                product.copy(
                                    name = name,
                                    description = description,
                                    price = price.toDouble(),
                                    stock = stock.toInt(),
                                    imageUrl = imageUrl
                                )
                            )
                        }
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
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Cambios")
                }
            }
        }
    }
}