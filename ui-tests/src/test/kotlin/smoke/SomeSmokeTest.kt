package smoke

import com.intellij.openapi.project.DumbService
import com.intellij.testFramework.LightPlatformTestCase.getProject
import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.attempt
import model.ideaModel.IdeaApp
import org.junit.jupiter.api.Test
import utils.optional
import utils.test
import java.lang.Thread.sleep


class SomeSmokeTest {
    // todo: settings
    private val url = "http://127.0.0.1:8080"

    private val app = IdeaApp(RemoteRobot(url))     // could be ThreadLocal for parallel runs

    @Test
    fun createNewProject() = test(app) {
        welcomeScreen {
            createNewProjectLink.click()
            jDialog("New Project") {
                jbList("Java").selectItem("Empty Project")
                jButton("Next").click()
                jButton("Finish").click()
            }
        }
        ideaFrame {
            optional {
                jDialog("Tip of the Day") {
                    jButton("Close").click()
                }
            }
            indexingSensitive {
                jDialog("Project Structure") {
                    jButton("Cancel").click()
                }
            }
            println(title)
        }
    }

    @Test
    fun runTopInTerminal() {
        app.ideaFrame {
            terminalButton.click()
            terminalPanel.enterCommand("top")
            sleep(5000)
            terminalPanel.sendCancel()
            terminalButton.click()
        }
    }

    @Test
    fun chooseSettingsMenu() {
        app.ideaFrame {
            openSettings()
            jDialog("Settings") {
                jbList("Appearance & Behavior").selectItem("Plugins")
                jbList("Android Support").selectItem("EditorConfig")
            }
        }
    }

    @Test
    fun checkIndexing() {
        with(app) {
            ideaFrame {
                indexingSensitive {
                    println("Indexing finished")
                }
            }
        }
    }

    @Test
    fun retrieveAnyTest() {
        with(app) {
            println(robot.retrieve { DumbService.getInstance(getProject()).isDumb })
        }
    }
}