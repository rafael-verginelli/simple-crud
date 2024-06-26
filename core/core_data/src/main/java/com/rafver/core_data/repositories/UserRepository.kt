package com.rafver.core_data.repositories

import com.rafver.core_data.dtos.UserDTO

interface UserRepository {
    suspend fun getUser(userId: String): Result<UserDTO>
    suspend fun getUserList(): Result<List<UserDTO>>
    suspend fun createUser(name: String, age: Int, email: String): Result<Boolean>
    suspend fun updateUser(id: String, name: String, age: Int, email: String): Result<Boolean>
    suspend fun deleteUser(userId: String): Result<Boolean>
}