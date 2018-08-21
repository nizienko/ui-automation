package model.ideaModel

import com.intellij.openapi.wm.impl.welcomeScreen.FlatWelcomeFrame
import com.jetbrains.test.RemoteRobot
import model.ideaModel.mainApp.IdeFrameFixture
import model.ideaModel.welcomeScreen.FlatWelcomeFrameFixture
import javax.swing.JFrame

class IdeaApp(val robot: RemoteRobot) {

    inline fun ideaFrame(function: IdeFrameFixture.() -> Unit) {
        robot.find<IdeFrameFixture> {
            it is JFrame
                    && it.isShowing
                    && it.title.contains("IntelliJ IDEA")
                    && it.title.contains("Welcome to IntelliJ IDEA").not()
        }.function()
    }

    inline fun welcomeScreen(function: FlatWelcomeFrameFixture.() -> Unit): FlatWelcomeFrameFixture {
        return robot.find<FlatWelcomeFrameFixture> { it is FlatWelcomeFrame && it.isShowing }.apply(function)
    }
}