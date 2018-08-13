package model.ideaModel.mainApp

import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.ComponentDescription
import com.jetbrains.test.fixtures.BaseFixture
import model.ideaModel.common.ContainerFixture
import utils.pressingKey
import java.awt.event.KeyEvent

class JBTerminalPanelFixture(remoteRobot: RemoteRobot, description: ComponentDescription) : ContainerFixture(remoteRobot, description) {
    fun enterCommand(cmd: String) = execute { r, _ ->
        r.enterText(cmd)
        r.pressAndReleaseKey(KeyEvent.VK_ENTER)
    }

    fun sendCancel() = execute { r, _ ->
        r.pressingKey(KeyEvent.VK_CONTROL) { r.pressAndReleaseKey(KeyEvent.VK_C) }
    }
}