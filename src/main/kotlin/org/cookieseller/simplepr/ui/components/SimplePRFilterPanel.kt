package org.cookieseller.simplepr.ui.components

import com.intellij.openapi.ui.ComboBox
import com.intellij.util.ui.JBUI
import javax.swing.JComponent
import javax.swing.JLabel

class SimplePRFilterPanel {
    fun create(): JComponent {

        val combo: ComboBox<String> = ComboBox()
        combo.addItem("Combo")
        val scopesLabel = JLabel("Repositories:")
        scopesLabel.labelFor = combo

        val state: ComboBox<String> =  ComboBox()
        state.addItem("Open") // TODO move into item loader
        state.addItem("Merged")
        state.addItem("Superseded")
        state.addItem("Declined")

        return JBUI.Panels.simplePanel(50, 10)
            .addToCenter(combo)
            .addToLeft(scopesLabel)
            .addToRight(state)
            .withBorder(JBUI.Borders.empty(0, 10))
    }
}