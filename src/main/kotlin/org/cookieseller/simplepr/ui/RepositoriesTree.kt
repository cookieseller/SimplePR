package org.cookieseller.simplepr.ui

import com.intellij.openapi.Disposable
import com.intellij.ui.treeStructure.Tree
import org.cookieseller.simplepr.services.CredentialStorageService
import org.cookieseller.simplepr.services.RepositoryService
import javax.swing.event.TreeSelectionEvent
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class RepositoriesTree() : Tree(), Disposable {

    init {
        model = DefaultTreeModel(DefaultMutableTreeNode())
        isRootVisible = false
        addTreeSelectionListener { v -> selectionChanged(v) }
        reloadRepositories()
    }

    private fun selectionChanged(selectionChanged: TreeSelectionEvent) {
        val url = selectionChanged.newLeadSelectionPath.lastPathComponent.toString()

        val credentialStorageService = CredentialStorageService()
        if (credentialStorageService.hasCredentials(url)) {
            val repositoryService = RepositoryService()

            val username = credentialStorageService.getUsername(url)?: return
            val password = credentialStorageService.getPassword(url)?: return
            repositoryService.getPullRequests(url, username, password)

            return
        }
    }

    fun reloadRepositories() {
        val rootNode = DefaultMutableTreeNode()
        val vcsList = DefaultTreeModel(rootNode)

        val repositoryService = RepositoryService()
        for (repository in repositoryService.getRepositoriesForProject())
            vcsList.insertNodeInto(DefaultMutableTreeNode(repository), rootNode, 0)

        model = vcsList
    }

    override fun dispose() {
    }
}