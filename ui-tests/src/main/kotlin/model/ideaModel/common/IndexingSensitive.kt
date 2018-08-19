package model.ideaModel.common

import com.jetbrains.test.RemoteRobot
import utils.waitWhileIndexing

interface IndexingSensitive {
    val remoteRobot: RemoteRobot

    fun indexingSensitive(action: () -> Unit) {
        remoteRobot.waitWhileIndexing()
        action()
    }
}