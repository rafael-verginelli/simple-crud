package com.rafver.create.domain.usecases

import com.rafver.core_data.repositories.UserRepository
import javax.inject.Inject

class CreateUser @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(name: String, age: String, email: String): Result<Boolean> {
        val ageInt = age.toIntOrNull() ?: throw IllegalStateException("User data validation failed")
        return userRepository.createUser(name, ageInt, email)
    }
}