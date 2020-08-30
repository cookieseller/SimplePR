package org.cookieseller.simplepr.ui

import com.intellij.openapi.ui.DialogWrapper
import java.awt.GridLayout
import javax.swing.*


class CredentialDialog() : DialogWrapper(true) {

    private val username = JTextField()
    private val password = JPasswordField()

    init {
        init()
    }

    override fun createCenterPanel(): JComponent? {
        title = "Enter Credentials"
        val dialogPanel = JPanel(GridLayout(2, 2))

        val labelUsername = JLabel("Username: ", JLabel.TRAILING)
        labelUsername.labelFor = username

        dialogPanel.add(labelUsername)
        dialogPanel.add(username)

        val labelPassword = JLabel("Password: ", JLabel.TRAILING)
        labelPassword.labelFor = password

        dialogPanel.add(labelPassword)
        dialogPanel.add(password)

        return dialogPanel;
    }

    fun getUsername(): String {
        return username.text ?: ""
    }

    fun getPassword(): String {
        return password.password.toString()
    }
}