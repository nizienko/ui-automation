package com.jetbrains.test


import com.google.gson.Gson
import com.jetbrains.test.data.*
import com.jetbrains.test.fixtures.Fixture

import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType
import org.fest.swing.core.Robot
import java.awt.Component
import java.io.Serializable

class RemoteRobot(
        val url: String
) {
    val gson = Gson()

    inline fun <reified T : Fixture> find(noinline filter: (c: Component) -> Boolean): T {
        return find(null, filter)
    }

    inline fun <reified T : Fixture> findAll(noinline filter: (c: Component) -> Boolean): List<T> {
        return findAll(null, filter)
    }

    inline fun <reified T : Fixture> find(
            container: Fixture?,
            noinline filter: (c: Component) -> Boolean): T {

        val urlString = if (container != null) {
            "$url/${container.remoteComponent.id}/component"
        } else {
            "$url/component"
        }

        return Request.Post(urlString)
                .bodyString(gson.toJson(filter.pack()), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<FindComponentsResponse>().elementList
                .map {
                    T::class.java.getConstructor(
                            RemoteRobot::class.java, RemoteComponent::class.java
                    ).newInstance(this, it)
                }.first()
    }

    inline fun <reified T : Fixture> findAll(
            container: Fixture?,
            noinline filter: (c: Component) -> Boolean): List<T> {

        val urlString = if (container != null) {
            "$url/${container.remoteComponent.id}/components"
        } else {
            "$url/components"
        }
        return Request.Post(urlString)
                .bodyString(gson.toJson(filter.pack()), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<FindComponentsResponse>().elementList
                .map {
                    T::class.java.getConstructor(
                            RemoteRobot::class.java, RemoteComponent::class.java
                    ).newInstance(this, it)
                }
    }

    fun execute(action: (Robot) -> Unit) {
        Request.Post("$url/execute")
                .bodyString(gson.toJson(action.pack()), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<CommonResponse>()
    }

    fun execute(element: Fixture, action: (Robot, Component) -> Unit) {
        Request.Post("$url/${element.remoteComponent.id}/execute")
                .bodyString(gson.toJson(action.pack()), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<CommonResponse>()
    }

    fun retrieveText(element: Fixture, function: (Robot, Component) -> String): String {
        return Request.Post("$url/${element.remoteComponent.id}/retrieveText")
                .bodyString(gson.toJson(function.pack()), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<CommonResponse>().message?: throw AssertionError("Can't retrieve text(is null)")
    }

/*    fun retrieveBoolean(element: Fixture, function: (Robot, Component) -> Boolean): Boolean {
        return Request.Post("$url/${element.remoteComponent.id}/retrieveBoolean")
                .bodyString(gson.toJson(function.pack()), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<BooleanResponse>().value
    }*/

    inline fun <reified T : Serializable> retrieve(noinline function: (Robot) -> T): T {
        return Request.Post("$url/retrieveAny")
                .bodyString(gson.toJson(function.pack()), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<ByteResponse>().bytes.deserialize()
    }

    inline fun <reified T : Serializable> retrieve(element: Fixture, noinline function: (Robot, Component) -> T): T {
        return Request.Post("$url/${element.remoteComponent.id}/retrieveAny")
                .bodyString(gson.toJson(function.pack()), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<ByteResponse>().bytes.deserialize()
    }
}

