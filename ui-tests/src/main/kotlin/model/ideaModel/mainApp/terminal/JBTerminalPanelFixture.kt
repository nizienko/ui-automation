package model.ideaModel.mainApp.terminal

import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.RemoteComponent
import model.ideaModel.common.IdeContainerFixture
import utils.pressingKey
import java.awt.event.KeyEvent

class JBTerminalPanelFixture(
        remoteRobot: RemoteRobot,
        remoteComponent: RemoteComponent) : IdeContainerFixture(remoteRobot, remoteComponent) {

    fun enterCommand(cmd: String) = remoteRobot.execute {
        it.enterText(cmd)
        it.pressAndReleaseKey(KeyEvent.VK_ENTER)
    }

    fun sendCancel() = remoteRobot.execute {
        it.pressingKey(KeyEvent.VK_CONTROL) { it.pressAndReleaseKey(KeyEvent.VK_C) }
    }
}