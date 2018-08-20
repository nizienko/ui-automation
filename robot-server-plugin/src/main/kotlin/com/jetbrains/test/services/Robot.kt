package com.jetbrains.test.services

import com.jetbrains.test.data.ObjectContainer
import com.jetbrains.test.data.RemoteComponent
import com.jetbrains.test.utils.LimitedMap
import org.fest.swing.core.BasicRobot
import org.fest.swing.core.Robot
import org.fest.swing.timing.Wait
import java.awt.Component
import java.awt.Container
import java.io.Serializable
import java.util.*

private val componentStorage by lazy { LimitedMap<String, Component>() }
private val lambdaLoader by lazy { LambdaLoader() }
private const val FIND_SECONDS_TO_WAIT = 1L
val robot by lazy { BasicRobot.robotWithCurrentAwtHierarchy()!! }


fun find(lambdaContainer: ObjectContainer): RemoteComponent {
    val lambda = lambdaLoader.getFunction<(c: Component) -> Boolean>(lambdaContainer)

    Wait.seconds(FIND_SECONDS_TO_WAIT).expecting("matching " + lambda.toString()).until {
        robot.finder().findAll { lambda(it) }.isNotEmpty()
    }
    val c = robot.finder().find { lambda(it) }
    val uid = UUID.randomUUID().toString()
    componentStorage[uid] = c
    return c.toRemoteComponent(uid)
}

fun find(containerId: String, lambdaContainer: ObjectContainer): RemoteComponent {
    val lambda = lambdaLoader.getFunction<(c: Component) -> Boolean>(lambdaContainer)

    val component = componentStorage[containerId]
            ?: throw IllegalStateException("Unknown component id $containerId")
    if (component is Container) {
        Wait.seconds(FIND_SECONDS_TO_WAIT).expecting("matching " + lambda.toString()).until {
            robot.finder().findAll(component) { lambda(it) }.isNotEmpty()
        }
        val c = robot.finder().find(component) { lambda(it) }
        val uid = UUID.randomUUID().toString()
        componentStorage[uid] = c
        return c.toRemoteComponent(uid)
    } else throw IllegalStateException("Component is not a container")
}

fun findAll(lambdaContainer: ObjectContainer): List<RemoteComponent> {
    val lambda = lambdaLoader.getFunction<(c: Component) -> Boolean>(lambdaContainer)

    Wait.seconds(FIND_SECONDS_TO_WAIT).expecting("matching " + lambda.toString()).until {
        robot.finder().findAll { lambda(it) }.isNotEmpty()
    }
    return robot.finder()
            .findAll { lambda(it) }
            .map {
                val uid = UUID.randomUUID().toString()
                componentStorage[uid] = it
                return@map it.toRemoteComponent(uid)
            }

}

fun findAll(containerId: String, lambdaContainer: ObjectContainer): List<RemoteComponent> {
    val lambda = lambdaLoader.getFunction<(c: Component) -> Boolean>(lambdaContainer)
    val component = componentStorage[containerId]
            ?: throw IllegalStateException("Unknown component id $containerId")
    if (component is Container) {
        Wait.seconds(FIND_SECONDS_TO_WAIT).expecting("matching " + lambda.toString()).until {
            robot.finder().findAll { lambda(it) }.isNotEmpty()
        }
        return robot.finder()
                .findAll(component) { lambda(it) }
                .map {
                    val uid = UUID.randomUUID().toString()
                    componentStorage[uid] = it
                    return@map it.toRemoteComponent(uid)
                }
    } else throw IllegalStateException("Component is not a container")

}

fun hierarchy(): List<Any> {
    val list = mutableListOf<Any>()
    robot.hierarchy().roots().forEach {
        list.add(it.toDescribed())
    }
    return list
}

fun doAction(actionContainer: ObjectContainer) {
    lambdaLoader.getFunction<(Robot) -> Unit>(actionContainer).invoke(robot)
}

fun doAction(componentId: String, actionContainer: ObjectContainer) {
    val component = componentStorage[componentId] ?: throw IllegalStateException("Unknown component id $componentId")
    lambdaLoader.getFunction<(Robot, Component) -> Unit>(actionContainer).invoke(robot, component)
}

fun retrieveText(componentId: String, actionContainer: ObjectContainer): String {
    val component = componentStorage[componentId] ?: throw IllegalStateException("Unknown component id $componentId")
    return lambdaLoader.getFunction<(Robot, Component) -> String>(actionContainer).invoke(robot, component)
}

/*fun retrieveBoolean(componentId: String, actionContainer: ObjectContainer): Boolean {
    val component = componentStorage[componentId] ?: throw IllegalStateException("Unknown component id $componentId")
    return lambdaLoader.getFunction<(Robot, Component) -> Boolean>(actionContainer).invoke(robot, component)
}*/

fun retrieveAny(actionContainer: ObjectContainer): Serializable {
    return lambdaLoader.getFunction<(Robot) -> Serializable>(actionContainer).invoke(robot)
}

fun retrieveAny(componentId: String, actionContainer: ObjectContainer): Serializable {
    val component = componentStorage[componentId] ?: throw IllegalStateException("Unknown component id $componentId")
    return lambdaLoader.getFunction<(Robot, Component) -> Serializable>(actionContainer).invoke(robot, component)
}

data class DescribedComponent(
        val thisElement: RemoteComponent,
        val children: List<DescribedComponent>
)

private fun Component.toDescribed(): DescribedComponent {
    val children = mutableListOf<DescribedComponent>()
    if (this is Container) {
        this.components.forEach {
            children.add(it.toDescribed())
        }
    }
    return DescribedComponent(this.toRemoteComponent(""), children)
}

private fun Component.toRemoteComponent(id: String): RemoteComponent {
    return RemoteComponent(
            id,
            this::class.java.canonicalName,
            this.name,
            this.x,
            this.y,
            this.width,
            this.height
    )
}


