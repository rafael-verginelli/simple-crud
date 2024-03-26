package com.rafver.create.ui

import app.cash.turbine.test
import com.rafver.create.R
import com.rafver.create.ui.models.CreateViewEvent
import com.rafver.create.util.TestCoroutineRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

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
    fun `when discard button is clicked, the correct snackbar event is triggered`() = runTest {
        // Given
        `given the tested view model`()

        // ToDo: test use cases once implemented
        viewModel.snackbarEvent.test {
            // When
            viewModel.onViewEvent(CreateViewEvent.OnDiscardClicked)
            // Then
            advanceUntilIdle()
            assertEquals(R.string.snackbar_msg_changes_discarded, viewModel.snackbarEvent.value?.peekContent())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when create button is clicked, the correct snackbar event is triggered`() = runTest {
        // Given
        `given the tested view model`()

        // When
        viewModel.onViewEvent(CreateViewEvent.OnCreateClicked)

        advanceUntilIdle()

        // Then
        // ToDo: test use cases once implemented
        // ToDo: remove hardcoded text
        assertEquals("User created!", viewModel.snackbarEvent.value?.peekContent())
    }

    private fun `given the tested view model`() {
        viewModel = CreateViewModel()
    }
}