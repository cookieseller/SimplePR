package org.cookieseller.simplepr.services

import com.intellij.openapi.diff.impl.patch.FilePatch
import com.intellij.openapi.diff.impl.patch.PatchReader

class RetrievePRChangesService {
    public fun readAllPatches(diffFile: String): List<FilePatch> {
        val reader = PatchReader(diffFile, true)
        reader.parseAllPatches()
        return reader.allPatches
    }
}