package org.cookieseller.simplepr.ui

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.scale.JBUIScale
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.JBUI
import org.cookieseller.simplepr.services.CredentialStorageService
import org.cookieseller.simplepr.services.RepositoryService
import org.cookieseller.simplepr.services.UiUpdateInterface
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.util.*
import javax.swing.*
import javax.swing.table.DefaultTableModel


class SimplePrWindow() : SimpleToolWindowPanel(false) {
    private val combo: ComboBox<String> = ComboBox()
    private val myActiveVCSs: JBTable = JBTable()

    init {
        val group = DefaultActionGroup()
//        group.add(object : AnAction("Add Remote", "Add Remote", AllIcons.General.Add) {
//            override fun actionPerformed(e: AnActionEvent) {
//            }
//        })
        group.add(object : AnAction("Refresh", "Refresh repository list", AllIcons.Actions.Refresh) {
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
        val credentialDialog = CredentialDialog(url)
        credentialDialog.show()
        if (credentialDialog.exitCode == DialogWrapper.OK_EXIT_CODE) {
            CredentialStorageService().storeCredentials(url, credentialDialog.username, credentialDialog.password)
            return true
        }

        return false
    }

    private fun populatePullRequestList() {
        val url = combo.selectedItem as String
        val credentialService = CredentialStorageService()
        if (!credentialService.hasCredentials(url) && !credentialChallenge(url)) {
            return
        }

        val userName = CredentialStorageService().getUsername(url) ?: ""
        val password = CredentialStorageService().getPassword(url) ?: ""

        val repositoryService = RepositoryService()
        repositoryService.onUpdateHandler(object : UiUpdateInterface {
            override fun updateUi(json: JsonObject) {
                val pullRequests = json["values"] as JsonArray<*>
                val model = DefaultTableModel(arrayOf("Title", "Author", "Date"), 0)
                pullRequests.forEach {
                    val item = it as JsonObject

                    val row = Vector<String>(3)
                    row.add(item["title"].toString())
                    row.add((item["author"] as JsonObject)["display_name"].toString())
                    row.add(item["created_on"].toString())
                    model.addRow(row)
                }
                myActiveVCSs.model = model
            }
        })
        repositoryService.getPullRequests(combo.selectedItem as String, userName, password)
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