package com.rafver.core_data.repositories

import com.rafver.core_domain.models.UserModel
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(): UserRepository {
    override fun getUserList(): Result<List<UserModel>> {
        // ToDo: To Be Implemented
        return Result.success(listOf(
            UserModel("1", "John", 30, "john@doe.com"),
            UserModel("2", "Audrey", 40, "audrey@hepburn.com"),
            UserModel("3", "Jane", 32, "jane@doe.com"),
            UserModel("4", "James", 50, "james@dean.com"),
        ))
    }

    override fun createUser(name: String, age: Int, email: String): Result<Boolean> {
        // ToDo: To Be Implemented
        return Result.success(true)
    }

}