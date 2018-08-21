package smoke

import com.intellij.openapi.project.DumbService
import com.intellij.testFramework.LightPlatformTestCase.getProject
import com.jetbrains.test.RemoteRobot
import model.ideaModel.IdeaApp
import org.junit.jupiter.api.Test
import utils.optional
import utils.ideaTest
import java.lang.Thread.sleep


class SomeSmokeTest {
    private val url = "http://127.0.0.1:8080"
    private val app = IdeaApp(RemoteRobot(url))     // could be ThreadLocal for parallel runs

    @Test
    fun createNewProject() = ideaTest(app) {
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
    fun runTopInTerminal() = ideaTest(app) {
        ideaFrame {
            terminalButton.click()
            terminalPanel.enterCommand("top")
            sleep(5000)
            terminalPanel.sendCancel()
            terminalButton.click()
        }
    }

    @Test
    fun chooseSettingsMenu() = ideaTest(app) {
        ideaFrame {
            openSettings()
            settingsDialog {
                treeView().click()
                jButton("Cancel").click()
            }
        }
    }

    @Test
    fun checkIndexing() = ideaTest(app) {
        ideaFrame {
            indexingSensitive {
                println("Indexing finished")
            }
        }
    }

    @Test
    fun retrieveAnyTest() = ideaTest(app) {
        println(robot.retrieve { DumbService.getInstance(getProject()).isDumb })
    }
}