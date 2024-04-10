package com.rafver.create.domain.usecases

import com.rafver.create.R
import com.rafver.create.data.CreateResultType
import javax.inject.Inject

class ValidateUser @Inject constructor() {
    operator fun invoke(name: String, age: String, email: String): List<CreateResultType.Error> {
        val errors = mutableListOf<CreateResultType.Error>()
        if(name.isEmpty()) {
            errors.add(CreateResultType.Error.NameMandatory(R.string.error_create_mandatory_field))
        }
        if(age.isEmpty()) {
            errors.add(CreateResultType.Error.AgeMandatory(R.string.error_create_mandatory_field))
        } else {
            age.toIntOrNull()
                ?: errors.add(CreateResultType.Error.InvalidAge(R.string.error_create_invalid_age))
        }
        if(email.isEmpty()) {
            errors.add(CreateResultType.Error.EmailMandatory(R.string.error_create_mandatory_field))
        }
        return errors
    }
}