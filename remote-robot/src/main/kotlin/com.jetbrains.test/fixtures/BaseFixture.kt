package com.jetbrains.test.fixtures

import com.jetbrains.test.data.ComponentDescription
import com.jetbrains.test.RemoteRobot
import org.fest.swing.core.Robot
import java.awt.Component

open class BaseFixture(val remoteRobot: RemoteRobot, val description: ComponentDescription) {
    fun click() = remoteRobot.execute(this) { r, c -> r.click(c) }

    inline fun <reified T : BaseFixture> findComponent(noinline filter: (Component) -> Boolean): T {
        return remoteRobot.findComponent(this, filter)
    }

    inline fun <reified T : BaseFixture> findComponents(noinline filter: (Component) -> Boolean): List<T> {
        return remoteRobot.findComponents(this, filter)
    }

    fun retrieveText(function: (Robot, Component) -> String): String {
        return remoteRobot.retrieveText(this, function)
    }

    fun execute(function: (Robot, Component) -> Unit) {
        remoteRobot.execute(this, function)
    }
}