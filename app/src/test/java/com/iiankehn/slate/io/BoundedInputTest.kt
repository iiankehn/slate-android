package com.iiankehn.slate.io

import java.io.ByteArrayInputStream
import java.io.InputStream
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class BoundedInputTest {
    @Test fun exactLimitAndEmptyFilesAreAccepted() {
        assertArrayEquals(byteArrayOf(1, 2, 3), readBoundedInput(ByteArrayInputStream(byteArrayOf(1, 2, 3)), 3))
        assertArrayEquals(byteArrayOf(), readBoundedInput(ByteArrayInputStream(byteArrayOf()), 0))
    }

    @Test fun endlessProviderIsStoppedAfterOneBytePastTheLimit() {
        var consumed = 0
        val input = object : InputStream() {
            override fun read(): Int { consumed++; return 1 }
        }
        assertThrows(IllegalArgumentException::class.java) { readBoundedInput(input, 10) }
        assertEquals(11, consumed)
    }

    @Test fun streamReturningZeroStillMakesProgress() {
        val input = object : ByteArrayInputStream(byteArrayOf(5, 6)) {
            override fun read(bytes: ByteArray, offset: Int, length: Int): Int = 0
        }
        assertArrayEquals(byteArrayOf(5, 6), readBoundedInput(input, 2))
    }
}
