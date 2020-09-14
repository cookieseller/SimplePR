package org.cookieseller.simplepr.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class SimplePRToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val simplePrContent = contentFactory.createContent(null, "SimplePR",false)

        simplePrContent.component = SimplePrWindow(project)
        toolWindow.contentManager.addContent(simplePrContent)
    }
}
