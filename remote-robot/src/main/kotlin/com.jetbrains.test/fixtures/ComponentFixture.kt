package com.jetbrains.test.fixtures

import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.RemoteComponent

open class ComponentFixture(
        remoteRobot: RemoteRobot,
        remoteComponent: RemoteComponent) : Fixture(remoteRobot, remoteComponent) {

    fun click() = remoteRobot.execute(this) { r, c -> r.click(c) }
}