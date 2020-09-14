package org.cookieseller.simplepr.ui.components

import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.ui.ChangesTree
import com.intellij.openapi.vcs.changes.ui.TreeModelBuilder
import com.intellij.openapi.vcs.changes.ui.VcsTreeModelData
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.tree.TreeUtil
import javax.swing.JComponent

class DiffDialog(val project: Project, val model: List<Change>) : DialogWrapper(true) {

    init {
        init()
    }

    override fun createCenterPanel(): JComponent? {
        val x = panel {}
        x.add(JBUI.Panels.simplePanel(createChangesTree(model)))
        return x
    }

    private fun createChangesTree(model: List<Change>): JComponent {
        val tree = object : ChangesTree(project, false, false) {
            override fun rebuildTree() {
                updateTreeModel(TreeModelBuilder(project, grouping).setChanges(model, null).build())
                if (isSelectionEmpty && !isEmpty) TreeUtil.selectFirstNode(this)
            }

            override fun getData(dataId: String) =
                super.getData(dataId) ?: VcsTreeModelData.getData(project, this, dataId)
        }

        (tree::rebuildTree)()

        return ScrollPaneFactory.createScrollPane(tree, true)
    }
}
