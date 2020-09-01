package org.cookieseller.simplepr.ui.components

import com.beust.klaxon.JsonObject
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import java.util.*
import javax.swing.table.DefaultTableModel

class PRTable {

    private val prTable: JBTable = JBTable()

    init {
        val scrollPane = JBScrollPane(prTable)
        val model = DefaultTableModel(arrayOf("Title", "Author", "Date"), 0)
    }

    fun addRow(columns: Vector<String>) {
        val model = prTable.model as DefaultTableModel
        model.addRow(columns)
    }

    fun clear() {
        prTable.model = PRTableModel()
    }

    fun getComponent(): JBScrollPane {
        return JBScrollPane(prTable)
    }

    inner class PRTableModel : DefaultTableModel() {

    }
}