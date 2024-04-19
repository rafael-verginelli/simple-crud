package com.rafver.core_domain.models

import com.rafver.core_data.dtos.UserDTO

data class UserModel(
    val id: String,
    val name: String,
    val age: Int,
    val email: String
)

fun UserModel.toDTO(): UserDTO = UserDTO(id = id, name = name, age = age, email = email)

fun List<UserModel>.toDTO(): List<UserDTO> = map { it.toDTO() }


fun UserDTO.toDomainModel(): UserModel = UserModel(id = id, name = name, age = age, email = email)

fun List<UserDTO>.toDomainModel(): List<UserModel> = map { it.toDomainModel() }