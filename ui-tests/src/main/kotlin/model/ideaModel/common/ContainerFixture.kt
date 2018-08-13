package model.ideaModel.common

import com.intellij.ui.components.JBList
import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.ComponentDescription
import com.jetbrains.test.fixtures.BaseFixture
import javax.swing.JButton

open class ContainerFixture(remoteRobot: RemoteRobot, description: ComponentDescription) : BaseFixture(remoteRobot, description) {
    fun jbList(containsItem: String): JBListFixture {
        return findComponent<JBListFixture> {c->
            c.isShowing && c is JBList<*>
            && (0 until c.model.size).map { c.model.getElementAt(it).toString() }.any { it == containsItem }
        }
    }

    fun jButton(text: String):BaseFixture  {
        return findComponent<BaseFixture> { it.isShowing && it is JButton && it.text == text }
    }
}