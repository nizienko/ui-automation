package utils

import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.fixtures.ComponentFixture
import javax.swing.JLabel


fun RemoteRobot.waitWhileIndexing() {
    waitFor(60 * 5) {
        findAll<ComponentFixture> {
            it is JLabel && ("Indexing..." == it.text
                    || "Scanning files to index..." == it.text
                    || "Updating Indices" == it.text)
        }.isEmpty()
    }
}

inline fun waitFor(seconds: Int, condition: () -> Boolean) {
    val endTime = System.currentTimeMillis() + seconds * 1000
    while (System.currentTimeMillis() < endTime) {
        if (condition()) {
            break
        } else {
            println(".....waiting ${endTime - System.currentTimeMillis()} more")
            Thread.sleep(1000)
        }
    }
}