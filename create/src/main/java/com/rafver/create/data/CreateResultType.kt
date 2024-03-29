package com.rafver.create.data

sealed class CreateResultType {
    data object Ok: CreateResultType()
    sealed class Error: CreateResultType() {
        data object NameMandatory: Error()
        data object AgeMandatory: Error()
        data object InvalidAge: Error()
        data object EmailMandatory: Error()
    }
}