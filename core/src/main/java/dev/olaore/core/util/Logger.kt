package dev.olaore.core.util

class Logger constructor (
    private val tag: String,
    private var isDebug: Boolean = true
) {

    fun log(msg: String) {
        if (isDebug) {
            printLogD(msg)
        } else {
            // log to production.
        }
    }

    private fun printLogD(msg: String) {
        println("$tag: $msg")
    }

    companion object {
        class Builder {
            lateinit var logger: Logger

            fun createLogger(tag: String): Builder {
                logger = Logger(tag, true)
                return this
            }

            fun setDebugMode(mode: Boolean): Builder {
                logger.isDebug = mode
                return this
            }

            fun build(): Logger {
                return logger
            }
        }
    }
}
