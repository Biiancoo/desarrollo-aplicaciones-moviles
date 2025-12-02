package com.example.app_pasteleria_mil_sabores.data.repository

import com.example.app_pasteleria_mil_sabores.data.dao.ProductDao
import com.example.app_pasteleria_mil_sabores.data.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {

    // Obtener productos en tiempo real
    fun getAllProducts(): Flow<List<Product>> =
        productDao.getAllProducts()

    // Obtener 1 producto específico
    suspend fun getById(id: Long): Product? =
        productDao.getProductById(id)

    // Insertar producto
    suspend fun insert(product: Product): Long =
        productDao.insert(product)

    // Actualizar producto
    suspend fun update(product: Product) =
        productDao.update(product)

    // Eliminar producto
    suspend fun delete(product: Product) =
        productDao.delete(product)

    // Borrar todos los productos (útil en logout o pruebas)
    suspend fun deleteAll() =
        productDao.deleteAll()
}
