package com.rafver.create.ui

import app.cash.turbine.test
import com.rafver.create.R
import com.rafver.create.data.CreateResultType
import com.rafver.create.domain.usecases.ValidateUser
import com.rafver.create.ui.models.CreateUiState
import com.rafver.create.ui.models.CreateViewEvent
import com.rafver.create.ui.models.CreateViewModelEffect
import com.rafver.create.util.TestCoroutineRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
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

    private lateinit var viewModel: CreateViewModel

    @Test
    fun `when user inputs a new name, the ui state is properly updated`() = runTest {
        // Given
        `given the tested view model`()
        val expectedName = "john"

        // When
        viewModel.onViewEvent(CreateViewEvent.OnNameChanged("john"))

        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            assertEquals(expectedName, awaitItem().name)
        }
    }

    @Test
    fun `when user inputs a new age, the ui state is properly updated`() = runTest {
        // Given
        `given the tested view model`()
        val expectedAge = "20"

        // When
        viewModel.onViewEvent(CreateViewEvent.OnAgeChanged("20"))

        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            assertEquals(expectedAge, awaitItem().age)
        }
    }

    @Test
    fun `when user inputs a new email, the ui state is properly updated`() = runTest {
        // Given
        `given the tested view model`()
        val expectedEmail = "john@doe.com"

        // When
        viewModel.onViewEvent(CreateViewEvent.OnEmailChanged("john@doe.com"))

        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            assertEquals(expectedEmail, awaitItem().email)
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
    fun `when create button is clicked, if inputs are valid, the correct snackbar event is triggered and the state is cleared`() = runTest {
        // Given
        `given the tested view model`()
        every {
            validateUser(name = "john", age = "30", email = "john@doe.com")
        } returns CreateResultType.Ok

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

        verify(exactly = 1) {
            validateUser("john", "30", "john@doe.com")
        }
    }

    @Test
    fun `when create button is clicked, if name field is empty the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`()
        every {
            validateUser(name = "", age = any(), email = any())
        } returns CreateResultType.Error.NameMandatory

        // When
        viewModel.uiState.test {
            // Then
            awaitItem().errors.mandatoryNameError `should be equal to` null
            viewModel.onViewEvent(CreateViewEvent.OnCreateClicked)
            awaitItem().errors.mandatoryNameError `should be equal to` CreateResultType.Error.NameMandatory
        }

        verify(exactly = 1) {
            validateUser(name = "", age = any(), email = any())
        }
    }

    @Test
    fun `when create button is clicked, if age field is empty the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`()
        every {
            validateUser(name = any(), age = "", email = any())
        } returns CreateResultType.Error.AgeMandatory

        // When
        viewModel.uiState.test {
            // Then
            awaitItem().errors.mandatoryAgeError `should be equal to` null
            viewModel.onViewEvent(CreateViewEvent.OnCreateClicked)
            awaitItem().errors.mandatoryAgeError `should be equal to` CreateResultType.Error.AgeMandatory
        }

        verify(exactly = 1) {
            validateUser(name = any(), age = "", email = any())
        }
    }

    @Test
    fun `when create button is clicked, if age field is invalid the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`()
        every {
            validateUser(name = any(), age = any(), email = any())
        } returns CreateResultType.Error.InvalidAge

        // When
        viewModel.uiState.test {
            // Then
            awaitItem().errors.invalidAgeError `should be equal to` null
            viewModel.onViewEvent(CreateViewEvent.OnCreateClicked)
            advanceUntilIdle()
            awaitItem().errors.invalidAgeError `should be equal to` CreateResultType.Error.InvalidAge
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) {
            validateUser(name = any(), age = any(), email = any())
        }
    }

    @Test
    fun `when create button is clicked, if email field is empty the ui state is updated with the correct error state`() = runTest {
        // Given
        `given the tested view model`()
        every {
            validateUser(name = "", age = any(), email = any())
        } returns CreateResultType.Error.EmailMandatory

        // When
        viewModel.uiState.test {
            // Then
            awaitItem().errors.mandatoryEmailError `should be equal to` null
            viewModel.onViewEvent(CreateViewEvent.OnCreateClicked)
            awaitItem().errors.mandatoryEmailError `should be equal to` CreateResultType.Error.EmailMandatory
        }

        verify(exactly = 1) {
            validateUser(name = any(), age = any(), email = "")
        }
    }

    @After
    fun tearDown() {
        viewModel.effectsChannel.cancel()
    }

    private fun `given the tested view model`() {
        viewModel = CreateViewModel(
            validateUser = validateUser,
        )
    }
}