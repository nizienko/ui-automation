package utils

import org.fest.swing.core.Robot

inline fun Robot.pressingKey(key: Int, action: () -> Unit) {
    this.pressKey(key)
    action()
    this.releaseKey(key)
}

