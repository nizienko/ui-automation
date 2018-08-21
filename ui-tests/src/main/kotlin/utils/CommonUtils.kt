package utils

import model.ideaModel.IdeaApp


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

inline fun test(app: IdeaApp, test: IdeaApp.()->Unit) {
    app.test()
}