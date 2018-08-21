package com.jetbrains.test.data

import com.google.gson.Gson
import com.jetbrains.test.data.ResponseStatus.SUCCESS
import org.apache.http.client.fluent.Content

val gson = Gson()

interface Response {
    val status: ResponseStatus
    val message: String?
}

data class CommonResponse(
        override val status: ResponseStatus = SUCCESS,
        override val message: String? = null,
        val exceptionClassName: String? = null) : Response

data class FindComponentsResponse(
        override val status: ResponseStatus = SUCCESS,
        override val message: String? = null,
        val elementList: List<RemoteComponent>) : Response

data class ListResponse(
        override val status: ResponseStatus = SUCCESS,
        override val message: String? = null,
        val list: List<Any?>
) : Response

data class BooleanResponse(
        override val status: ResponseStatus = SUCCESS,
        override val message: String? = null,
        val value: Boolean
) : Response

data class ByteResponse(
        override val status: ResponseStatus = SUCCESS,
        override val message: String? = null,
        val className: String,
        val bytes: ByteArray
) : Response

inline fun <reified R : Response> Content.asResponse(): R {
    val responseString = this.asString()
    val response = try {
        gson.fromJson(responseString, R::class.java)
    } catch (e: Throwable) {
        gson.fromJson(responseString, CommonResponse::class.java)
    }
    if (response.status != SUCCESS) {
        if (response is CommonResponse) {
            throw IdeaSideError(response.exceptionClassName ?: "Unknown error", response.message
                    ?: "Unknown message")
        } else {
            throw IllegalStateException(response.message ?: "Unknown error")
        }
    }
    return response as R
}

class IdeaSideError(exceptionClassName: String, message: String) : IllegalStateException("$exceptionClassName: $message")

enum class ResponseStatus { SUCCESS, ERROR }