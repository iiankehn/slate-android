package com.iiankehn.slate.io

import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class DocxPackageSafetyTest {
    @Test fun unknownOversizedPartsAreBoundedBeforeXmlParsing() {
        val bytes = archive(listOf("unused.bin" to 26 * 1024 * 1024))
        val error = assertThrows(IllegalArgumentException::class.java) { DocumentFormats.importDocx(bytes, "Test") }
        assertTrue(error.message.orEmpty().contains("size limit"))
    }

    @Test fun aggregateExpandedBytesAreBoundedAcrossParts() {
        val bytes = archive((1..3).map { "unused-$it.bin" to 24 * 1024 * 1024 })
        val error = assertThrows(IllegalArgumentException::class.java) { DocumentFormats.importDocx(bytes, "Test") }
        assertTrue(error.message.orEmpty().contains("size limit"))
    }

    @Test fun parentTraversalPathsAreRejected() {
        val error = assertThrows(IllegalArgumentException::class.java) {
            DocumentFormats.importDocx(archive(listOf("word/../document.xml" to 0)), "Test")
        }
        assertTrue(error.message.orEmpty().contains("unsafe"))
    }

    private fun archive(parts: List<Pair<String, Int>>): ByteArray {
        val output = ByteArrayOutputStream()
        val zeros = ByteArray(8192)
        ZipOutputStream(output).use { zip ->
            parts.forEach { (name, size) ->
                zip.putNextEntry(ZipEntry(name))
                var remaining = size
                while (remaining > 0) {
                    val count = minOf(remaining, zeros.size)
                    zip.write(zeros, 0, count)
                    remaining -= count
                }
                zip.closeEntry()
            }
        }
        return output.toByteArray()
    }
}
