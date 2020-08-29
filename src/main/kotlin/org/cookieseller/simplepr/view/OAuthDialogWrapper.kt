package org.cookieseller.simplepr.view

import com.intellij.openapi.ui.DialogWrapper
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel


class OAuthDialogWrapper(): DialogWrapper(true) {

    private val jfxPanel: JFXPanel = JFXPanel()
    init {
        title = "Test"

        init()
    }

    override fun createCenterPanel(): JComponent  {
        Platform.runLater {
            val root = WebView()
            val engine: WebEngine = root.engine

            val scene = Scene(root)
            jfxPanel.scene = scene
            engine.load("https://google.com")
        }
        val panel = JPanel(BorderLayout())
//        val browser = SimpleSwingBrowser()
//        browser.isVisible = true
//        browser.loadURL("http://oracle.com")



//        stage.show()

//        val scopesLabel = JLabel("Repositories:")
//        panel.add(scopesLabel, BorderLayout.NORTH)
        panel.add(jfxPanel, BorderLayout.CENTER)
//        panel.add(sc)

//        combo.setMinimumAndPreferredWidth(JBUIScale.scale(500))
//        combo.addActionListener { populatePullRequestList() }
//        populateRepositoryList()
//
//        val chooserPanel = JPanel(GridBagLayout())
//        val scopesLabel = JLabel("Repositories:")
//        scopesLabel.setDisplayedMnemonic('R')
//        scopesLabel.labelFor = combo
//        val gc = GridBagConstraints(
//            GridBagConstraints.RELATIVE, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
//            JBUI.insets(2, 8, 2, 4), 0, 0
//        )
//        chooserPanel.add(scopesLabel, gc)
//        gc.insets = JBUI.insets(2)
//        chooserPanel.add(combo, gc)
//
//        gc.fill = GridBagConstraints.HORIZONTAL
//        gc.weightx = 1.0
//
//        chooserPanel.add(Box.createHorizontalBox(), gc)
//        panel.add(chooserPanel, BorderLayout.NORTH)
//        panel.add(myActiveVCSs, BorderLayout.CENTER)

        return panel
    }
}