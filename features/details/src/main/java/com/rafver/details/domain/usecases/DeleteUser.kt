package com.rafver.details.domain.usecases

import com.rafver.core_data.repositories.UserRepository
import javax.inject.Inject

class DeleteUser @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(id: String): Result<Boolean> {
        return userRepository.deleteUser(id)
    }
}