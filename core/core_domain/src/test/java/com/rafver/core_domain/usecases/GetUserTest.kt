package com.rafver.core_domain.usecases

import com.rafver.core_data.dtos.UserDTO
import com.rafver.core_data.repositories.UserRepository
import com.rafver.core_domain.models.UserModel
import com.rafver.core_domain.usecases.GetUser
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should not be`
import org.junit.Test

class GetUserTest {

    private val userRepository: UserRepository = mockk(relaxed = true)

    private lateinit var useCase: GetUser

    @Test
    fun `when use case is invoked, if error occurs, return failure with exception`() = runTest {
        // Given
        `given the tested use case`()
        every { userRepository.getUser(any()) } returns Result.failure(Exception("some exception"))
        val expectedException = Exception("some exception")

        // When
        val result = useCase("some-id")

        // Then

        result.isFailure `should be equal to` true
        result.exceptionOrNull() `should not be` null
        result.exceptionOrNull()?.message `should be equal to`  expectedException.message
    }

    @Test
    fun `when use case is invoked, if operation is successful, return success with domain model`() = runTest {
        // Given
        `given the tested use case`()
        every {
            userRepository.getUser("1")
        } returns Result.success(
            UserDTO(id = "1", name = "john", age = 20, email = "john@doe.com")
        )
        val expectedUser = UserModel(id = "1", name = "john", age = 20, email = "john@doe.com")

        // When
        val result = useCase("1")

        // Then
        result.isSuccess `should be equal to` true
        result.getOrNull() `should be equal to` expectedUser
    }

    private fun `given the tested use case`() {
        useCase = GetUser(userRepository = userRepository)
    }
}