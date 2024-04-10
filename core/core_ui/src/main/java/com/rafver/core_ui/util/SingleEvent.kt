package com.rafver.core_ui.util

/**
 * Wrapper for an event that should be consumed only once
 */
class SingleEvent<out T>(private val content: T) {

    var hasBeenHandled: Boolean = false
        private set

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