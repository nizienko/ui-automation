package model.ideaModel.mainApp

import com.intellij.openapi.actionSystem.impl.ActionMenu
import com.intellij.openapi.actionSystem.impl.ActionMenuItem
import com.intellij.openapi.wm.impl.IdeMenuBar
import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.RemoteComponent
import com.jetbrains.test.fixtures.ComponentFixture

class IdeMenuBarFixture(
        remoteRobot: RemoteRobot,
        remoteComponent: RemoteComponent) : ComponentFixture(remoteRobot, remoteComponent) {

    fun openMenu(menuItem: String): ActiveMenu {
        execute { r, c ->
            c as IdeMenuBar
            val item = c.components.first { it is ActionMenu && it.text == menuItem }
            r.click(item)
        }
        return ActiveMenu(remoteRobot, remoteComponent)
    }
}

class ActiveMenu(
        remoteRobot: RemoteRobot,
        remoteComponent: RemoteComponent) : ComponentFixture(remoteRobot, remoteComponent) {

    fun select(menuItem: String): ActiveMenu {
        execute { r, _ ->
            val item = r.findActivePopupMenu()?.let { jPopupMenu ->
                jPopupMenu.components.first {
                    (it is ActionMenu && it.text == menuItem) || (it is ActionMenuItem && it.text == menuItem)
                }
            } ?: throw IllegalStateException("No active popup menu")
            r.click(item)
        }
        return this
    }
}