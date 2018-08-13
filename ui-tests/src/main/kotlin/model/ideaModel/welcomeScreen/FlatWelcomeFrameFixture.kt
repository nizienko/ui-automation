package model.ideaModel.welcomeScreen

import com.intellij.ui.components.labels.ActionLink
import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.ComponentDescription
import com.jetbrains.test.fixtures.BaseFixture
import model.ideaModel.common.ContainerFixture


class FlatWelcomeFrameFixture(remoteRobot: RemoteRobot, description: ComponentDescription) : ContainerFixture(remoteRobot, description) {
    val createNewProjectLink
        get() = findComponent<BaseFixture> { it is ActionLink && it.text == "Create New Project" }
}