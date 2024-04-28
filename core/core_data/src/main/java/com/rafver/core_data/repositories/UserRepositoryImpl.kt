package com.rafver.core_data.repositories

import com.rafver.core_data.datasources.UserFakeDataSource
import com.rafver.core_data.dtos.UserDTO
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userFakeDataSource: UserFakeDataSource)
    : UserRepository {

    override suspend fun getUser(userId: String)
            : Result<UserDTO> = userFakeDataSource.getUser(userId = userId)

    override suspend fun getUserList(): Result<List<UserDTO>> = userFakeDataSource.getUserList()

    override suspend fun createUser(name: String, age: Int, email: String)
            : Result<Boolean> = userFakeDataSource.createUser(name = name, age = age, email = email)

    override suspend fun updateUser(id: String, name: String, age: Int, email: String)
            : Result<Boolean> = userFakeDataSource.updateUser(
        id = id,
        name = name,
        age = age,
        email = email
    )

    override suspend fun deleteUser(id: String)
            : Result<Boolean> = userFakeDataSource.deleteUser(userId = id)
}