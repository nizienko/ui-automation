package model.ideaModel.mainApp

import com.intellij.openapi.actionSystem.impl.ActionMenu
import com.intellij.openapi.actionSystem.impl.ActionMenuItem
import com.intellij.openapi.wm.impl.IdeMenuBar
import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.ComponentDescription
import com.jetbrains.test.fixtures.BaseFixture

class IdeMenuBarFixture(remoteRobot: RemoteRobot, description: ComponentDescription) : BaseFixture(remoteRobot, description) {

    fun openMenu(menuItem: String): IdeMenuBarFixture {
        execute { r, c ->
            c as IdeMenuBar
            val item = c.components.first { it is ActionMenu && it.text == menuItem }
            r.click(item)
        }
        return this
    }

    fun onActiveMenuSelect(menuItem: String): IdeMenuBarFixture {
        execute { r, _ ->
            val item = r.findActivePopupMenu().components.first {
                (it is ActionMenu && it.text == menuItem) || (it is ActionMenuItem && it.text == menuItem)
            }
            r.click(item)
        }
        return this
    }
}