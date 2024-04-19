package com.rafver.details.domain.usecases

import com.rafver.core_data.repositories.UserRepository
import com.rafver.core_domain.models.UserModel
import com.rafver.core_domain.models.toDomainModel
import javax.inject.Inject

class GetUser @Inject constructor(private val userRepository: UserRepository){
    operator fun invoke(userId: String): Result<UserModel> {
        val result = userRepository.getUser(userId)
        val user = result.getOrNull()
        if (user != null) {
            return Result.success(user.toDomainModel())
        }

        return Result.failure(result.exceptionOrNull()!!)
    }
}