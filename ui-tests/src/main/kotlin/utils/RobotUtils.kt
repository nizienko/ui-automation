package utils

import com.jetbrains.test.data.IdeaSideError
import org.fest.swing.core.Robot

inline fun Robot.pressingKey(key: Int, action: () -> Unit) {
    this.pressKey(key)
    action()
    this.releaseKey(key)
}

inline fun optional(action: ()-> Unit) {
    try {
        action()
    } catch (ignore: IdeaSideError) {
        println("Optional action ignored")
    }
}