package model.ideaModel.welcomeScreen

import com.intellij.ui.components.labels.ActionLink
import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.RemoteComponent
import com.jetbrains.test.fixtures.ComponentFixture
import model.ideaModel.common.IdeContainerFixture


class FlatWelcomeFrameFixture(
        remoteRobot: RemoteRobot,
        remoteComponent: RemoteComponent) : IdeContainerFixture(remoteRobot, remoteComponent) {

    val createNewProjectLink
        get() = find<ComponentFixture> { it is ActionLink && it.text == "Create New Project" }
}