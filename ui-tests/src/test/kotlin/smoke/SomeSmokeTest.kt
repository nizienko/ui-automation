package smoke

import com.jetbrains.test.RemoteRobot
import model.ideaModel.IdeaApp
import org.junit.jupiter.api.Test


class SomeSmokeTest {
    private val url = "http://127.0.0.1:8080"

    private val app = IdeaApp(RemoteRobot(url))

    @Test
    fun createNewProject() {
        with(app) {
            welcomeScreen {
                createNewProjectLink.click()
            }
            dialog("New Project") {
                jbList("Java").selectItem("Empty Project")
                jButton("Next").click()
                jButton("Finish").click()
            }
            dialog("Project Structure") {
                jButton("Cancel").click()
            }
        }
    }

    @Test
    fun runTopInTerminal() {
        with(app) {
            ideaFrame {
                terminalButton.click()
                terminalPanel.enterCommand("top")
                java.lang.Thread.sleep(5000)
                terminalPanel.sendCancel()
                terminalButton.click()
            }
        }
    }

    @Test
    fun chooseSettingsMenu() {
        with(app) {
            ideaFrame {
                mainMenu.openMenu("File").onActiveMenuSelect("Settings...")
            }
        }
    }
}