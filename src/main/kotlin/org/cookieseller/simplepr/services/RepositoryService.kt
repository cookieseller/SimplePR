package org.cookieseller.simplepr.services

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.intellij.dvcs.DvcsUtil
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import git4idea.GitUtil
import git4idea.push.GitPushSupport


class RepositoryService {

    private val requestDoneHandler = mutableListOf<UiUpdateInterface>()

    fun onUpdateHandler(handler: UiUpdateInterface) {
        requestDoneHandler.add(handler)
    }

    fun getRepositoriesForProject(): MutableSet<String> {
        val remoteUrls = mutableSetOf<String>()

        ProjectManager.getInstance().openProjects.forEach {
            val abstractVCSs = ProjectLevelVcsManager.getInstance(it).allActiveVcss
            val repositoryManager = GitUtil.getRepositoryManager(it)
            for (abstractVCS in abstractVCSs) {
                DvcsUtil.getPushSupport(abstractVCS) as GitPushSupport? ?: continue
                for (repository in repositoryManager.repositories) {
                    for (remote in repository.remotes) for (pushUrl in remote.pushUrls) {
                        remoteUrls.add(pushUrl)
                    }
                }
            }
        }

        return remoteUrls
    }

    fun getDiff(url: String, username: String, password: String) {
        Fuel.get(url)
            .authentication()
            .basic(username, password)
            .responseString { request, response, result ->
                result.success {
                    val parser: Parser = Parser.default()
                    val responseObject = parser.parse(StringBuilder(it)) as JsonObject
                    requestDoneHandler.forEach {
                        it.updateUi(responseObject)
                    }
                }
                result.failure {
                    requestDoneHandler.forEach {
                        it.updateUi(JsonObject())
                    }
                }
            }
    }

    fun getPullRequest(url: String, id: String, username: String, password: String) {
        val match = Regex("(https|http).*?@(.*?)/(.*?)/(.*?)/?$").find(url) ?: return
        val (protocol, uri, workspace, repository) = match.destructured

        val finalUri = "$protocol://$uri/!api/2.0/repositories/$workspace/$repository/pullrequests/$id"
        Fuel.get(finalUri)
            .authentication()
            .basic(username, password)
            .responseString { request, response, result ->
                result.success {
                    val parser: Parser = Parser.default()
                    val responseObject = parser.parse(StringBuilder(it)) as JsonObject
                    requestDoneHandler.forEach {
                        it.updateUi(responseObject)
                    }
                }
                result.failure {
                    requestDoneHandler.forEach {
                        it.updateUi(JsonObject())
                    }
                }
            }
    }

    fun getPullRequests(url: String, state: String, username: String, password: String) {
        val match = Regex("(https|http).*?@(.*?)/(.*?)/(.*?)/?$").find(url) ?: return
        val (protocol, uri, workspace, repository) = match.destructured

        val finalUri = "$protocol://$uri/!api/2.0/repositories/$workspace/$repository/pullrequests?q=state=\"$state\""

        Fuel.get(finalUri)
            .authentication()
            .basic(username, password)
            .responseString { request, response, result ->
                result.success {
                    val parser: Parser = Parser.default()
                    val responseObject = parser.parse(StringBuilder(it)) as JsonObject
                    requestDoneHandler.forEach {
                        it.updateUi(responseObject)
                    }
                }
                result.failure {
                    requestDoneHandler.forEach {
                        it.updateUi(JsonObject())
                    }
                }
            }
    }
}