package com.example.app_pasteleria_mil_sabores.ui.components

import android.util.Patterns

object FormValidation {

    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "El email es requerido"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Email inválido"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "La contraseña es requerida"
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }

    fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "El nombre es requerido"
            name.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }
    }

    fun validatePhone(phone: String): String? {
        return when {
            phone.isBlank() -> null // Phone is optional
            phone.length < 8 -> "El teléfono debe tener al menos 8 dígitos"
            !phone.all { it.isDigit() || it == '+' || it == ' ' } ->
                "El teléfono solo puede contener números"
            else -> null
        }
    }

    fun validateProductName(name: String): String? {
        return when {
            name.isBlank() -> "El nombre del producto es requerido"
            name.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }
    }

    fun validatePrice(priceText: String): String? {
        return when {
            priceText.isBlank() -> "El precio es requerido"
            priceText.toDoubleOrNull() == null -> "Precio inválido"
            priceText.toDouble() <= 0 -> "El precio debe ser mayor a 0"
            else -> null
        }
    }

    fun validateStock(stockText: String): String? {
        return when {
            stockText.isBlank() -> "El stock es requerido"
            stockText.toIntOrNull() == null -> "Stock inválido"
            stockText.toInt() < 0 -> "El stock no puede ser negativo"
            else -> null
        }
    }

    fun validateDescription(description: String): String? {
        return when {
            description.isBlank() -> "La descripción es requerida"
            description.length < 10 -> "La descripción debe tener al menos 10 caracteres"
            else -> null
        }
    }
}