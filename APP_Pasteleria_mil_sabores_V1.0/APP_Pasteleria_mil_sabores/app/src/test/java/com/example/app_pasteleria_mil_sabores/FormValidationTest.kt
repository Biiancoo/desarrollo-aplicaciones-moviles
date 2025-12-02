package com.example.ecommerce.ui.components

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class FormValidationTest : StringSpec({

    "validateEmail should return null for valid email" {
        val result = FormValidation.validateEmail("test@example.com")
        result shouldBe null
    }

    "validateEmail should return error for empty email" {
        val result = FormValidation.validateEmail("")
        result shouldNotBe null
        result shouldBe "El email es requerido"
    }

    "validateEmail should return error for invalid email format" {
        val result = FormValidation.validateEmail("invalid-email")
        result shouldNotBe null
        result shouldBe "Email inválido"
    }

    "validatePassword should return null for valid password" {
        val result = FormValidation.validatePassword("password123")
        result shouldBe null
    }

    "validatePassword should return error for empty password" {
        val result = FormValidation.validatePassword("")
        result shouldNotBe null
        result shouldBe "La contraseña es requerida"
    }

    "validatePassword should return error for short password" {
        val result = FormValidation.validatePassword("123")
        result shouldNotBe null
        result shouldBe "La contraseña debe tener al menos 6 caracteres"
    }

    "validateName should return null for valid name" {
        val result = FormValidation.validateName("John Doe")
        result shouldBe null
    }

    "validateName should return error for empty name" {
        val result = FormValidation.validateName("")
        result shouldNotBe null
        result shouldBe "El nombre es requerido"
    }

    "validateName should return error for short name" {
        val result = FormValidation.validateName("Jo")
        result shouldNotBe null
        result shouldBe "El nombre debe tener al menos 3 caracteres"
    }

    "validatePhone should return null for valid phone" {
        val result = FormValidation.validatePhone("12345678")
        result shouldBe null
    }

    "validatePhone should return null for empty phone (optional field)" {
        val result = FormValidation.validatePhone("")
        result shouldBe null
    }

    "validatePhone should return error for short phone" {
        val result = FormValidation.validatePhone("123")
        result shouldNotBe null
        result shouldBe "El teléfono debe tener al menos 8 dígitos"
    }

    "validateProductName should return null for valid product name" {
        val result = FormValidation.validateProductName("Gaming Laptop")
        result shouldBe null
    }

    "validateProductName should return error for empty product name" {
        val result = FormValidation.validateProductName("")
        result shouldNotBe null
        result shouldBe "El nombre del producto es requerido"
    }

    "validatePrice should return null for valid price" {
        val result = FormValidation.validatePrice("99.99")
        result shouldBe null
    }

    "validatePrice should return error for empty price" {
        val result = FormValidation.validatePrice("")
        result shouldNotBe null
        result shouldBe "El precio es requerido"
    }

    "validatePrice should return error for invalid price" {
        val result = FormValidation.validatePrice("invalid")
        result shouldNotBe null
        result shouldBe "Precio inválido"
    }

    "validatePrice should return error for zero or negative price" {
        val result = FormValidation.validatePrice("0")
        result shouldNotBe null
        result shouldBe "El precio debe ser mayor a 0"
    }

    "validateStock should return null for valid stock" {
        val result = FormValidation.validateStock("10")
        result shouldBe null
    }

    "validateStock should return null for zero stock" {
        val result = FormValidation.validateStock("0")
        result shouldBe null
    }

    "validateStock should return error for negative stock" {
        val result = FormValidation.validateStock("-5")
        result shouldNotBe null
        result shouldBe "El stock no puede ser negativo"
    }

    "validateDescription should return null for valid description" {
        val result = FormValidation.validateDescription("This is a great product")
        result shouldBe null
    }

    "validateDescription should return error for empty description" {
        val result = FormValidation.validateDescription("")
        result shouldNotBe null
        result shouldBe "La descripción es requerida"
    }

    "validateDescription should return error for short description" {
        val result = FormValidation.validateDescription("Short")
        result shouldNotBe null
        result shouldBe "La descripción debe tener al menos 10 caracteres"
    }
})