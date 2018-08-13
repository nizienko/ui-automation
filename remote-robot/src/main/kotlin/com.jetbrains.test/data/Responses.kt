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
        override val message: String? = null) : Response

data class FindComponentsResponse(
        override val status: ResponseStatus = SUCCESS,
        override val message: String? = null,
        val elementList: List<ComponentDescription>) : Response

data class ListResponse(
        override val status: ResponseStatus = SUCCESS,
        override val message: String? = null,
        val list: List<Any?>
): Response

inline fun <reified R : Response> Content.asResponse(): R {
    val responseString = this.asString()
    val response = gson.fromJson(responseString, R::class.java)
    if (response.status != SUCCESS) {
        throw IllegalStateException(response.message ?: "Unknown error")
    }
    return response
}

enum class ResponseStatus { SUCCESS, ERROR }