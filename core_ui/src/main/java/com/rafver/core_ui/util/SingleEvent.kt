package com.rafver.core_ui.util

class SingleEvent<out T>(private val content: T) {

    private var hasBeenHandled: Boolean = false

    fun handleSingleEvent(): T? {
        return if(!hasBeenHandled) {
            hasBeenHandled = true
            content
        } else {
            null
        }
    }

    fun peekContent(): T = content
}