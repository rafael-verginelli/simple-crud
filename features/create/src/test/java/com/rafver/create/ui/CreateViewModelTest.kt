package com.rafver.create.ui

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.rafver.core_domain.models.UserModel
import com.rafver.core_domain.usecases.GetUser
import com.rafver.core_testing.util.TestCoroutineRule
import com.rafver.create.R
import com.rafver.create.data.CreateResultType
import com.rafver.create.domain.usecases.CreateUser
import com.rafver.create.domain.usecases.UpdateUser
import com.rafver.create.domain.usecases.ValidateUser
import com.rafver.create.ui.models.CreateUiErrorState
import com.rafver.create.ui.models.CreateUiState
import com.rafver.create.ui.models.CreateViewEvent
import com.rafver.create.ui.models.CreateViewModelEffect
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be equal to`
import org.junit.After
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val validateUser: ValidateUser = mockk(relaxed = true)
    private val createUser: CreateUser = mockk(relaxed = true)
    private val updateUser: UpdateUser = mockk(relaxed = true)
    private val getUser: GetUser = mockk(relaxed = true)

    private lateinit var viewModel: CreateViewModel

    @Test
    fun `when user inputs a new name, the ui state is properly updated`() = runTest {
        // Given
        `given the tested view model`()
        val expectedName = "john"

        viewModel.uiState.test {

            awaitItem().name `should be equal to` ""

            // When
            viewModel.onViewEvent(CreateViewEvent.OnNameChanged("john"))

            // Then
            awaitItem().name `should be equal to` expectedName
        }
    }

    @Test
    fun `when user inputs a new name, if errors are present, the name error is removed, but not the others`() = runTest {
        // Given
        `given the tested view model`()
        val expectedName = "john"
        val initialErrors = CreateUiErrorState(
            mandatoryNameError = 123,
            mandatoryAgeError = 234,
            invalidAgeError = 345,
            mandatoryEmailError = 456
        )
        val expectedErrors = CreateUiErrorState(
            mandatoryNameError = null,
            mandatoryAgeError = 234,
            invalidAgeError = 345,
            mandatoryEmailError = 456
        )
        viewModel.updateState(CreateUiState(errors = initialErrors))

        viewModel.uiState.test {
            awaitItem().run {
                name `should be equal to` ""
                errors `should be equal to` initialErrors
            }

            // When
            viewModel.onViewEvent(CreateViewEvent.OnNameChanged("john"))

            // Then
            awaitItem().run {
                name `should be equal to` expectedName
                errors `should be equal to` expectedErrors
            }
        }
    }

    @Test
    fun `when user inputs a new age, the ui state is properly updated`() = runTest {
        // Given
        `given the tested view model`()
        val expectedAge = "20"

        viewModel.uiState.test {
            awaitItem().age `should be equal to` ""

            // When
            viewModel.onViewEvent(CreateViewEvent.OnAgeChanged("20"))
            advanceUntilIdle()

            // Then
            awaitItem().age `should be equal to` expectedAge
        }
    }

    @Test
    fun `when user inputs a new age, if errors are present, the age errors are removed, but not the others`() = runTest {
        // Given
        `given the tested view model`()
        val expectedAge = "20"
        val initialErrors = CreateUiErrorState(
            mandatoryNameError = 123,
            mandatoryAgeError = 234,
            invalidAgeError = 345,
            mandatoryEmailError = 456
        )
        val expectedErrors = CreateUiErrorState(
            mandatoryNameError = 123,
            mandatoryAgeError = null,
            invalidAgeError = null,
            mandatoryEmailError = 456
        )
        viewModel.updateState(CreateUiState(errors = initialErrors))

        viewModel.uiState.test {
            awaitItem().run {
                age `should be equal to` ""
                errors `should be equal to` initialErrors
            }

            // When
            viewModel.onViewEvent(CreateViewEvent.OnAgeChanged("20"))

            // Then
            awaitItem().run {
                age `should be equal to` expectedAge
                errors `should be equal to` expectedErrors
            }
        }
    }

    @Test
    fun `when user inputs a new email, the ui state is properly updated`() = runTest {
        // Given
        `given the tested view model`()
        val expectedEmail = "john@doe.com"


        viewModel.uiState.test {
            awaitItem().email `should be equal to` ""

            // When
            viewModel.onViewEvent(CreateViewEvent.OnEmailChanged("john@doe.com"))
            advanceUntilIdle()

            // Then
            awaitItem().email `should be equal to` expectedEmail
        }
    }

    @Test
    fun `when user inputs a new email, if errors are present, the email error is removed, but not the others`() = runTest {
        // Given
        `given the tested view model`()
        val expectedEmail = "john@doe.com"
        val initialErrors = CreateUiErrorState(
            mandatoryNameError = 123,
            mandatoryAgeError = 234,
            invalidAgeError = 345,
            mandatoryEmailError = 456
        )
        val expectedErrors = CreateUiErrorState(
            mandatoryNameError = 123,
            mandatoryAgeError = 234,
            invalidAgeError = 345,
            mandatoryEmailError = null
        )
        viewModel.updateState(CreateUiState(errors = initialErrors))

        viewModel.uiState.test {
            awaitItem().run {
                email `should be equal to` ""
                errors `should be equal to` initialErrors
            }

            // When
            viewModel.onViewEvent(CreateViewEvent.OnEmailChanged("john@doe.com"))

            // Then
            awaitItem().run {
                email `should be equal to` expectedEmail
                errors `should be equal to` expectedErrors
            }
        }
    }

    @Test
    fun `when discard button is clicked, the correct snackbar event is triggered and the text input focus clear effect is triggered`() = runTest {
        // Given
        `given the tested view model`()

        viewModel.effects.test {
            // When
            viewModel.onViewEvent(CreateViewEvent.OnDiscardClicked)
            // Then
            advanceUntilIdle()
            assertEquals(CreateViewModelEffect.DisplaySnackbar(R.string.snackbar_msg_changes_discarded), awaitItem())
            assertEquals(CreateViewModelEffect.OnNameTextInputFocusRequest, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when create button is clicked, if inputs are valid and creation succeeds, the correct snackbar event is triggered and the state is cleared`() = runTest {
        // Given
        `given the tested view model`()
        every {
            validateUser(name = "john", age = "30", email = "john@doe.com")
        } returns emptyList()
        coEvery {
            createUser(name = "john", age = "30", email = "john@doe.com")
        } returns Result.success(true)

        val expectedSnackbarEvent = CreateViewModelEffect.DisplaySnackbar(R.string.snackbar_msg_user_created)

        // Then
        viewModel.uiState.test {
            // initial state
            awaitItem().run {
                name `should be equal to` ""
                age `should be equal to` ""
                email `should be equal to` ""
            }
            // When
            viewModel.updateState(CreateUiState(name = "john", age = "30", email = "john@doe.com"))

            // updated state
            awaitItem().run {
                name `should be equal to` "john"
                age `should be equal to` "30"
                email `should be equal to` "john@doe.com"
            }

            viewModel.onViewEvent(CreateViewEvent.OnCreateClicked)

            // state after clearForm() is called
            awaitItem().run {
                name `should be equal to` ""
                age `should be equal to` ""
                email `should be equal to` ""
            }

            expectNoEvents()
        }

        viewModel.effectsChannel.receive() `should be equal to` expectedSnackbarEvent

        coVerifyOrder {
            validateUser("john", "30", "john@doe.com")
            createUser("john", "30", "john@doe.com")
        }
    }

    @Test
    fun `when create button is clicked, if inputs are valid but creation fails, the error snackbar event is triggered and the state is not cleared`() = runTest {
        // Given
        `given the tested view model`()
        every {
            validateUser(name = "john", age = "30", email = "john@doe.com")
        } returns emptyList()
        coEvery {
            createUser(name = "john", age = "30", email = "john@doe.com")
        } returns Result.failure(Exception("Can't add user"))

        val expectedSnackbarEvent = CreateViewModelEffect.DisplaySnackbar(R.string.error_create_generic)

        // Then
        viewModel.uiState.test {
            // initial state
            awaitItem().run {
                name `should be equal to` ""
                age `should be equal to` ""
                email `should be equal to` ""
            }
            // When
            viewModel.updateState(CreateUiState(name = "john", age = "30", email = "john@doe.com"))

            // updated state
            awaitItem().run {
                name `should be equal to` "john"
                age `should be equal to` "30"
                email `should be equal to` "john@doe.com"
            }

            viewModel.onViewEvent(CreateViewEvent.OnCreateClicked)

            viewModel.currentState.run {
                name `should be equal to` "john"
                age `should be equal to` "30"
                email `should be equal to` "john@doe.com"
            }

            expectNoEvents()
        }

        viewModel.effectsChannel.receive() `should be equal to` expectedSnackbarEvent

        coVerifyOrder {
            validateUser("john", "30", "john@doe.com")
            createUser("john", "30", "john@doe.com")
        }
    }

    @Test
    fun `when create button is clicked, if name field is empty the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`()
        val expectedErrorState = CreateResultType.Error.NameMandatory(R.string.error_create_mandatory_field)
        every {
            validateUser(name = "", age = any(), email = any())
        } returns listOf(expectedErrorState)

        // When
        viewModel.uiState.test {
            // Then
            awaitItem().errors.mandatoryNameError `should be equal to` null
            viewModel.onViewEvent(CreateViewEvent.OnCreateClicked)
            awaitItem().errors.mandatoryNameError `should be equal to` expectedErrorState.resId
            expectNoEvents()
        }

        verify(exactly = 1) {
            validateUser(name = "", age = any(), email = any())
        }

        coVerify(exactly = 0) {
            createUser(name = any(), age = any(), email = any())
        }
    }

    @Test
    fun `when create button is clicked, if age field is empty the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`()
        val expectedErrorState = CreateResultType.Error.AgeMandatory(R.string.error_create_mandatory_field)
        every {
            validateUser(name = any(), age = "", email = any())
        } returns listOf(expectedErrorState)

        // When
        viewModel.uiState.test {
            // Then
            awaitItem().errors.mandatoryAgeError `should be equal to` null
            viewModel.onViewEvent(CreateViewEvent.OnCreateClicked)
            awaitItem().errors.mandatoryAgeError `should be equal to` expectedErrorState.resId
            expectNoEvents()
        }

        verify(exactly = 1) {
            validateUser(name = any(), age = "", email = any())
        }

        coVerify(exactly = 0) {
            createUser(name = any(), age = any(), email = any())
        }
    }

    @Test
    fun `when create button is clicked, if age field is invalid the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`()
        val expectedErrorState = CreateResultType.Error.InvalidAge(R.string.error_create_invalid_age)
        every {
            validateUser(name = any(), age = "abc", email = any())
        } returns listOf(expectedErrorState)

        viewModel.updateState(CreateUiState(age = "abc"))

        // When
        viewModel.uiState.test {
            // Then
            awaitItem().errors.invalidAgeError `should be equal to` null
            viewModel.onViewEvent(CreateViewEvent.OnCreateClicked)
            awaitItem().errors.invalidAgeError `should be equal to` expectedErrorState.resId
            expectNoEvents()
        }

        verify(exactly = 1) {
            validateUser(name = any(), age = "abc", email = any())
        }

        coVerify(exactly = 0) {
            createUser(name = any(), age = any(), email = any())
        }
    }

    @Test
    fun `when create button is clicked, if email field is empty the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`()
        val expectedErrorState = CreateResultType.Error.EmailMandatory(R.string.error_create_mandatory_field)
        every {
            validateUser(name = any(), age = any(), email = "")
        } returns listOf(expectedErrorState)

        // When
        viewModel.uiState.test {
            // Then
            awaitItem().errors.mandatoryEmailError `should be equal to` null
            viewModel.onViewEvent(CreateViewEvent.OnCreateClicked)
            awaitItem().errors.mandatoryEmailError `should be equal to` expectedErrorState.resId
            expectNoEvents()
        }

        verify(exactly = 1) {
            validateUser(name = any(), age = any(), email = "")
        }

        coVerify(exactly = 0) {
            createUser(name = any(), age = any(), email = any())
        }
    }

    @Test
    fun `when create button is clicked, if multiple fields are empty the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`()
        val expectedNameErrorState = CreateResultType.Error.NameMandatory(R.string.error_create_mandatory_field)
        val expectedAgeErrorState = CreateResultType.Error.AgeMandatory(R.string.error_create_mandatory_field)
        val expectedEmailErrorState = CreateResultType.Error.EmailMandatory(R.string.error_create_mandatory_field)
        every {
            validateUser(name = "", age = "", email = "")
        } returns listOf(
            expectedNameErrorState,
            expectedAgeErrorState,
            expectedEmailErrorState,
        )

        // When
        viewModel.uiState.test {
            // Then
            awaitItem().errors.run {
                mandatoryNameError `should be equal to` null
                mandatoryAgeError `should be equal to` null
                invalidAgeError `should be equal to` null
                mandatoryEmailError `should be equal to` null
            }
            viewModel.onViewEvent(CreateViewEvent.OnCreateClicked)

            awaitItem().errors.mandatoryNameError `should be equal to` expectedNameErrorState.resId
            awaitItem().errors.run {
                mandatoryAgeError `should be equal to` expectedAgeErrorState.resId
                invalidAgeError `should be equal to` null
            }
            awaitItem().errors.mandatoryEmailError `should be equal to` expectedEmailErrorState.resId
            expectNoEvents()
        }

        verify(exactly = 1) {
            validateUser(name = "", age = "", email = "")
        }

        coVerify(exactly = 0) {
            createUser(name = any(), age = any(), email = any())
        }
    }

    @Test
    fun `if user id is not present in savedStateHandle, then edit mode is false in ui state`() = runTest {
        // Given
        `given the tested view model`()

        // When
        // Then
        viewModel.uiState.test {
            awaitItem().isEditMode `should be equal to` false
            expectNoEvents()
        }

        coVerify(exactly = 0) {
            getUser(any())
        }
    }

    @Test
    fun `if user id is present in savedStateHandle, then getUser use case is called and if successful, ui state is properly updated`() = runTest {
        // Given
        val expectedUserId = "3"
        val expectedUserName = "Audrey"
        val expectedUserAge = "25"
        val expectedUserEmail = "audrey@hepburn.com"
        coEvery {
            getUser("3")
        } returns Result.success(UserModel(
            id = "3",
            name = "Audrey",
            age = 25,
            email = "audrey@hepburn.com"
        ))

        `given the tested view model`(mapOf(Pair("userId", expectedUserId)))

        // When
        // Then
        viewModel.uiState.test {
            awaitItem().run {
                isEditMode `should be equal to` false
                name `should be equal to` ""
                age `should be equal to` ""
                email `should be equal to` ""
            }
            awaitItem().run {
                isEditMode `should be equal to` true
                name `should be equal to` expectedUserName
                age `should be equal to` expectedUserAge
                email `should be equal to` expectedUserEmail
            }
            expectNoEvents()
        }

        coVerify(exactly = 1) {
            getUser(expectedUserId)
        }
    }

    @Test
    fun `if user id is present in savedStateHandle, then getUser use case is called and if it fails, the error snackbar effect is called`() = runTest {
        // Given
        val expectedUserId = "3"
        coEvery {
            getUser("3")
        } returns Result.failure(Exception("some exception"))

        `given the tested view model`(mapOf(Pair("userId", expectedUserId)))

        // When
        // Then
        viewModel.uiState.test {
            awaitItem().run {
                isEditMode `should be equal to` false
                name `should be equal to` ""
                age `should be equal to` ""
                email `should be equal to` ""
            }
            expectNoEvents()
        }

        viewModel.effectsChannel.receive() `should be equal to` CreateViewModelEffect.DisplaySnackbar(resId = R.string.error_create_generic)

        coVerify(exactly = 1) {
            getUser(expectedUserId)
        }
    }

    @Test
    fun `when update button is clicked, if inputs are valid and update operation succeeds, the correct snackbar event is triggered and the state is cleared`() = runTest {
        // Given
        `given the tested view model`(mapOf(Pair("userId", "1")))
        `given that get user returns john doe of id 1`()
        every {
            validateUser(name = "john", age = "30", email = "john@doe.com")
        } returns emptyList()
        coEvery {
            updateUser(id = "1", name = "john", age = "30", email = "john@doe.com")
        } returns Result.success(true)

        val expectedSnackbarEvent = CreateViewModelEffect.DisplaySnackbar(R.string.snackbar_msg_user_updated)

        // Then
        viewModel.uiState.test {
            // initial state
            awaitItem().run {
                name `should be equal to` ""
                age `should be equal to` ""
                email `should be equal to` ""
            }

            // state after get user
            awaitItem().run {
                name `should be equal to` "john"
                age `should be equal to` "30"
                email `should be equal to` "john@doe.com"
            }

            // When
            viewModel.updateState(CreateUiState(name = "johnny", age = "31", email = "johnny@doe.com"))

            // updated state
            awaitItem().run {
                name `should be equal to` "johnny"
                age `should be equal to` "31"
                email `should be equal to` "johnny@doe.com"
            }

            viewModel.onViewEvent(CreateViewEvent.OnUpdateClicked)

            // state after clearForm() is called
            awaitItem().run {
                name `should be equal to` ""
                age `should be equal to` ""
                email `should be equal to` ""
            }

            expectNoEvents()
        }

        viewModel.effectsChannel.receive() `should be equal to` expectedSnackbarEvent

        coVerifyOrder {
            getUser("1")
            validateUser("johnny", "31", "johnny@doe.com")
            updateUser("1","johnny", "31", "johnny@doe.com")
        }
    }

    @Test
    fun `when update button is clicked, if inputs are valid but update operation fails, the error snackbar event is triggered and the state is not cleared`() = runTest {
        // Given
        `given the tested view model`(mapOf(Pair("userId", "1")))
        `given that get user returns john doe of id 1`()
        every {
            validateUser(name = "johnny", age = "31", email = "johnny@doe.com")
        } returns emptyList()
        coEvery {
            updateUser(id = "1", name = "johnny", age = "31", email = "johnny@doe.com")
        } returns Result.failure(Exception("Can't update user"))

        val expectedSnackbarEvent = CreateViewModelEffect.DisplaySnackbar(R.string.error_create_generic)

        // Then
        viewModel.uiState.test {
            // initial state
            awaitItem().run {
                name `should be equal to` ""
                age `should be equal to` ""
                email `should be equal to` ""
            }

            // state after GetUser
            awaitItem().run {
                name `should be equal to` "john"
                age `should be equal to` "30"
                email `should be equal to` "john@doe.com"
            }

            // When
            viewModel.updateState(CreateUiState(name = "johnny", age = "31", email = "johnny@doe.com"))

            // updated state
            awaitItem().run {
                name `should be equal to` "johnny"
                age `should be equal to` "31"
                email `should be equal to` "johnny@doe.com"
            }

            viewModel.onViewEvent(CreateViewEvent.OnUpdateClicked)

            viewModel.currentState.run {
                name `should be equal to` "johnny"
                age `should be equal to` "31"
                email `should be equal to` "johnny@doe.com"
            }

            expectNoEvents()
        }

        viewModel.effectsChannel.receive() `should be equal to` expectedSnackbarEvent

        coVerifyOrder {
            getUser("1")
            validateUser("johnny", "31", "johnny@doe.com")
            updateUser("1","johnny", "31", "johnny@doe.com")
        }
    }

    @Test
    fun `when update button is clicked, if name field is empty the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`(mapOf(Pair("userId", "1")))
        `given that get user returns john doe of id 1`()

        val expectedErrorState = CreateResultType.Error.NameMandatory(R.string.error_create_mandatory_field)
        every {
            validateUser(name = "", age = "30", email = "john@doe.com")
        } returns listOf(expectedErrorState)

        // When
        viewModel.uiState.test {
            // initial state
            awaitItem().errors.mandatoryNameError `should be equal to` null
            // state after GetUser
            awaitItem().errors.mandatoryNameError `should be equal to` null
            // Then
            viewModel.updateState(viewModel.currentState.copy(name = ""))
            awaitItem().errors.mandatoryNameError `should be equal to` null

            viewModel.onViewEvent(CreateViewEvent.OnUpdateClicked)
            awaitItem().errors.mandatoryNameError `should be equal to` expectedErrorState.resId

            expectNoEvents()
        }

        coVerifyOrder {
            getUser("1")
            validateUser(name = "", age = "30", email = "john@doe.com")
        }
        coVerify(exactly = 0) {
            updateUser(id = any(), name = any(), age = any(), email = any())
        }
    }

    @Test
    fun `when update button is clicked, if age field is empty the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`(mapOf(Pair("userId", "1")))
        `given that get user returns john doe of id 1`()

        val expectedErrorState = CreateResultType.Error.AgeMandatory(R.string.error_create_mandatory_field)
        every {
            validateUser(name = "john", age = "", email = "john@doe.com")
        } returns listOf(expectedErrorState)

        // When
        viewModel.uiState.test {
            // initial state
            awaitItem().errors.mandatoryAgeError `should be equal to` null
            // state after GetUser
            awaitItem().errors.mandatoryAgeError `should be equal to` null
            // Then
            viewModel.updateState(viewModel.currentState.copy(age = ""))
            awaitItem().errors.mandatoryAgeError `should be equal to` null

            viewModel.onViewEvent(CreateViewEvent.OnUpdateClicked)
            awaitItem().errors.mandatoryAgeError `should be equal to` expectedErrorState.resId

            expectNoEvents()
        }

        coVerifyOrder {
            getUser("1")
            validateUser(name = "john", age = "", email = "john@doe.com")
        }
        coVerify(exactly = 0) {
            updateUser(id = any(), name = any(), age = any(), email = any())
        }
    }

    @Test
    fun `when update button is clicked, if age field is invalid the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`(mapOf(Pair("userId", "1")))
        `given that get user returns john doe of id 1`()

        val expectedErrorState = CreateResultType.Error.InvalidAge(R.string.error_create_invalid_age)
        every {
            validateUser(name = "john", age = "abc", email = "john@doe.com")
        } returns listOf(expectedErrorState)

        // When
        viewModel.uiState.test {
            // initial state
            awaitItem().errors.invalidAgeError `should be equal to` null
            // state after GetUser
            awaitItem().errors.invalidAgeError `should be equal to` null
            // Then
            viewModel.updateState(viewModel.currentState.copy(age = "abc"))
            awaitItem().errors.invalidAgeError `should be equal to` null

            viewModel.onViewEvent(CreateViewEvent.OnUpdateClicked)
            awaitItem().errors.invalidAgeError `should be equal to` expectedErrorState.resId

            expectNoEvents()
        }

        coVerifyOrder {
            getUser("1")
            validateUser(name = "john", age = "abc", email = "john@doe.com")
        }
        coVerify(exactly = 0) {
            updateUser(id = any(), name = any(), age = any(), email = any())
        }
    }

    @Test
    fun `when update button is clicked, if email field is empty the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`(mapOf(Pair("userId", "1")))
        `given that get user returns john doe of id 1`()

        val expectedErrorState = CreateResultType.Error.EmailMandatory(R.string.error_create_mandatory_field)
        every {
            validateUser(name = "john", age = "30", email = "")
        } returns listOf(expectedErrorState)

        // When
        viewModel.uiState.test {
            // initial state
            awaitItem().errors.mandatoryEmailError `should be equal to` null
            // state after GetUser
            awaitItem().errors.mandatoryEmailError `should be equal to` null
            // Then
            viewModel.updateState(viewModel.currentState.copy(email = ""))
            awaitItem().errors.mandatoryEmailError `should be equal to` null

            viewModel.onViewEvent(CreateViewEvent.OnUpdateClicked)
            awaitItem().errors.mandatoryEmailError `should be equal to` expectedErrorState.resId

            expectNoEvents()
        }

        coVerifyOrder {
            getUser("1")
            validateUser(name = "john", age = "30", email = "")
        }
        coVerify(exactly = 0) {
            updateUser(id = any(), name = any(), age = any(), email = any())
        }
    }

    @Test
    fun `when update button is clicked, if multiple fields are empty the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`(mapOf(Pair("userId", "1")))
        `given that get user returns john doe of id 1`()

        val expectedNameErrorState = CreateResultType.Error.NameMandatory(R.string.error_create_mandatory_field)
        val expectedAgeErrorState = CreateResultType.Error.AgeMandatory(R.string.error_create_mandatory_field)
        val expectedEmailErrorState = CreateResultType.Error.EmailMandatory(R.string.error_create_mandatory_field)
        every {
            validateUser(name = "", age = "", email = "")
        } returns listOf(
            expectedNameErrorState,
            expectedAgeErrorState,
            expectedEmailErrorState,
        )

        // When
        viewModel.uiState.test {
            // initial state
            awaitItem().errors.run {
                mandatoryNameError `should be equal to` null
                mandatoryAgeError `should be equal to` null
                invalidAgeError `should be equal to` null
                mandatoryEmailError `should be equal to` null
            }

            // after GetUser
            awaitItem().errors.run {
                mandatoryNameError `should be equal to` null
                mandatoryAgeError `should be equal to` null
                invalidAgeError `should be equal to` null
                mandatoryEmailError `should be equal to` null
            }

            // Then
            viewModel.updateState(viewModel.currentState.copy(name = "", age = "", email = ""))
            awaitItem().errors.run {
                mandatoryNameError `should be equal to` null
                mandatoryAgeError `should be equal to` null
                invalidAgeError `should be equal to` null
                mandatoryEmailError `should be equal to` null
            }

            viewModel.onViewEvent(CreateViewEvent.OnUpdateClicked)
            awaitItem().errors.mandatoryNameError `should be equal to` expectedNameErrorState.resId
            awaitItem().errors.run {
                mandatoryAgeError `should be equal to` expectedAgeErrorState.resId
                invalidAgeError `should be equal to` null
            }
            awaitItem().errors.mandatoryEmailError `should be equal to` expectedEmailErrorState.resId
        }

        coVerifyOrder {
            getUser("1")
            validateUser(name = "", age = "", email = "")
        }
        coVerify(exactly = 0) {
            updateUser(id = any(), name = any(), age = any(), email = any())
        }
    }

    @After
    fun tearDown() {
        viewModel.effectsChannel.cancel()
    }

    private fun `given that get user returns john doe of id 1`() {
        coEvery {
            getUser(userId = "1")
        } returns Result.success(UserModel(id = "1", name = "john", age = 30, email = "john@doe.com"))
    }

    private fun `given the tested view model`(savedStateHandleArgs: Map<String, Any?> = emptyMap()) {
        viewModel = CreateViewModel(
            savedStateHandle = SavedStateHandle(savedStateHandleArgs),
            validateUser = validateUser,
            createUser = createUser,
            updateUser = updateUser,
            getUser = getUser,
        )
    }
}