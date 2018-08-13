package model.ideaModel.mainApp

import com.intellij.openapi.wm.impl.IdeMenuBar
import com.intellij.openapi.wm.impl.StripeButton
import com.intellij.terminal.JBTerminalPanel
import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.ComponentDescription
import com.jetbrains.test.fixtures.BaseFixture
import model.ideaModel.common.ContainerFixture
import javax.swing.JFrame

class IdeaFrameFixture(remoteRobot: RemoteRobot, description: ComponentDescription) : ContainerFixture(remoteRobot, description) {

    val title
        get() = retrieveText { _, c -> (c as JFrame).title }

    val mainMenu
        get() = findComponent<IdeMenuBarFixture> { it is IdeMenuBar }

    val terminalPanel
        get() = findComponent<JBTerminalPanelFixture> { it is JBTerminalPanel }

    val terminalButton
        get() = findComponent<BaseFixture> { it is StripeButton && it.text == "Terminal" }

}






























































