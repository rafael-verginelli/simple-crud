package com.rafver.create.domain.repositories

import com.rafver.core_data.dtos.UserDTO
import com.rafver.create.data.CreateResultType

interface UserRepository {
    fun createUser(userDTO: UserDTO): Result<CreateResultType>
}