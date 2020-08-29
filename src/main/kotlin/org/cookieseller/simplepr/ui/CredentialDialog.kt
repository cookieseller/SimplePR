package org.cookieseller.simplepr.ui

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.GridBag
import java.awt.*
import javax.swing.*


class CredentialDialog() : DialogWrapper(true) {

    private val username: JTextField = JTextField()
    private val password: JPasswordField = JPasswordField()

    init {
        init()
    }

    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(GridLayout(2, 2))

        val labelUsername = JLabel("Username: ", JLabel.TRAILING)
        dialogPanel.add(labelUsername)

        labelUsername.labelFor = username
        dialogPanel.add(username)

        val labelPassword = JLabel("Password: ", JLabel.TRAILING)
        dialogPanel.add(labelPassword)
        labelPassword.labelFor = password
        dialogPanel.add(password)

        return dialogPanel;
    }

    fun getUsername(): String {
        return username.text
    }

    fun getPassword(): CharArray? {
        return password.password
    }
}