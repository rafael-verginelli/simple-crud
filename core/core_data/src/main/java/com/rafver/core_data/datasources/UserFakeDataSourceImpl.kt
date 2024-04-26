package com.rafver.core_data.datasources

import com.rafver.core_data.dtos.UserDTO
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject

/**
 * This is a fake data source to mimic basic CRUD operations.
 * Replace this file by a real Data Source implementation.
 */
class UserFakeDataSourceImpl @Inject constructor(): UserFakeDataSource {

    companion object {
        private val userList = mutableListOf<UserDTO>()
    }

    private val fakeDelays = listOf(500L, 1000L, 2000L)

    init {
        println("UserFakeDataSourceImpl INIT!!!!")
    }

    override suspend fun getUser(userId: String): Result<UserDTO> {
        delay(fakeDelays.shuffled().first())
        val user = userList.firstOrNull { it.id == userId }
        return if(user != null) Result.success(user) else Result.failure(Exception("User not found."))
    }

    override suspend fun getUserList(): Result<List<UserDTO>> {
        delay(fakeDelays.shuffled().first())
        return Result.success(userList)
    }

    override suspend fun createUser(name: String, age: Int, email: String): Result<Boolean> {
        delay(fakeDelays.shuffled().first())
        val id = UUID.randomUUID().toString()
        val userDto = UserDTO(id, name, age, email)
        userList.add(userDto)
        return Result.success(true)
    }

    override suspend fun updateUser(
        id: String,
        name: String,
        age: Int,
        email: String
    ): Result<Boolean> {
        delay(fakeDelays.shuffled().first())
        val userIndex = userList.indexOfFirst { it.id == id }
        if(userIndex == -1) {
            return Result.failure(Exception("User not found."))
        }
        userList.removeAt(userIndex)
        val updatedUserDto = UserDTO(id, name, age, email)

        userList.add(updatedUserDto)
        return Result.success(true)
    }

    override suspend fun deleteUser(userId: String): Result<Boolean> {
        delay(fakeDelays.shuffled().first())
        val userIndex = userList.indexOfFirst { it.id == userId }
        if(userIndex == -1) {
            return Result.failure(Exception("User not found."))
        }
        userList.removeAt(userIndex)
        return Result.success(true)
    }

}