package org.cookieseller.simplepr.services

import com.intellij.dvcs.DvcsUtil
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import git4idea.GitUtil
import git4idea.push.GitPushSupport
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.auth.BasicScheme
import org.apache.http.impl.client.DefaultHttpClient
import java.net.http.HttpClient
import java.util.*


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

    fun authorize() {
        val finalUri = "https://bitbucket.org/site/oauth2/authorize?client_id="
            .plus("W4tc62sawzm2uw4fVT")
            .plus("&response_type=code")
            .plus("&state=")
            .plus(UUID.randomUUID().toString())

        val client: HttpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build()
    }

    fun getPullRequests(url: String, username: String, password: String) {
        val match = Regex("(https|http).*?@(.*?)/(.*?)/(.*?)/?$").find(url) ?: return
        val (protocol, uri, workspace, repository) = match.destructured
///2.0/repositories/{workspace}
        val finalUri = listOf(protocol.plus("://").plus(uri), "!api/2.0/repositories", workspace, repository, "pullrequests").joinToString(separator = "/")
        val repositorySlugFinder = listOf(protocol.plus("://").plus(uri), "!api/2.0/repositories", workspace, repository).joinToString(separator = "/")

        val client: HttpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build()

        val httpClient: DefaultHttpClient = DefaultHttpClient()
        val httpGet = HttpGet(repositorySlugFinder)
        httpGet.addHeader(
            BasicScheme.authenticate(
                UsernamePasswordCredentials("Cookieseller", "ZjPfhYFtQgdYYKCcLv3M"),
                "UTF-8", false
            )
        )
//        val httpResponse = httpClient.execute(httpGet)
        // Base64.getEncoder().encodeToString(usernameColonPassword.getBytes())
//        val request2 = HttpRequest.newBuilder()
//            .uri(URI.create(repositorySlugFinder))
//            .header("Authorization", basicAuth(username, password))
//            .header("Content-Type", "application/json")
//            .build()
//        val response2 = client.send(request2, HttpResponse.BodyHandlers.ofString())
//        print(response2)

        // Base64.getEncoder().encodeToString(usernameColonPassword.getBytes())
//        val request = HttpRequest.newBuilder()
//            .uri(URI.create("https://bitbucket.org/Superleroy/simpleprbitbucket/pull-requests/"))
//            .uri(URI.create(finalUri))
//            .header("Authorization", basicAuth(username, password))
//            .header("Content-Type", "application/json")
//            .build()
//        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
//
//        print(response)
    }
}