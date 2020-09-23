package org.cookieseller.simplepr.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import org.cookieseller.simplepr.message.OpenPRListener
import org.cookieseller.simplepr.services.RepositoryService

class SimplePRToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val simplePrContent = contentFactory.createContent(null, "SimplePR",false)

        simplePrContent.component = SimplePrWindow(project)
        simplePrContent.isCloseable = false
        toolWindow.contentManager.addContent(simplePrContent)

        project.messageBus.connect().apply {
            subscribe(OpenPRListener.TOPIC, object : OpenPRListener {
                override fun openPR(name: String, repositoryService: RepositoryService) {
                    val tab = SimplePRToolWindowTabManager(project, repositoryService).createTab(name)
                    toolWindow.contentManager.addContent(tab)
                }
            })
        }
    }
}
