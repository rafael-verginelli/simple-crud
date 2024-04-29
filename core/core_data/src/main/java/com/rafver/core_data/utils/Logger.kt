package com.rafver.core_data.utils

import timber.log.Timber

fun Any.createLogger(): Logger {
    return LoggerImpl()
}

/**
 * This logger interface exposes methods to log things as Errors, Warnings or simple Debug.
 * It is meant to be implemented in a way that any logger lib can be used for debugging.
 * For example, the same logger can use Timber to print debug messages, but use Timber and Firebase
 * to log errors.
 */
interface Logger {
    fun e(message: String)
    fun e(throwable: Throwable?)
    fun e(message: String, throwable: Throwable?)
    fun d(message: String)
    fun d(throwable: Throwable?)
    fun d(message: String, throwable: Throwable?)
    fun w(message: String)
    fun w(throwable: Throwable?)
    fun w(message: String, throwable: Throwable?)
}
class LoggerImpl: Logger {
    /**
     * Logs errors with message
     */
    override fun e(message: String) {
        Timber.e(message)
    }

    /**
     * Logs errors with throwables
     */
    override fun e(throwable: Throwable?) {
        Timber.e(throwable)
    }

    /**
     * Logs errors with message and throwable
     */
    override fun e(message: String, throwable: Throwable?) {
        Timber.e(t = throwable, message = message)
    }

    /**
     * Logs general debug things with message
     */
    override fun d(message: String) {
        Timber.d(message)
    }

    /**
     * Logs general debug things with throwable
     */
    override fun d(throwable: Throwable?) {
        Timber.d(throwable)
    }

    /**
     * Logs general debug things with message and throwable
     */
    override fun d(message: String, throwable: Throwable?) {
        Timber.d(t = throwable, message = message)
    }

    /**
     * Logs warnings with message
     */
    override fun w(message: String) {
        Timber.w(message)
    }

    /**
     * Logs warnings with throwable
     */
    override fun w(throwable: Throwable?) {
        Timber.w(throwable)
    }

    /**
     * Logs warnings with message and throwable
     */
    override fun w(message: String, throwable: Throwable?) {
        Timber.w(t = throwable, message = message)
    }
}