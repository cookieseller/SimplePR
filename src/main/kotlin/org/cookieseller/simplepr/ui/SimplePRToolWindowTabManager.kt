package org.cookieseller.simplepr.ui

import com.beust.klaxon.JsonObject
import com.intellij.openapi.application.invokeAndWaitIfNeeded
import com.intellij.openapi.diff.impl.patch.FilePatch
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.ui.ChangesTree
import com.intellij.openapi.vcs.changes.ui.TreeModelBuilder
import com.intellij.openapi.vcs.changes.ui.VcsTreeModelData
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.tree.TreeUtil
import com.intellij.vcsUtil.VcsUtil
import git4idea.GitContentRevision
import git4idea.GitRevisionNumber
import org.cookieseller.simplepr.message.DiffLoadListener
import org.cookieseller.simplepr.message.OpenPRListener
import org.cookieseller.simplepr.services.RepositoryService
import org.cookieseller.simplepr.services.UiUpdateInterface
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.JComponent
import javax.swing.JPanel

class SimplePRToolWindowTabManager(val project: Project, private val repositoryService: RepositoryService) {

    lateinit var content: Content

    fun createTab(tabName: String): Content {
        content = ContentFactory.SERVICE.getInstance().createContent(JPanel(GridLayout(1,0)), tabName, false)
        content.isCloseable = true
        content.setDisposer(Disposer.newDisposable())

        dataLoadHandler()

        return content
    }

    private fun createCenterPanel(model: List<Change>): JComponent? {
        return createChangesTree(model)
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

    private fun dataLoadHandler() {
        project.messageBus.connect().apply {
            subscribe(DiffLoadListener.TOPIC, object : DiffLoadListener {
                override fun diffLoaded(lines: List<FilePatch>) {
                    val emptyList: MutableList<Change> = mutableListOf()
                    lines.forEach {
                        val beforeRef = it.beforeVersionId
                        val afterRef = it.afterVersionId
                        if (beforeRef != null && afterRef != null) {
                            val change = createChangeFromPatch(beforeRef, afterRef, it)
                            emptyList.add(change)
                        }
                    }
                    invokeAndWaitIfNeeded {
                        content.component.add(createCenterPanel(emptyList))
                    }
                }

                override fun loadingError(error: String) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun createChangeFromPatch(beforeRef: String, afterRef: String, patch: FilePatch): Change {
        val project = project
        val (beforePath, afterPath) = getPatchPaths(patch)
        val beforeRevision = beforePath?.let { GitContentRevision.createRevision(it, GitRevisionNumber(beforeRef), project) }
        val afterRevision = afterPath?.let { GitContentRevision.createRevision(it, GitRevisionNumber(afterRef), project) }

        return Change(beforeRevision, afterRevision)
    }

    private fun getPatchPaths(patch: FilePatch): Pair<FilePath?, FilePath?> {
        val beforeName = if (patch.isNewFile) null else patch.beforeName
        val afterName = if (patch.isDeletedFile) null else patch.afterName

        return beforeName?.let { VcsUtil.getFilePath(project.workspaceFile!!, it) } to afterName?.let { VcsUtil.getFilePath(
            project.workspaceFile!!, it) }
    }
}