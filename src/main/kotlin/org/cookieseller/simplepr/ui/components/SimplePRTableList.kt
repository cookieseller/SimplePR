package org.cookieseller.simplepr.ui.components

import com.intellij.openapi.project.Project
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.intellij.util.ui.components.BorderLayoutPanel
import org.jdesktop.swingx.VerticalLayout
import javax.swing.JPanel
import javax.swing.JScrollPane

class SimplePRTableList {
    fun create(): BorderLayoutPanel {
        val table = JBTable()
        val tablePanel = createTablePanel(table)

        val filterPanel = JPanel(VerticalLayout(0)).apply {
            add(SimplePRFilterPanel().create())
        }

        return JBUI.Panels.simplePanel(tablePanel).addToTop(filterPanel)
    }

    private fun createTablePanel(table: JBTable): JScrollPane {
        return ScrollPaneFactory.createScrollPane(table, true).apply {
            isOpaque = false
            viewport.isOpaque = false

            verticalScrollBar.apply {
                isOpaque = true
                UIUtil.putClientProperty(this, JBScrollPane.IGNORE_SCROLLBAR_IN_INSETS, false)
            }
        }
    }
}