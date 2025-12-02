package com.example.ecommerce.ui.viewmodels

import com.example.ecommerce.data.database.entities.UserEntity
import com.example.ecommerce.data.repository.UserRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: AuthViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        userRepository = mockk()
        viewModel = AuthViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login should update state to loading then success on successful login`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val user = UserEntity(1, email, password, "Test User")

        coEvery { userRepository.login(email, password) } returns Result.success(user)

        viewModel.login(email, password)

        // Initially loading
        assertTrue(viewModel.authState.value.isLoading)

        testDispatcher.scheduler.advanceUntilIdle()

        // After completion
        val state = viewModel.authState.value
        assertFalse(state.isLoading)
        assertTrue(state.isLoggedIn)
        assertEquals(1, state.userId)
        assertEquals(null, state.errorMessage)

        coVerify { userRepository.login(email, password) }
    }

    @Test
    fun `login should update state with error message on failed login`() = runTest {
        val email = "test@example.com"
        val password = "wrongpassword"
        val errorMessage = "Email o contraseña incorrectos"

        coEvery { userRepository.login(email, password) } returns
                Result.failure(Exception(errorMessage))

        viewModel.login(email, password)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.authState.value
        assertFalse(state.isLoading)
        assertFalse(state.isLoggedIn)
        assertEquals(errorMessage, state.errorMessage)

        coVerify { userRepository.login(email, password) }
    }

    @Test
    fun `register should update state with success message on successful registration`() = runTest {
        val email = "newuser@example.com"
        val password = "password123"
        val name = "New User"
        val userId = 1L

        coEvery { userRepository.register(email, password, name) } returns
                Result.success(userId)

        viewModel.register(email, password, name)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.authState.value
        assertFalse(state.isLoading)
        assertFalse(state.isLoggedIn)
        assertEquals("Registro exitoso. Por favor inicia sesión.", state.successMessage)
        assertEquals(null, state.errorMessage)

        coVerify { userRepository.register(email, password, name) }
    }

    @Test
    fun `register should update state with error message on failed registration`() = runTest {
        val email = "existing@example.com"
        val password = "password123"
        val name = "Test User"
        val errorMessage = "El email ya está registrado"

        coEvery { userRepository.register(email, password, name) } returns
                Result.failure(Exception(errorMessage))

        viewModel.register(email, password, name)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.authState.value
        assertFalse(state.isLoading)
        assertFalse(state.isLoggedIn)
        assertEquals(errorMessage, state.errorMessage)
        assertEquals(null, state.successMessage)

        coVerify { userRepository.register(email, password, name) }
    }

    @Test
    fun `clearMessages should reset error and success messages`() = runTest {
        val email = "test@example.com"
        val password = "wrongpassword"

        coEvery { userRepository.login(email, password) } returns
                Result.failure(Exception("Error"))

        viewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        // Should have error message
        assertEquals("Error", viewModel.authState.value.errorMessage)

        viewModel.clearMessages()

        // Messages should be cleared
        assertEquals(null, viewModel.authState.value.errorMessage)
        assertEquals(null, viewModel.authState.value.successMessage)
    }

    @Test
    fun `logout should reset auth state`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val user = UserEntity(1, email, password, "Test User")

        coEvery { userRepository.login(email, password) } returns Result.success(user)

        viewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        // Should be logged in
        assertTrue(viewModel.authState.value.isLoggedIn)

        viewModel.logout()

        // Should be logged out
        val state = viewModel.authState.value
        assertFalse(state.isLoading)
        assertFalse(state.isLoggedIn)
        assertEquals(0, state.userId)
        assertEquals(null, state.errorMessage)
        assertEquals(null, state.successMessage)
    }

    @Test
    fun `multiple rapid login attempts should handle loading state correctly`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val user = UserEntity(1, email, password, "Test User")

        coEvery { userRepository.login(email, password) } returns Result.success(user)

        viewModel.login(email, password)
        viewModel.login(email, password)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.authState.value
        assertFalse(state.isLoading)
        assertTrue(state.isLoggedIn)
    }
}