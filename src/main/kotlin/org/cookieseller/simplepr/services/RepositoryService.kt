package org.cookieseller.simplepr.services

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.intellij.dvcs.DvcsUtil
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import git4idea.GitUtil
import git4idea.push.GitPushSupport


class RepositoryService {

    fun getRepositoriesForProject(): MutableSet<String>
    {
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

    fun getPullRequests(url: String, username: String, password: String) {
        val match = Regex("(https|http).*?@(.*?)/(.*?)/(.*?)/?$").find(url) ?: return
        val (protocol, uri, workspace, repository) = match.destructured

        val finalUri = listOf(protocol.plus("://").plus(uri), "!api/2.0/repositories", workspace, repository, "pullrequests").joinToString(separator = "/")
        val repositorySlugFinder = listOf(protocol.plus("://").plus(uri), "!api/2.0/repositories", workspace, repository).joinToString(separator = "/")

        Fuel.get(finalUri)
            .authentication()
            .basic(username, password)
            .response { request, response, result ->
                println(request)
                println(response)
                println(result)
            }
    }
}