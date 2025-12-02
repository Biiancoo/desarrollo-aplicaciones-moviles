package com.example.app_pasteleria_mil_sabores

import com.example.app_pasteleria_mil_sabores.data.dao.ProductDao
import com.example.app_pasteleria_mil_sabores.data.model.Product
import com.example.app_pasteleria_mil_sabores.data.repository.ProductRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class ProductRepositoryTest : FunSpec({
    val mockDao = mockk<ProductDao>()
    val repository = ProductRepository(mockDao)

    test("insert product calls dao") {
        val product = Product(name = "Torta", price = 20.0, description = "Rica", imageResId = 1)
        coEvery { mockDao.insert(product) } returns 1L

        runTest {
            val id = repository.insert(product)
            id shouldBe 1L
            coVerify { mockDao.insert(product) }
        }
    }
})