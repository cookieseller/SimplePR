package org.cookieseller.simplepr.ui

import javafx.application.Platform
import javafx.concurrent.Worker
import javafx.embed.swing.JFXPanel
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionListener
import java.net.MalformedURLException
import java.net.URL
import javax.swing.*


class TestBrowser() : JFrame() {
    private val jfxPanel = JFXPanel()
    private var engine: WebEngine? = null

    private val panel = JPanel(BorderLayout())
    private val lblStatus = JLabel()


    private val btnGo = JButton("Go")
    private val txtURL = JTextField()
    private val progressBar = JProgressBar()

    init {
//        initComponents()
    }


    private fun initComponents() {
//        createScene()
        val al = ActionListener { loadURL(txtURL.text) }
        btnGo.addActionListener(al)
        txtURL.addActionListener(al)
        progressBar.preferredSize = Dimension(150, 18)
        progressBar.isStringPainted = true
        val topBar = JPanel(BorderLayout(5, 0))
        topBar.border = BorderFactory.createEmptyBorder(3, 5, 3, 5)
        topBar.add(txtURL, BorderLayout.CENTER)
        topBar.add(btnGo, BorderLayout.EAST)
        val statusBar = JPanel(BorderLayout(5, 0))
        statusBar.border = BorderFactory.createEmptyBorder(3, 5, 3, 5)
        statusBar.add(lblStatus, BorderLayout.CENTER)
        statusBar.add(progressBar, BorderLayout.EAST)
        panel.add(topBar, BorderLayout.NORTH)
//        panel.add(jfxPanel, BorderLayout.CENTER)
        panel.add(statusBar, BorderLayout.SOUTH)
        contentPane.add(panel)
        preferredSize = Dimension(1024, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
        pack()
    }

//    private fun createScene() {
//        Platform.runLater {
//            val view = WebView()
//            engine = view.engine
//            engine?.titleProperty()?.addListener { observable, oldValue, newValue ->
//                SwingUtilities.invokeLater {
//                    this@TestBrowser.setTitle(
//                        newValue
//                    )
//                }
//            }
//            engine?.setOnStatusChanged(EventHandler { event ->
//                SwingUtilities.invokeLater {
//                    lblStatus.text = event.data
//                }
//            })
//            engine?.locationProperty()?.addListener { ov, oldValue, newValue ->
//                SwingUtilities.invokeLater {
//                    txtURL.text = newValue
//                }
//            }
//            engine?.getLoadWorker()?.workDoneProperty()?.addListener { observableValue, oldValue, newValue ->
//                SwingUtilities.invokeLater {
//                    progressBar.value = newValue.toInt()
//                }
//            }
//            engine?.getLoadWorker()
//                ?.exceptionProperty()
//                ?.addListener { o, old, value ->
//                    if (engine?.getLoadWorker()?.state == Worker.State.FAILED) {
//                        SwingUtilities.invokeLater {
//                            JOptionPane.showMessageDialog(
//                                panel,
//                                if (value != null) """
//     ${engine?.getLocation()}
//     ${value.message}
//     """.trimIndent() else """
//     ${engine?.getLocation()}
//     Unexpected error.
//     """.trimIndent(),
//                                "Loading error...",
//                                JOptionPane.ERROR_MESSAGE
//                            )
//                        }
//                    }
//                }
//            jfxPanel.scene = Scene(view)
//        }
//    }
//
    fun loadURL(url: String) {
        Platform.runLater {
            var tmp = toURL(url)
            if (tmp == null) {
                tmp = toURL("http://$url")
            }
            engine!!.load(tmp)
        }
    }

    private fun toURL(str: String): String? {
        return try {
            URL(str).toExternalForm()
        } catch (exception: MalformedURLException) {
            null
        }
    }
}