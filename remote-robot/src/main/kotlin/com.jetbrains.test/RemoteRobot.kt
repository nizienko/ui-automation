package com.jetbrains.test


import com.google.gson.Gson
import com.jetbrains.test.data.*
import com.jetbrains.test.fixtures.BaseFixture

import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType
import org.fest.swing.core.Robot
import java.awt.Component

class RemoteRobot(
        val url: String
) {
    val gson = Gson()

    inline fun <reified T : BaseFixture> findComponent(noinline filter: (c: Component) -> Boolean): T {
        return findComponent(null, filter)
    }

    inline fun <reified T : BaseFixture> findComponents(noinline filter: (c: Component) -> Boolean): List<T> {
        return findComponents(null, filter)
    }

    inline fun <reified T : BaseFixture> findComponent(
            container: BaseFixture?,
            noinline filter: (c: Component) -> Boolean): T {

        val urlString = if (container != null) {
            "$url/${container.description.id}/component"
        } else {
            "$url/component"
        }

        return Request.Post(urlString)
                .bodyString(gson.toJson(filter.pack()), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<FindComponentsResponse>().elementList
                .map {
                    T::class.java.getConstructor(
                            RemoteRobot::class.java, ComponentDescription::class.java
                    ).newInstance(this, it)
                }.first()
    }

    inline fun <reified T : BaseFixture> findComponents(
            container: BaseFixture?,
            noinline filter: (c: Component) -> Boolean): List<T> {

        val urlString = if (container != null) {
            "$url/${container.description.id}/components"
        } else {
            "$url/components"
        }
        return Request.Post(urlString)
                .bodyString(gson.toJson(filter.pack()), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<FindComponentsResponse>().elementList
                .map {
                    T::class.java.getConstructor(
                            RemoteRobot::class.java, ComponentDescription::class.java
                    ).newInstance(this, it)
                }
    }

    fun execute(element: BaseFixture, action: (Robot, Component) -> Unit) {
        Request.Post("$url/${element.description.id}/execute")
                .bodyString(gson.toJson(action.pack()), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<CommonResponse>()
    }

    fun retrieveText(element: BaseFixture, fucnction: (Robot, Component) -> String): String {
        return Request.Post("$url/${element.description.id}/retrieveText")
                .bodyString(gson.toJson(fucnction.pack()), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<CommonResponse>().message?: throw AssertionError("Can't retrieve text(is null)")
    }
}

