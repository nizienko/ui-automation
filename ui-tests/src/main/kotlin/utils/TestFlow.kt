package utils

import com.jetbrains.test.data.IdeaSideError
import model.ideaModel.IdeaApp

inline fun ideaTest(app: IdeaApp, test: IdeaApp.() -> Unit) {
    // check status in launcher
    app.test()
}

// actions which are not necessary to finish
inline fun optional(action: ()-> Unit) {
    try {
        action()
    } catch (ignore: IdeaSideError) {
        println("Optional action ignored")
    }
}