package model.ideaModel.mainApp

import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.RemoteComponent
import model.ideaModel.common.IdeContainerFixture
import utils.pressingKey
import java.awt.event.KeyEvent

class JBTerminalPanelFixture(
        remoteRobot: RemoteRobot,
        remoteComponent: RemoteComponent) : IdeContainerFixture(remoteRobot, remoteComponent) {

    fun enterCommand(cmd: String) = execute { r, _ ->
        r.enterText(cmd)
        r.pressAndReleaseKey(KeyEvent.VK_ENTER)
    }

    fun sendCancel() = execute { r, _ ->
        r.pressingKey(KeyEvent.VK_CONTROL) { r.pressAndReleaseKey(KeyEvent.VK_C) }
    }
}