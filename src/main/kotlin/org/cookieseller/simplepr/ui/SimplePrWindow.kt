package org.cookieseller.simplepr.ui

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBList
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.JBUI
import org.cookieseller.simplepr.services.CredentialStorageService
import org.cookieseller.simplepr.services.RepositoryService
import java.awt.BorderLayout
import java.awt.Desktop
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.net.URI
import javax.swing.*


class SimplePrWindow() : SimpleToolWindowPanel(false) {
    private val combo: ComboBox<String> = ComboBox()
    private val myActiveVCSs: JBList<String> = JBList()

    init {
        val group = DefaultActionGroup()
        group.add(object : AnAction("Add Remote", "Add Remote", AllIcons.General.Add) {
            override fun actionPerformed(e: AnActionEvent) {
                GrantAccessDialog().showAndGet()
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(URI("https://www.google.com"));
                }
//              https://bitbucket.org/site/oauth2/authorize?client_id=W4tc62sawzm2uw4fVT&response_type=code&state=f9fdc5cd-baa3-45a5-b9f5-4becb4c02f3b
            }
        })
        group.add(object : AnAction("Refresh", "Reload repository list", AllIcons.Actions.Refresh) {
            override fun actionPerformed(e: AnActionEvent) {
                populateRepositoryList()
            }
        })

        setContent(createCenterPanel())
        toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, group, false).component
    }

    private fun populateRepositoryList() {
        RepositoryService().getRepositoriesForProject().forEach {
            combo.addItem(it)
        }
    }

    private fun credentialChallenge(url: String): Boolean {
        val dialogWrapper = GrantAccessDialog()
        dialogWrapper.show()
        if (dialogWrapper.exitCode == DialogWrapper.OK_EXIT_CODE) {
//            val username = dialogWrapper.getUsername()
//            val password = dialogWrapper.getPassword()
//            CredentialStorageService().storeCredentials(url, username, password)

            return true
        }

        return false
    }

    private fun populatePullRequestList() {
        val model = DefaultListModel<String>()

        val url = combo.selectedItem as String
        val credentialService = CredentialStorageService()
        if (!credentialService.hasCredentials(url) and !credentialChallenge(url)) {
            return
        }

        val userName = CredentialStorageService().getUsername(url) ?: ""
        val password = CredentialStorageService().getPassword(url) ?: ""

        RepositoryService().getPullRequests(combo.selectedItem as String, userName, password)
        model.addElement("Test")
    }

    private fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())

        combo.setMinimumAndPreferredWidth(JBUIScale.scale(500))
        combo.addActionListener { populatePullRequestList() }
        populateRepositoryList()

        val chooserPanel = JPanel(GridBagLayout())
        val scopesLabel = JLabel("Repositories:")
        scopesLabel.setDisplayedMnemonic('R')
        scopesLabel.labelFor = combo
        val gc = GridBagConstraints(
            GridBagConstraints.RELATIVE, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            JBUI.insets(2, 8, 2, 4), 0, 0
        )
        chooserPanel.add(scopesLabel, gc)
        gc.insets = JBUI.insets(2)
        chooserPanel.add(combo, gc)

        gc.fill = GridBagConstraints.HORIZONTAL
        gc.weightx = 1.0

        chooserPanel.add(Box.createHorizontalBox(), gc)
        panel.add(chooserPanel, BorderLayout.NORTH)
        panel.add(myActiveVCSs, BorderLayout.CENTER)

        return panel
    }
}