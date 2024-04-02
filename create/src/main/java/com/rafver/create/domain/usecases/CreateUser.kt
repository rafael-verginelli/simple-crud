package com.rafver.create.domain.usecases

import com.rafver.core_data.dtos.UserDTO
import com.rafver.create.data.CreateResultType
import com.rafver.create.domain.repositories.UserRepository
import javax.inject.Inject

class CreateUser @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(name: String, age: String, email: String): Result<CreateResultType> {
        val userDTO = UserDTO(
            name = name,
            age = age.toIntOrNull() ?: throw Exception("User data validation failed"),
            email = email
        )
        return userRepository.createUser(userDTO)
    }
}