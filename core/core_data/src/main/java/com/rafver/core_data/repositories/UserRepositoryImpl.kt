package com.rafver.core_data.repositories

import com.rafver.core_data.dtos.UserDTO
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(): UserRepository {

    override fun getUser(userId: String): Result<UserDTO> {
        // ToDo: To Be Implemented
        val userList = getUserList().getOrNull()
        val user = userList?.firstOrNull { user -> user.id == userId }

        return if(user != null) Result.success(user) else Result.failure(Exception("User not found"))
    }

    override fun getUserList(): Result<List<UserDTO>> {
        // ToDo: To Be Implemented
        return Result.success(listOf(
            UserDTO("1", "John", 30, "john@doe.com"),
            UserDTO("2", "Audrey", 40, "audrey@hepburn.com"),
            UserDTO("3", "Jane", 32, "jane@doe.com"),
            UserDTO("4", "James", 50, "james@dean.com"),
        ))
    }

    override fun createUser(name: String, age: Int, email: String): Result<Boolean> {
        // ToDo: To Be Implemented
        return Result.success(true)
    }

    override fun updateUser(id: String, name: String, age: Int, email: String): Result<Boolean> {
        // ToDo: To Be Implemented
        return Result.success(true)
    }

    override fun deleteUser(id: String): Result<Boolean> {
        // ToDo: To Be Implemented
        return Result.success(true)
    }
}