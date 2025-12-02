package com.example.ecommerce.data.repository

import com.example.ecommerce.data.database.dao.UserDao
import com.example.ecommerce.data.database.entities.UserEntity
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserRepositoryTest {

    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        userDao = mockk()
        userRepository = UserRepository(userDao)
    }

    @Test
    fun `register should return success when user does not exist`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val name = "Test User"
        val userId = 1L

        coEvery { userDao.getUserByEmail(email) } returns null
        coEvery { userDao.insert(any()) } returns userId

        val result = userRepository.register(email, password, name)

        assertTrue(result.isSuccess)
        assertEquals(userId, result.getOrNull())

        coVerify { userDao.getUserByEmail(email) }
        coVerify { userDao.insert(any()) }
    }

    @Test
    fun `register should return failure when user already exists`() = runTest {
        val email = "existing@example.com"
        val password = "password123"
        val name = "Test User"
        val existingUser = UserEntity(1, email, "oldpass", "Old User")

        coEvery { userDao.getUserByEmail(email) } returns existingUser

        val result = userRepository.register(email, password, name)

        assertTrue(result.isFailure)
        assertEquals("El email ya está registrado", result.exceptionOrNull()?.message)

        coVerify { userDao.getUserByEmail(email) }
        coVerify(exactly = 0) { userDao.insert(any()) }
    }

    @Test
    fun `login should return success with user when credentials are correct`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val user = UserEntity(1, email, password, "Test User")

        coEvery { userDao.login(email, password) } returns user

        val result = userRepository.login(email, password)

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())

        coVerify { userDao.login(email, password) }
    }

    @Test
    fun `login should return failure when credentials are incorrect`() = runTest {
        val email = "test@example.com"
        val password = "wrongpassword"

        coEvery { userDao.login(email, password) } returns null

        val result = userRepository.login(email, password)

        assertTrue(result.isFailure)
        assertEquals("Email o contraseña incorrectos", result.exceptionOrNull()?.message)

        coVerify { userDao.login(email, password) }
    }

    @Test
    fun `updateUser should return success when update is successful`() = runTest {
        val user = UserEntity(1, "test@example.com", "password", "Updated Name")

        coEvery { userDao.update(user) } just Runs

        val result = userRepository.updateUser(user)

        assertTrue(result.isSuccess)

        coVerify { userDao.update(user) }
    }

    @Test
    fun `updateUser should return failure when update throws exception`() = runTest {
        val user = UserEntity(1, "test@example.com", "password", "Updated Name")
        val exception = RuntimeException("Database error")

        coEvery { userDao.update(user) } throws exception

        val result = userRepository.updateUser(user)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())

        coVerify { userDao.update(user) }
    }
}