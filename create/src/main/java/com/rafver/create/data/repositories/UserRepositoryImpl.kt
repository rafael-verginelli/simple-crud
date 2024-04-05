package com.rafver.create.data.repositories

import com.rafver.core_data.dtos.UserDTO
import com.rafver.create.data.CreateResultType
import com.rafver.create.domain.repositories.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(): UserRepository {

    override fun createUser(userDTO: UserDTO): Result<CreateResultType> {
        // ToDo: To Be Implemented
        return Result.success(CreateResultType.Ok)
    }

}