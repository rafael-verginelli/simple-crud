package com.rafver.core_data.repositories

import com.rafver.core_data.dtos.UserDTO

interface UserRepository {
    fun getUser(userId: String): Result<UserDTO>
    fun getUserList(): Result<List<UserDTO>>
    fun createUser(name: String, age: Int, email: String): Result<Boolean>
    fun updateUser(id: String, name: String, age: Int, email: String): Result<Boolean>
}