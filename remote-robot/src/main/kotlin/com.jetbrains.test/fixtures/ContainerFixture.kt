package com.jetbrains.test.fixtures

import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.RemoteComponent
import java.awt.Component

open class ContainerFixture(
        remoteRobot: RemoteRobot,
        remoteComponent: RemoteComponent) : Fixture(remoteRobot, remoteComponent) {

    inline fun <reified T : Fixture> find(noinline filter: (Component) -> Boolean): T {
        return remoteRobot.find(this, filter)
    }

    inline fun <reified T : Fixture> findAll(noinline filter: (Component) -> Boolean): List<T> {
        return remoteRobot.findAll(this, filter)
    }
}