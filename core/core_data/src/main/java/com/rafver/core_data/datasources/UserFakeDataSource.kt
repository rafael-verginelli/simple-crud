package com.rafver.core_data.datasources

import com.rafver.core_data.dtos.UserDTO

/**
 * This is a fake data source to mimic basic CRUD operations.
 * Replace this file by a real Data Source abstraction.
 */
interface UserFakeDataSource {
    suspend fun getUser(userId: String): Result<UserDTO>
    suspend fun getUserList(): Result<List<UserDTO>>
    suspend fun createUser(name: String, age: Int, email: String): Result<Boolean>
    suspend fun updateUser(id: String, name: String, age: Int, email: String): Result<Boolean>
    suspend fun deleteUser(userId: String): Result<Boolean>
}