package com.jetbrains.test

import com.google.gson.Gson
import com.intellij.openapi.actionSystem.impl.ActionMenu
import com.jetbrains.test.data.*
import com.jetbrains.test.services.*
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.text.DateFormat

class RobotServerImpl : RobotServer {

    private fun startServer() {
        embeddedServer(Netty, port = 8080) {
            install(DefaultHeaders)
            install(Compression)
            install(ContentNegotiation) {
                gson {
                    setDateFormat(DateFormat.LONG)
                    setPrettyPrinting()
                }
            }
            routing {
                get("/hello") {
                    call.respond("Hello from idea")
                }
                post("/component") {
                    call.dataRequest {
                        FindComponentsResponse(
                                elementList = listOf(find(lambdaContainer = call.receiveJson())))
                    }
                }
                post("/{id}/component") {
                    call.dataRequest {
                        val id = call.parameters["id"] ?: throw IllegalArgumentException("empty id")
                        FindComponentsResponse(
                                elementList = listOf(
                                        find(
                                                containerId = id,
                                                lambdaContainer = call.receiveJson())
                                ))
                    }
                }
                post("/components") {
                    call.dataRequest {
                        FindComponentsResponse(
                                elementList = findAll(lambdaContainer = call.receiveJson()))
                    }
                }
                post("/{id}/components") {
                    call.dataRequest {
                        val id = call.parameters["id"] ?: throw IllegalArgumentException("empty id")
                        FindComponentsResponse(
                                elementList = findAll(
                                        containerId = id,
                                        lambdaContainer = call.receiveJson())
                        )
                    }
                }
                get("/hierarchy") {
                    call.dataRequest {
                        ListResponse(list = hierarchy())
                    }
                    val y: ActionMenu
                }
                post("/execute") {
                    call.commonRequest {
                        doAction(call.receiveJson())
                    }
                }
                post("/{id}/execute") {
                    call.commonRequest {
                        val id = call.parameters["id"] ?: throw IllegalArgumentException("empty id")
                        doAction(id, call.receiveJson())
                    }
                }
                post("/{id}/retrieveText") {
                    call.dataRequest {
                        val id = call.parameters["id"] ?: throw IllegalArgumentException("empty id")
                        CommonResponse(message = retrieveText(id, call.receiveJson()))
                    }
                }
/*                post("/{id}/retrieveBoolean") {
                    call.dataRequest {
                        val id = call.parameters["id"] ?: throw IllegalArgumentException("empty id")
                        BooleanResponse(value = retrieveBoolean(id, call.receiveJson()))
                    }
                }*/
                post("/retrieveAny") {
                    call.dataRequest {
                        ByteResponse(className = "", bytes = retrieveAny(call.receiveJson()).serializeToBytes())
                    }
                }
                post("/{id}/retrieveAny") {
                    call.dataRequest {
                        val id = call.parameters["id"] ?: throw IllegalArgumentException("empty id")
                        ByteResponse(className = "", bytes = retrieveAny(id, call.receiveJson()).serializeToBytes())
                    }
                }
            }
        }.start(wait = false)
    }

    override fun initComponent() {
        startServer()
    }

    override fun getComponentName(): String {
        return "RobotServerImpl.Main"
    }
}

suspend inline fun ApplicationCall.commonRequest(code: () -> Unit) {
    val response = try {
        code()
        CommonResponse()
    } catch (e: Throwable) {
        e.printStackTrace()
        CommonResponse(ResponseStatus.ERROR, e.message)
    }
    this.respond(response)
}

suspend inline fun ApplicationCall.dataRequest(code: () -> Response) {
    val response = try {
        code()
    } catch (e: Throwable) {
        e.printStackTrace()
        CommonResponse(ResponseStatus.ERROR, e.message)
    }
    this.respond(response)
}

val gson = Gson()

suspend inline fun <reified T> ApplicationCall.receiveJson(): T {
    return gson.fromJson(receiveText(), T::class.java)
}