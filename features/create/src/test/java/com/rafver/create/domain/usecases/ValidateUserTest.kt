package com.rafver.create.domain.usecases

import com.rafver.create.R
import com.rafver.create.data.CreateResultType
import org.amshove.kluent.`should be equal to`
import org.junit.Test

class ValidateUserTest {

    private lateinit var useCase: ValidateUser

    @Test
    fun `when name is empty, the correct error is returned`() {
        // Given
        `given the tested use case`()
        val name = ""
        val age = "30"
        val email = "john@doe.com"
        val expectedResult = listOf(
            CreateResultType.Error.NameMandatory(R.string.error_create_mandatory_field)
        )

        // When
        val result = useCase(name, age, email)

        // Then
        result `should be equal to` expectedResult
    }

    @Test
    fun `when age is empty, the correct error is returned`() {
        // Given
        `given the tested use case`()
        val name = "John"
        val age = ""
        val email = "john@doe.com"
        val expectedResult = listOf(
            CreateResultType.Error.AgeMandatory(R.string.error_create_mandatory_field)
        )

        // When
        val result = useCase(name, age, email)

        // Then
        result `should be equal to` expectedResult
    }

    @Test
    fun `when age is not an int number, the correct error is returned`() {
        // Given
        `given the tested use case`()
        val name = "John"
        val age = "abc"
        val email = "john@doe.com"
        val expectedResult = listOf(
            CreateResultType.Error.InvalidAge(R.string.error_create_invalid_age)
        )

        // When
        val result = useCase(name, age, email)

        // Then
        result `should be equal to` expectedResult
    }

    @Test
    fun `when email is empty, the correct error is returned`() {
        // Given
        `given the tested use case`()
        val name = "John"
        val age = "30"
        val email = ""
        val expectedResult = listOf(
            CreateResultType.Error.EmailMandatory(R.string.error_create_mandatory_field)
        )

        // When
        val result = useCase(name, age, email)

        // Then
        result `should be equal to` expectedResult
    }

    @Test
    fun `when multiple fields are empty, the correct errors are returned`() {
        // Given
        `given the tested use case`()
        val name = ""
        val age = ""
        val email = ""
        val expectedResult = listOf(
            CreateResultType.Error.NameMandatory(R.string.error_create_mandatory_field),
            CreateResultType.Error.AgeMandatory(R.string.error_create_mandatory_field),
            CreateResultType.Error.EmailMandatory(R.string.error_create_mandatory_field)
        )

        // When
        val result = useCase(name, age, email)

        // Then
        result `should be equal to` expectedResult
    }

    private fun `given the tested use case`() {
        useCase = ValidateUser()
    }
}