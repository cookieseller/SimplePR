package org.cookieseller.simplepr.ui

import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.UIBundle
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.RowBuilder
import com.intellij.ui.layout.panel
import git4idea.i18n.GitBundle
import javax.swing.JComponent


class CredentialDialog(url: String) : DialogWrapper(true) {

    private val usernameField = JBTextField("", 20).apply { isEditable = true }
    private val passwordField = JBPasswordField()
    private var useCredentialHelper = false
    private lateinit var dialogPanel: DialogPanel
    private val rememberCheckbox = JBCheckBox(UIBundle.message("auth.remember.cb"), true)

    var username: String
        get() = usernameField.text
        internal set(value) {
            usernameField.text = value
        }

    var password: String
        get() = String(passwordField.password!!)
        internal set(value) {
            passwordField.text = value
        }

    var rememberPassword: Boolean
        get() = rememberCheckbox.isSelected
        internal set(value) {
            rememberCheckbox.isSelected = value
        }

    init {
        title = url
        setOKButtonText("Login")
        init()
    }

    override fun createCenterPanel(): JComponent? {
        dialogPanel = panel {
            row(GitBundle.message("login.dialog.prompt.enter.credentials")) {}
            buildCredentialsPanel()
        }

        return dialogPanel
    }

    private fun RowBuilder.buildCredentialsPanel() {
        row(GitBundle.message("login.dialog.username.label")) { usernameField(growX) }
        row(GitBundle.message("login.dialog.password.label")) { passwordField(growX) }
        row { rememberCheckbox() }
    }

    override fun doValidateAll(): List<ValidationInfo> {
        dialogPanel.apply()
        if (useCredentialHelper) {
            return emptyList()
        }
        return listOfNotNull(
            if (username.isBlank())
                ValidationInfo(GitBundle.message("login.dialog.error.username.cant.be.empty"), usernameField)
            else null,
            if (passwordField.password.isEmpty())
                ValidationInfo(GitBundle.message("login.dialog.error.password.cant.be.empty"), passwordField)
            else null
        )
    }
}