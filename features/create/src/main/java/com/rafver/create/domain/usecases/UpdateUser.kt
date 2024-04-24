package com.rafver.create.domain.usecases

import com.rafver.core_data.repositories.UserRepository
import javax.inject.Inject

class UpdateUser @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(id: String, name: String, age: String, email: String): Result<Boolean> {
        val ageInt = age.toIntOrNull() ?: throw IllegalStateException("User data validation failed")
        return userRepository.updateUser(id, name, ageInt, email)
    }
}