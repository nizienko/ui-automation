package model.ideaModel

import com.intellij.openapi.wm.impl.welcomeScreen.FlatWelcomeFrame
import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.fixtures.BaseFixture
import model.ideaModel.common.ContainerFixture
import model.ideaModel.mainApp.IdeaFrameFixture
import model.ideaModel.welcomeScreen.FlatWelcomeFrameFixture
import javax.swing.JDialog
import javax.swing.JFrame

class IdeaApp(val robot: RemoteRobot) {
    fun ideaFrame(function: IdeaFrameFixture.() -> Unit) {
        robot.findComponent<IdeaFrameFixture> {
            it is JFrame
                    && it.title.contains("IntelliJ IDEA")
                    && it.title.contains("Welcome to IntelliJ IDEA").not()
        }.function()
    }

    fun welcomeScreen(function: FlatWelcomeFrameFixture.() -> Unit) {
        robot.findComponent<FlatWelcomeFrameFixture> { it is FlatWelcomeFrame }.function()
    }

    fun dialog(tittle: String, function: ContainerFixture.() -> Unit) {
        robot.findComponent<ContainerFixture> { it is JDialog && it.isShowing && it.title == tittle }.function()
    }
}