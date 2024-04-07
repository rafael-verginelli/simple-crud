package com.rafver.create.data

import androidx.annotation.StringRes

sealed class CreateResultType {
    sealed class Error: CreateResultType() {
        data class NameMandatory(@StringRes val resId: Int): Error()
        data class AgeMandatory(@StringRes val resId: Int): Error()
        data class InvalidAge(@StringRes val resId: Int): Error()
        data class EmailMandatory(@StringRes val resId: Int): Error()
    }
}