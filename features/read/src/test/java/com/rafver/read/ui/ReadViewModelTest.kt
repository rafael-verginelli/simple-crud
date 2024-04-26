@file:OptIn(ExperimentalCoroutinesApi::class)

package com.rafver.read.ui

import app.cash.turbine.test
import com.rafver.core_domain.models.UserModel
import com.rafver.core_testing.util.TestCoroutineRule
import com.rafver.core_ui.models.UserUiModel
import com.rafver.read.R
import com.rafver.read.domain.usecases.GetUserList
import com.rafver.read.ui.models.ReadViewModelEffect
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should not be equal to`
import org.junit.After
import org.junit.Rule
import org.junit.Test

class ReadViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getUserList: GetUserList = mockk(relaxed = true)

    private lateinit var viewModel: ReadViewModel

    @Test
    fun `when view model is initialized, try to get user list and if failed, the correct snackbar event is triggered`() = runTest {
        // Given
        `given the tested view model`()
        coEvery { getUserList() } returns Result.failure(Exception("Some exception"))
        val expectedSnackbarEvent = ReadViewModelEffect.DisplaySnackbar(R.string.error_read_generic)

        // When
        viewModel.uiState.test {
            // Then
            // initial State
            awaitItem().run {
                userList `should be equal to` null
                loading `should be equal to` false
            }
            advanceUntilIdle()
            awaitItem().run {
                userList `should be equal to` null
                loading `should be equal to` true
            }
            advanceUntilIdle()
            awaitItem().run {
                userList `should be equal to` null
                loading `should be equal to` false
            }
            expectNoEvents()
        }

        viewModel.effectsChannel.receive() `should be equal to` expectedSnackbarEvent

        coVerify(exactly = 1) {
            getUserList()
        }
    }

    @Test
    fun `when view model is initialized, try to get user list and if successful, the ui state is correctly updated`() = runTest {
        // Given
        `given the tested view model`()
        coEvery { getUserList() } returns Result.success(MOCK_USERS)
        val expectedUserList = MOCK_USERS_UI

        // When
        viewModel.uiState.test {
            // Then
            // initial state
            awaitItem().run {
                userList `should be equal to` null
                loading `should be equal to` false
            }
            advanceUntilIdle()
            awaitItem().run {
                userList `should be equal to` null
                loading `should be equal to` true
            }
            advanceUntilIdle()
            awaitItem().run {
                userList?.size `should be equal to` expectedUserList.size
                loading `should be equal to` false
                userList.compareWith(expectedUserList)
            }

            expectNoEvents()
        }

        coVerify(exactly = 1) {
            getUserList()
        }
    }

    @Test
    fun `when view model is initialized, try to get user list and if successful, the ui state is correctly updated, even if the request result is empty`() = runTest {
        // Given
        `given the tested view model`()
        coEvery { getUserList() } returns Result.success(emptyList())
        val expectedUserList = emptyList<UserUiModel>()

        // When
        viewModel.uiState.test {
            // Then
            // initial state
            awaitItem().run {
                userList `should be equal to` null
                loading `should be equal to` false
            }
            advanceUntilIdle()
            awaitItem().run {
                userList `should be equal to` null
                loading `should be equal to` true
            }
            advanceUntilIdle()
            awaitItem().run {
                userList `should be equal to` expectedUserList
                loading `should be equal to` false
            }

            expectNoEvents()
        }

        coVerify(exactly = 1) {
            getUserList()
        }
    }

    private fun List<UserUiModel>?.compareWith(other: List<UserUiModel>?) {
        if(this == null) {
            other `should be equal to` null
            return
        }

        other `should not be equal to` null

        forEachIndexed { index, user ->
            user.id `should be equal to` other!![index].id
            user.name `should be equal to` other[index].name
            user.age `should be equal to` other[index].age
            user.email `should be equal to` other[index].email
        }
    }

    @After
    fun tearDown() {
        viewModel.effectsChannel.cancel()
    }

    private fun `given the tested view model`() {
        viewModel = ReadViewModel(getUserList = getUserList)
    }

    private val MOCK_USERS = listOf(
        UserModel("1", "John", 30, "john@doe.com"),
        UserModel("2", "Audrey", 40, "audrey@hepburn.com"),
        UserModel("3", "Jane", 32, "jane@doe.com"),
        UserModel("4", "James", 50, "james@dean.com"),
    )

    private val MOCK_USERS_UI = listOf(
        UserUiModel("1", "John", 30, "john@doe.com"),
        UserUiModel("2", "Audrey", 40, "audrey@hepburn.com"),
        UserUiModel("3", "Jane", 32, "jane@doe.com"),
        UserUiModel("4", "James", 50, "james@dean.com"),
    )
}