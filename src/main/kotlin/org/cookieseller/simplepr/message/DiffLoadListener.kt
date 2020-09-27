package org.cookieseller.simplepr.message

import com.intellij.openapi.diff.impl.patch.FilePatch
import com.intellij.util.messages.Topic
import org.cookieseller.simplepr.services.RepositoryService

interface DiffLoadListener {

    companion object {
        val TOPIC = Topic.create("Load diff", DiffLoadListener::class.java)
    }

    fun diffLoaded(lines: List<FilePatch>)
    fun loadingError(error: String)
}