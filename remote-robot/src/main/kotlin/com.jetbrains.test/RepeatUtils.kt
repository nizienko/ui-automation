package com.jetbrains.test


fun wait(s: Int, waitFor: () -> Boolean): Boolean {
    val step = 100L
    val c = s * 10
    for (i in 1..c) {
        try {
            if (waitFor()) {
                break
            } else {
                Thread.sleep(step)
            }
        } catch (e: Exception) {
            Thread.sleep(step)
        }
    }
    return waitFor()
}

inline fun <O> tryTimes(n: Int, onError: () -> Unit = {},
                        finalException: (Throwable) -> Exception,
                        block: () -> O): O {
    var finalError: Throwable? = null
    for (i in 1..n) {
        try {
            return block()
        } catch (e: Throwable) {
            when (e) {
                is OutOfAttemptsException -> throw e
                else -> {
                    onError()
                    println(bar(i))
                    finalError = e
                    Thread.sleep(500)
                }
            }
        }
    }
    val reason = finalError?.let { finalError } ?: IllegalStateException("Íå óäàëîñü âûïîëíèòü äåéñòâèå")

    throw finalException(reason)
}

fun bar(n: Int): String {
    val bar = StringBuilder()
    for (i in 1..n) {
        bar.append(".")
    }
    return bar.toString()
}

inline fun <O> attempt(n: Int = 5, onError: () -> Unit = {}, block: () -> O): O =
        tryTimes(n, onError, finalException = { OutOfAttemptsException(it) }, block = block)


class OutOfAttemptsException(e: Throwable) : Exception(e)

