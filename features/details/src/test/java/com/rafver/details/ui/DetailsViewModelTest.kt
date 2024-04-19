package com.rafver.details.ui

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.expectNoEvents
import app.cash.turbine.test
import com.rafver.core_domain.models.UserModel
import com.rafver.core_testing.util.TestCoroutineRule
import com.rafver.core_ui.models.toUiModel
import com.rafver.details.R
import com.rafver.details.domain.usecases.GetUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should not be`
import org.junit.Rule
import org.junit.Test

class DetailsViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getUser: GetUser = mockk(relaxed = true)

    private lateinit var viewModel: DetailsViewModel

    @Test
    fun `when the view model is initialized, if DetailsArgs is invalid, an exception is thrown`() = runTest {
        // Given
        val invalidSavedStateHandle = SavedStateHandle(emptyMap())
        val expectedException = IllegalStateException("Required value was null.")

        var exception: Exception? = null
        try {
            `given the tested view model`(invalidSavedStateHandle)
        } catch (e: Exception) {
            exception = e
        }

        // When
        // Then
        exception `should not be` null
        exception `should be instance of` IllegalStateException::class
        exception!!.message `should be equal to` expectedException.message
    }

    @Test
    fun `when the view model is initialized, if get user operation succeeds, the ui state is properly updated`() = runTest {
        // Given
        val validSavedStateHandle = SavedStateHandle(mapOf(Pair("userId", "1")))
        `given the tested view model`(validSavedStateHandle)
        val expectedUserModel = UserModel(id = "1", name = "john", age = 20, email = "john@doe.com")
        val expectedUserUiModel = expectedUserModel.toUiModel()

        every { getUser("1") } returns Result.success(expectedUserModel)

        // When
        // Then
        viewModel.uiState.test {
            awaitItem().userModel `should be equal to` null
            awaitItem().userModel `should be equal to` expectedUserUiModel
            expectNoEvents()
        }

        verify(exactly = 1) {
            getUser("1")
        }
    }

    @Test
    fun `when the view model is initialized, if get user operation fails, an error snackbar is shown`() = runTest {
        // Given
        val validSavedStateHandle = SavedStateHandle(mapOf(Pair("userId", "1")))
        `given the tested view model`(validSavedStateHandle)

        every { getUser("1") } returns Result.failure(Exception("some exception"))

        // When
        // Then
        viewModel.uiState.test {
            awaitItem().userModel `should be equal to` null
            expectNoEvents()
        }

        viewModel.effectsChannel.receive() `should be equal to`
                DetailsViewModelEffect.DisplaySnackbar(R.string.error_details_generic)
        viewModel.effectsChannel.expectNoEvents()

        verify(exactly = 1) {
            getUser("1")
        }
    }

    // ToDo: test OnDeleteClicked event
    // ToDo: test OnEditClicked event

    private fun `given the tested view model`(savedStateHandle: SavedStateHandle) {
        viewModel = DetailsViewModel(
            savedStateHandle = savedStateHandle,
            getUser = getUser,
        )
    }

}