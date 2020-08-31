package org.cookieseller.simplepr.services

import com.beust.klaxon.JsonObject

interface UiUpdateInterface {
    fun updateUi(json: JsonObject)
}