package org.cookieseller.simplepr.message

import com.intellij.util.messages.Topic

interface UpdatePRListener {

    companion object {
        val TOPIC = Topic.create("Pull Request data operations", UpdatePRListener::class.java)
    }

    fun updatePR()
}