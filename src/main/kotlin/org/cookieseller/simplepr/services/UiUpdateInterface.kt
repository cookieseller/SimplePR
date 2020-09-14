package org.cookieseller.simplepr.services

import com.beust.klaxon.JsonObject
import com.intellij.openapi.diff.impl.patch.FilePatch

interface UiUpdateInterface {
    fun updateUi(json: JsonObject)
    fun updateDiff(lines: List<FilePatch>)
}