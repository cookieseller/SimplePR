package org.cookieseller.simplepr

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import org.cookieseller.simplepr.view.MyToolWindow

class SimplePRToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val allTodosContent = contentFactory.createContent(
            null,
            "BitbucketPR",
            false
        )

        allTodosContent.component = MyToolWindow(false)
        toolWindow.contentManager.addContent(allTodosContent)


        val testTodosContent = contentFactory.createContent(
            null,
            "BitbucketPR",
            false
        )

        testTodosContent.component = MyToolWindow(false)
        toolWindow.contentManager.addContent(testTodosContent)
    }
}
