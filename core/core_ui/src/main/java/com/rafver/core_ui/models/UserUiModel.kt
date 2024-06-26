package com.rafver.core_ui.models

import com.rafver.core_domain.models.UserModel

data class UserUiModel(val id: String, val name: String, val age: Int, val email: String)

fun UserModel.toUiModel(): UserUiModel = UserUiModel(id = id, name = name, age = age, email = email)

fun List<UserModel>.toUiModel(): List<UserUiModel> = map { it.toUiModel() }


fun UserUiModel.toDomainModel(): UserModel = UserModel(id = id, name = name, age = age, email = email)

fun List<UserUiModel>.toDomainModel(): List<UserModel> = map { it.toDomainModel() }