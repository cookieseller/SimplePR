package org.cookieseller.simplepr.services

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.credentialStore.isFulfilled
import com.intellij.ide.passwordSafe.PasswordSafe


class CredentialStorageService {

    private val SERVICE_NAME: String = "cookieseller.simplepr"

    fun storeCredentials(key: String, username: String, password: CharArray?)
    {
        val credentialAttributes = createCredentialAttributes(key) ?: return
        PasswordSafe.instance.set(credentialAttributes, Credentials(username, password))
    }

    fun removeCredentials(key: String)
    {
        val credentialAttributes = createCredentialAttributes(key) ?: return
        PasswordSafe.instance.set(credentialAttributes, null)
    }

    fun hasCredentials(key: String): Boolean
    {
        val credentialAttributes = createCredentialAttributes(key) ?: return false
        val password = PasswordSafe.instance.getPassword(credentialAttributes)
        val credentials = PasswordSafe.instance.get(credentialAttributes) ?: return false

        return credentials.isFulfilled()
    }

    fun getUsername(key: String): String?
    {
        val credentialAttributes = createCredentialAttributes(key) ?: return null
        val credentials = PasswordSafe.instance.get(credentialAttributes) ?: return null

        return credentials.userName
    }

    fun getPassword(key: String): String?
    {
        val credentialAttributes = createCredentialAttributes(key) ?: return null
        val credentials = PasswordSafe.instance.get(credentialAttributes) ?: return null

        return credentials.getPasswordAsString()
    }

    private fun createCredentialAttributes(key: String): CredentialAttributes? {
        return CredentialAttributes(generateServiceName(SERVICE_NAME, key))
    }
}