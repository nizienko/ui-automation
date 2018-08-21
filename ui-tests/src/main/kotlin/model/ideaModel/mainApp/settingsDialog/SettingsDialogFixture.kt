package model.ideaModel.mainApp.settingsDialog

import com.intellij.openapi.options.newEditor.SettingsTreeView
import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.RemoteComponent
import com.jetbrains.test.fixtures.ComponentFixture
import model.ideaModel.common.IdeContainerFixture

class SettingsDialogFixture(
        remoteRobot: RemoteRobot,
        remoteComponent: RemoteComponent) : IdeContainerFixture(remoteRobot, remoteComponent) {
    inline fun treeView(function: SettingsTreeViewFixture.() -> Unit = {}): SettingsTreeViewFixture {
        return find<SettingsTreeViewFixture> { it is SettingsTreeView }.apply(function)
    }
}

class SettingsTreeViewFixture(
        remoteRobot: RemoteRobot,
        remoteComponent: RemoteComponent) : ComponentFixture(remoteRobot, remoteComponent)