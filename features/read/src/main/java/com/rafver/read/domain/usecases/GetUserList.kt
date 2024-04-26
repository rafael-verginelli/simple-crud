package com.rafver.read.domain.usecases

import com.rafver.core_domain.models.UserModel
import com.rafver.core_data.repositories.UserRepository
import com.rafver.core_domain.models.toDomainModel
import javax.inject.Inject

class GetUserList @Inject constructor(private val userRepository: UserRepository){
    suspend operator fun invoke(): Result<List<UserModel>> {
        val result = userRepository.getUserList()
        if(result.isSuccess) {
            return Result.success(result.getOrNull().orEmpty().toDomainModel())
        }
        // ToDo implement proper error handling
        return Result.failure(Exception("GetUserList operation failed"))
    }
}