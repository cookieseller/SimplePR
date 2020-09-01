package org.cookieseller.simplepr.model

enum class PRStates(val state: String) {
    OPEN("Open"),
    MERGED("Merged"),
    SUPERSEDED("Superseded"),
    DECLINED("Declined")
}