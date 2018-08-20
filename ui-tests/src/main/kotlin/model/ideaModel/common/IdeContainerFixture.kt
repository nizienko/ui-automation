package model.ideaModel.common

import com.intellij.ui.components.JBList
import com.jetbrains.test.RemoteRobot
import com.jetbrains.test.data.RemoteComponent
import com.jetbrains.test.fixtures.ComponentFixture
import com.jetbrains.test.fixtures.ContainerFixture
import javax.swing.JButton
import javax.swing.JDialog

open class IdeContainerFixture(
        remoteRobot: RemoteRobot,
        remoteComponent: RemoteComponent) : ContainerFixture(remoteRobot, remoteComponent) {

    fun jbList(containsItem: String, function: JBListFixture.() -> Unit = {}): JBListFixture {
        return find<JBListFixture> { c ->
            c.isShowing && c is JBList<*> && (0 until c.model.size)
                    .map { c.model.getElementAt(it).toString() }
                    .any { it == containsItem }
        }.apply(function)
    }

    fun jButton(text: String): ComponentFixture {
        return find { it.isShowing && it is JButton && it.text == text }
    }

    fun jDialog(title: String, function: IdeContainerFixture.() -> Unit = {}): IdeContainerFixture {
        return find<IdeContainerFixture> { it is JDialog && it.isShowing && it.title == title }
                .apply(function)
    }
}