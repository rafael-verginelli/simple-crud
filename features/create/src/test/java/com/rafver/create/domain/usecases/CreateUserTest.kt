package com.rafver.create.domain.usecases

import com.rafver.core_data.repositories.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be equal to`
import org.junit.Test

class CreateUserTest {

    private val userRepository: UserRepository = mockk(relaxed = true)

    private lateinit var useCase: CreateUser

    @Test
    fun `when use case is invoked, if age is not int, an error is thrown`() = runTest {
        //Given
        `given the tested use case`()
        val name = "john"
        val age = "abc"
        val email = "john@doe.com"
        val expectedExceptionMessage = "User data validation failed"

        // when
        var caughtException: Exception? = null
        var result: Result<Boolean>? = null
        try {
            result = useCase(name, age, email)
        } catch (e: Exception) {
            caughtException = e
        }

        // Then
        result `should be equal to` null
        caughtException?.message `should be equal to` expectedExceptionMessage

        coVerify(exactly = 0) {
            userRepository.createUser(any(), any(), any())
        }
    }

    @Test
    fun `when use case is invoked, if all params are valid but repo operation fails, fail result is returned`() = runTest {
        //Given
        `given the tested use case`()
        val expectedName = "john"
        val expectedAge = 20
        val expectedEmail = "john@doe.com"
        val expectedException = Exception("Some Exception")
        coEvery { userRepository.createUser(
            expectedName,
            expectedAge,
            expectedEmail
        ) } returns Result.failure(expectedException)

        // when
        val result = useCase(name = "john", age = "20", email = "john@doe.com")

        // Then
        result.isFailure `should be equal to` true
        result.exceptionOrNull() `should be equal to` expectedException

        coVerify(exactly = 1) {
            userRepository.createUser(expectedName, expectedAge, expectedEmail)
        }
    }

    @Test
    fun `when use case is invoked, if all params are valid and repo operation succeeds, success result is returned`() = runTest {
        //Given
        `given the tested use case`()
        val name = "john"
        val age = "20"
        val email = "john@doe.com"
        val expectedResult = true

        coEvery { userRepository.createUser(any(), any(), any()) } returns Result.success(true)

        // when
        val result = useCase(name, age, email)

        // Then
        result.isSuccess `should be equal to` true
        result.getOrNull() `should be equal to` expectedResult
    }

    private fun `given the tested use case`() {
        useCase = CreateUser(userRepository = userRepository)
    }
}