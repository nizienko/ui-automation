package model.ideaModel.common

import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.fixtures.ComponentFixture
import utils.waitFor
import javax.swing.JLabel

interface IndexingSensitive {
    val remoteRobot: RemoteRobot

    fun indexingSensitive(action: () -> Unit) {
        waitWhileIndexing()
        try {
            action()
        } catch (e: Throwable) {
            if (isIndexingInProgress()) {
                waitWhileIndexing()
                action()
            } else {
                throw e
            }
        }
    }

    private fun waitWhileIndexing() {
        waitFor(60 * 5) {
            isIndexingInProgress().not()
        }
        if (isIndexingInProgress()) {
            throw IllegalStateException("Indexing still in progress")
        }
    }

    private fun isIndexingInProgress(): Boolean {
        return remoteRobot.findAll<ComponentFixture> {
            it is JLabel && ("Indexing..." == it.text
                    || "Scanning files to index..." == it.text
                    || "Updating Indices" == it.text)
        }.isNotEmpty()
    }
}

