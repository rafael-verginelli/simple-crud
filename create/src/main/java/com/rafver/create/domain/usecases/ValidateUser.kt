package com.rafver.create.domain.usecases

import com.rafver.create.data.CreateResultType
import javax.inject.Inject

class ValidateUser @Inject constructor() {
    operator fun invoke(name: String, age: String, email: String): CreateResultType {
        if(name.isEmpty()) {
            return CreateResultType.Error.NameMandatory
        }
        if(age.isEmpty()) {
            return CreateResultType.Error.AgeMandatory
        }
        age.toIntOrNull() ?: return CreateResultType.Error.InvalidAge
        if(email.isEmpty()) {
            return CreateResultType.Error.EmailMandatory
        }
        return CreateResultType.Ok
    }
}