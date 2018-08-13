package com.jetbrains.test.services

import com.jetbrains.test.data.ComponentDescription
import com.jetbrains.test.data.ObjectContainer
import com.jetbrains.test.utils.LimitedMap
import org.fest.swing.core.BasicRobot
import org.fest.swing.core.Robot
import java.awt.Component
import java.awt.Container
import java.util.*


private val componentStorage by lazy { LimitedMap<String, Component>() }
private val lambdaLoader by lazy { LambdaLoader() }
val robot by lazy { BasicRobot.robotWithCurrentAwtHierarchy()!! }

fun findComponent(containerId: String? = null, lambdaContainer: ObjectContainer): ComponentDescription {
    val lambda = lambdaLoader.getFunction<(c: Component) -> Boolean>(lambdaContainer)
    return if (containerId == null) {
        val c = robot.finder().find { lambda(it) }
        val uid = UUID.randomUUID().toString()
        componentStorage[uid] = c
        c.getDescription(uid)
    } else {
        val component = componentStorage[containerId]
                ?: throw IllegalStateException("Unknown component id $containerId")
        if (component is Container) {
            val c = robot.finder().find { lambda(it) }
            val uid = UUID.randomUUID().toString()
            componentStorage[uid] = c
            return c.getDescription(uid)
        } else throw IllegalStateException("Component is not a container")
    }
}

fun findComponents(containerId: String? = null, lambdaContainer: ObjectContainer): List<ComponentDescription> {
    val lambda = lambdaLoader.getFunction<(c: Component) -> Boolean>(lambdaContainer)
    if (containerId == null) {
        return robot.finder()
                .findAll { lambda(it) }
                .map {
                    val uid = UUID.randomUUID().toString()
                    componentStorage[uid] = it
                    return@map it.getDescription(uid)
                }
    } else {
        val component = componentStorage[containerId]
                ?: throw IllegalStateException("Unknown component id $containerId")
        if (component is Container) {
            return robot.finder()
                    .findAll(component) { lambda(it) }
                    .map {
                        val uid = UUID.randomUUID().toString()
                        componentStorage[uid] = it
                        return@map it.getDescription(uid)
                    }
        } else throw IllegalStateException("Component is not a container")
    }
}

fun hierarchy(): List<Any> {
    val list = mutableListOf<Any>()
    robot.hierarchy().roots().forEach {
        list.add(it.toDescribed())
    }
    return list
}

fun doAction(componentId: String, actionContainer: ObjectContainer) {
    val component = componentStorage[componentId] ?: throw IllegalStateException("Unknown component id $componentId")
    lambdaLoader.getFunction<(Robot, Component) -> Unit>(actionContainer).invoke(robot, component)
}

fun retrieveText(componentId: String, actionContainer: ObjectContainer): String {
    val component = componentStorage[componentId] ?: throw IllegalStateException("Unknown component id $componentId")
    return lambdaLoader.getFunction<(Robot, Component) -> String>(actionContainer).invoke(robot, component)
}


data class DescribedComponent(
        val thisElement: ComponentDescription,
        val children: List<DescribedComponent>
)

private fun Component.toDescribed(): DescribedComponent {
    val children = mutableListOf<DescribedComponent>()
    if (this is Container) {
        this.components.forEach {
            children.add(it.toDescribed())
        }
    }
    return DescribedComponent(this.getDescription(""), children)
}

private fun Component.getDescription(id: String): ComponentDescription {
    // todo: do we really need this fieldsMap?
    val fieldsMap = mutableMapOf<String, Any?>()
    this::class.java.declaredFields.forEach {
        val name = it.name
        it.isAccessible = true
        val value = it.get(this)
        fieldsMap[name] = value?.toString()
    }
    return ComponentDescription(
            id,
            this::class.java.canonicalName,
            this.name,
            this.x,
            this.y,
            this.width,
            this.height,
            this.isVisible,
            this.isEnabled,
            this.isValid,
            fieldsMap
    )
}


