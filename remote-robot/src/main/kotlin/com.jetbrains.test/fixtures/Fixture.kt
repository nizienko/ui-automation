package com.jetbrains.test.fixtures

import com.jetbrains.test.data.RemoteComponent
import com.jetbrains.test.RemoteRobot
import org.fest.swing.core.Robot
import java.awt.Component

abstract class Fixture(
        val remoteRobot: RemoteRobot,
        val remoteComponent: RemoteComponent) {

    protected fun retrieveText(function: (Robot, Component) -> String): String {
        return remoteRobot.retrieveText(this, function)
    }

    protected fun execute(function: (Robot, Component) -> Unit) {
        remoteRobot.execute(this, function)
    }
}