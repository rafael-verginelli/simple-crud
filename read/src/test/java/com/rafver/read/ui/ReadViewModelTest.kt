package com.rafver.read.ui

import app.cash.turbine.test
import com.rafver.core_domain.models.UserModel
import com.rafver.core_testing.util.TestCoroutineRule
import com.rafver.read.domain.usecases.GetUserList
import com.rafver.read.ui.models.UserUiModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should not be equal to`
import org.junit.Rule
import org.junit.Test

class ReadViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getUserList: GetUserList = mockk(relaxed = true)

    private lateinit var viewModel: ReadViewModel

    @Test
    fun `when view model is initialized, user list is loaded and ui state is correctly updated`() = runTest {
        // Given
        `given the tested view model`()
        every { getUserList() } returns Result.success(MOCK_USERS)
        val expectedUserList = MOCK_USERS_UI

        // When
        viewModel.uiState.test {
            // Then
            awaitItem().userList `should be equal to` null

            awaitItem().run {
                userList?.size `should be equal to` expectedUserList.size
                userList.compareWith(expectedUserList)
            }

            expectNoEvents()
        }

        verify(exactly = 1) {
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