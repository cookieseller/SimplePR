package org.cookieseller.simplepr.ui

import com.intellij.openapi.ui.DialogWrapper
import org.jetbrains.annotations.NotNull
import java.awt.GridLayout
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel


class GrantAccessDialog() : DialogWrapper(true) {

    init {
        init()
    }

    override fun createActions(): Array<out @NotNull Action> {
        super.createDefaultActions()

        okAction.putValue(Action.NAME, "Grant access")
        return arrayOf(okAction, cancelAction)
    }

    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(GridLayout(1, 1))

        val labelUsername = JLabel("Grant SimplePR access to Bitbucket", JLabel.TRAILING)
        dialogPanel.add(labelUsername)

        return dialogPanel;
    }
}