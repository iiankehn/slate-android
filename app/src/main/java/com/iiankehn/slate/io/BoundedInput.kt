package com.iiankehn.slate.io

import java.io.ByteArrayOutputStream
import java.io.InputStream

/** Stop oversized provider streams before allocating the entire file. Does not close the caller's stream. */
internal fun readBoundedInput(input: InputStream, maxBytes: Int): ByteArray {
    require(maxBytes >= 0)
    val output = ByteArrayOutputStream()
    val buffer = ByteArray(8192)
    while (true) {
        val count = input.read(buffer, 0, minOf(buffer.size.toLong(), maxBytes.toLong() - output.size() + 1).toInt())
        if (count < 0) return output.toByteArray()
        if (count == 0) {
            val next = input.read()
            if (next < 0) return output.toByteArray()
            require(output.size() < maxBytes) { "The selected file exceeds the import size limit." }
            output.write(next)
        } else {
            require(count.toLong() + output.size() <= maxBytes) { "The selected file exceeds the import size limit." }
            output.write(buffer, 0, count)
        }
    }
}
