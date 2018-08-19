package model.ideaModel.mainApp

import com.intellij.openapi.wm.impl.IdeMenuBar
import com.intellij.openapi.wm.impl.StripeButton
import com.intellij.terminal.JBTerminalPanel
import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.RemoteComponent
import com.jetbrains.test.fixtures.ComponentFixture
import model.ideaModel.common.IdeContainerFixture
import model.ideaModel.common.IndexingSensitive
import javax.swing.JFrame

class IdeFrameFixture(
        remoteRobot: RemoteRobot,
        remoteComponent: RemoteComponent) : IdeContainerFixture(remoteRobot, remoteComponent),
        IndexingSensitive {

    val title
        get() = this.retrieveText { _, c -> (c as JFrame).title }

    val mainMenu
        get() = find<IdeMenuBarFixture> { it is IdeMenuBar }

    val terminalPanel
        get() = find<JBTerminalPanelFixture> { it is JBTerminalPanel }

    val terminalButton
        get() = find<ComponentFixture> { it is StripeButton && it.text == "Terminal" }

    fun openSettings() = mainMenu.openMenu("File").select("Settings...")

}






























































