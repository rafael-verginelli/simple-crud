package com.rafver.create.domain.usecases

import com.rafver.core_data.dtos.UserDTO
import com.rafver.create.data.CreateResultType
import com.rafver.create.domain.repositories.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.amshove.kluent.`should be equal to`
import org.junit.Test

class CreateUserTest {

    private val userRepository: UserRepository = mockk(relaxed = true)

    private lateinit var useCase: CreateUser

    @Test
    fun `when use case is invoked, if age is not int, an error is thrown`() {
        //Given
        `given the tested use case`()
        val name = "john"
        val age = "abc"
        val email = "john@doe.com"
        val expectedExceptionMessage = "User data validation failed"

        // when
        var caughtException: Exception? = null
        var result: Result<CreateResultType>? = null
        try {
            result = useCase(name, age, email)
        } catch (e: Exception) {
            caughtException = e
        }

        // Then
        result `should be equal to` null
        caughtException?.message `should be equal to` expectedExceptionMessage

        verify(exactly = 0) {
            userRepository.createUser(any())
        }
    }

    @Test
    fun `when use case is invoked, if all params are valid but repo operation fails, fail result is returned`() {
        //Given
        `given the tested use case`()
        val expectedName = "john"
        val expectedAge = 20
        val expectedEmail = "john@doe.com"
        val expectedException = Exception("Some Exception")
        val slot = slot<UserDTO>()
        every { userRepository.createUser(capture(slot)) } returns Result.failure(expectedException)

        // when
        val result = useCase(name = "john", age = "20", email = "john@doe.com")

        // Then
        slot.captured.run {
            name `should be equal to` expectedName
            age `should be equal to` expectedAge
            email `should be equal to` expectedEmail
        }

        result.isFailure `should be equal to` true
        result.exceptionOrNull() `should be equal to` expectedException

        verify(exactly = 1) {
            userRepository.createUser(any())
        }
    }

    @Test
    fun `when use case is invoked, if all params are valid and repo operation succeeds, success result is returned`() {
        //Given
        `given the tested use case`()
        val name = "john"
        val age = "20"
        val email = "john@doe.com"
        val expectedResult = CreateResultType.Ok

        every { userRepository.createUser(any()) } returns Result.success<CreateResultType>(CreateResultType.Ok)

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