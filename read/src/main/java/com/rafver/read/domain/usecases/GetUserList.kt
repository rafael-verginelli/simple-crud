package com.rafver.read.domain.usecases

import com.rafver.core_domain.models.UserModel
import com.rafver.core_data.repositories.UserRepository
import javax.inject.Inject

class GetUserList @Inject constructor(private val userRepository: UserRepository){
    operator fun invoke(): Result<List<UserModel>> = userRepository.getUserList()
}