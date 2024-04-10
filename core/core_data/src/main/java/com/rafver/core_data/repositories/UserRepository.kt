package com.rafver.core_data.repositories

import com.rafver.core_domain.models.UserModel

interface UserRepository {
    fun getUserList(): Result<List<UserModel>>
    fun createUser(name: String, age: Int, email: String): Result<Boolean>
}