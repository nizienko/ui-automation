package model.ideaModel.common

import com.intellij.ui.components.JBList
import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.RemoteComponent
import com.jetbrains.test.fixtures.ComponentFixture
import org.fest.swing.fixture.JListFixture as FestJListFixture

class JBListFixture(
        remoteRobot: RemoteRobot,
        remoteComponent: RemoteComponent) : ComponentFixture(remoteRobot, remoteComponent) {

    fun selectItem(item: String) = execute { robot, component ->
        FestJListFixture(robot, component as JBList<*>).clickItem(item)
    }
}