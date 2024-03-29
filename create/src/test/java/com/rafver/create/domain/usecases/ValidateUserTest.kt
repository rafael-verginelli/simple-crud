package com.rafver.create.domain.usecases

import com.rafver.create.data.CreateResultType
import org.amshove.kluent.`should be equal to`
import org.junit.Test

class ValidateUserTest {

    private lateinit var useCase: ValidateUser

    @Test
    fun `when name is empty, return the correct error`() {
        // Given
        `given the tested use case`()
        val name = ""
        val age = "30"
        val email = "john@doe.com"

        // When
        val result = useCase(name, age, email)

        // Then
        result `should be equal to` CreateResultType.Error.NameMandatory
    }

    @Test
    fun `when age is empty, return the correct error`() {
        // Given
        `given the tested use case`()
        val name = "John"
        val age = ""
        val email = "john@doe.com"

        // When
        val result = useCase(name, age, email)

        // Then
        result `should be equal to` CreateResultType.Error.AgeMandatory
    }

    @Test
    fun `when age is not an int number, return the correct error`() {
        // Given
        `given the tested use case`()
        val name = "John"
        val age = "abc"
        val email = "john@doe.com"

        // When
        val result = useCase(name, age, email)

        // Then
        result `should be equal to` CreateResultType.Error.InvalidAge
    }

    @Test
    fun `when email is empty, return the correct error`() {
        // Given
        `given the tested use case`()
        val name = "John"
        val age = "30"
        val email = ""

        // When
        val result = useCase(name, age, email)

        // Then
        result `should be equal to` CreateResultType.Error.EmailMandatory
    }

    private fun `given the tested use case`() {
        useCase = ValidateUser()
    }
}