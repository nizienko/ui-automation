package model.ideaModel.common

import com.intellij.ui.components.JBList
import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.ComponentDescription
import com.jetbrains.test.fixtures.BaseFixture
import org.fest.swing.fixture.JListFixture as FestJListFixture

class JBListFixture(remoteRobot: RemoteRobot, description: ComponentDescription) : BaseFixture(remoteRobot, description) {
    fun selectItem(item: String) = execute { robot, component ->
        FestJListFixture(robot, component as JBList<*>).clickItem(item)
    }
}