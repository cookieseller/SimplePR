package org.cookieseller.simplepr.message

import com.intellij.util.messages.Topic
import org.cookieseller.simplepr.services.RepositoryService

interface OpenPRListener {

    companion object {
        val TOPIC = Topic.create("Pull Request data operations", OpenPRListener::class.java)
    }

    fun openPR(name: String, repositoryService: RepositoryService)
}