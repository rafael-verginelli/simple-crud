package com.rafver.details.ui

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.expectNoEvents
import app.cash.turbine.test
import com.rafver.core_domain.models.UserModel
import com.rafver.core_testing.util.TestCoroutineRule
import com.rafver.core_ui.models.toUiModel
import com.rafver.details.R
import com.rafver.core_domain.usecases.GetUser
import com.rafver.details.domain.usecases.DeleteUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
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
    private val deleteUser: DeleteUser = mockk(relaxed = true)

    private lateinit var viewModel: DetailsViewModel

    @Test
    fun `when the view model is initialized, if DetailsArgs is invalid, an exception is thrown`() = runTest {
        // Given
        val invalidSavedStateHandleArgs = emptyMap<String, Any>()
        val expectedException = IllegalStateException("Required value was null.")

        var exception: Exception? = null
        try {
            `given the tested view model`(invalidSavedStateHandleArgs)
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
        `given the tested view model`(mapOf(Pair("userId", "1")))
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
        `given the tested view model`(mapOf(Pair("userId", "1")))

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
    @Test
    fun `when edit button is clicked, the correct effect is emitted`() = runTest {
        // Given
        val expectedUserId = "1"
        `given the tested view model`(mapOf(Pair("userId", "1")))

        every {
            getUser("1")
        } returns Result.success(
            UserModel(id = "1", name = "john", age = 20, email = "john@doe.com")
        )

        // When
        viewModel.uiState.test {
            viewModel.onViewEvent(DetailsViewEvent.OnEditClicked)
            cancelAndIgnoreRemainingEvents()
        }

        // Then
        viewModel.effectsChannel.receive() `should be equal to` DetailsViewModelEffect.NavigateToEdit(expectedUserId)
    }

    @Test
    fun `when delete button is clicked, then showDeleteDialog state is set to true`() = runTest {
        // Given
        `given the tested view model`(mapOf(Pair("userId", "1")))

        every {
            getUser("1")
        } returns Result.success(
            UserModel(id = "1", name = "john", age = 20, email = "john@doe.com")
        )

        viewModel.uiState.test {
            // initial state
            awaitItem().showDeleteDialog `should be equal to` false

            // state after GetUser
            awaitItem().showDeleteDialog `should be equal to` false

            // When
            viewModel.onViewEvent(DetailsViewEvent.OnDeleteClicked)
            awaitItem().showDeleteDialog `should be equal to` true

            expectNoEvents()
        }
    }

    @Test
    fun `when delete dialog is cancelled, then showDeleteDialog state is set to false`() = runTest {
        // Given
        `given the tested view model`(mapOf(Pair("userId", "1")))

        every {
            getUser("1")
        } returns Result.success(
            UserModel(id = "1", name = "john", age = 20, email = "john@doe.com")
        )

        viewModel.uiState.test {
            // skip initial state and state after GetUser
            skipItems(2)

            // forcing state change
            viewModel.updateState(viewModel.currentState.copy(showDeleteDialog = true))
            awaitItem().showDeleteDialog `should be equal to` true

            // When
            viewModel.onViewEvent(DetailsViewEvent.OnDeleteCancelClicked)
            // Then
            awaitItem().showDeleteDialog `should be equal to` false

            expectNoEvents()
        }
    }

    @Test
    fun `when delete dialog is confirmed, if deletion fails, then showDeleteDialog state is set to false and snackbar effect is emitted`() = runTest {
        // Given
        `given the tested view model`(mapOf(Pair("userId", "1")))
        every { deleteUser("1") } returns Result.failure(Exception("Some exception"))

        val expectedSnackbarEffect = DetailsViewModelEffect.DisplaySnackbar(R.string.error_details_generic)

        every {
            getUser("1")
        } returns Result.success(
            UserModel(id = "1", name = "john", age = 20, email = "john@doe.com")
        )

        viewModel.uiState.test {
            // skip initial state and state after GetUser
            skipItems(2)

            // forcing state change
            viewModel.updateState(viewModel.currentState.copy(showDeleteDialog = true))
            awaitItem().showDeleteDialog `should be equal to` true

            // When
            viewModel.onViewEvent(DetailsViewEvent.OnDeleteConfirmationClicked)
            // Then
            awaitItem().showDeleteDialog `should be equal to` false

            expectNoEvents()
        }

        viewModel.effectsChannel.receive() `should be equal to` expectedSnackbarEffect

        verifyOrder {
            getUser("1")
            deleteUser("1")
        }
    }

    @Test
    fun `when delete dialog is confirmed, if deletion succeeds, then showDeleteDialog state is set to false and navigate up effect is emitted`() = runTest {
        // Given
        `given the tested view model`(mapOf(Pair("userId", "1")))
        every { deleteUser("1") } returns Result.success(true)

        val expectedEffect = DetailsViewModelEffect.NavigateUp

        every {
            getUser("1")
        } returns Result.success(
            UserModel(id = "1", name = "john", age = 20, email = "john@doe.com")
        )

        viewModel.uiState.test {
            // skip initial state and state after GetUser
            skipItems(2)

            // forcing state change
            viewModel.updateState(viewModel.currentState.copy(showDeleteDialog = true))
            awaitItem().showDeleteDialog `should be equal to` true

            // When
            viewModel.onViewEvent(DetailsViewEvent.OnDeleteConfirmationClicked)
            // Then
            awaitItem().showDeleteDialog `should be equal to` false

            expectNoEvents()
        }

        viewModel.effectsChannel.receive() `should be equal to` expectedEffect

        verifyOrder {
            getUser("1")
            deleteUser("1")
        }
    }

    private fun `given the tested view model`(savedStateHandleArgs: Map<String, Any?> = emptyMap()) {
        viewModel = DetailsViewModel(
            savedStateHandle = SavedStateHandle(savedStateHandleArgs),
            getUser = getUser,
            deleteUser = deleteUser,
        )
    }

}