package com.iiankehn.slate.io

import com.iiankehn.slate.model.RichTextDocument
import com.iiankehn.slate.model.RichTextRange
import com.iiankehn.slate.model.RichTextStyle
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class SlxCodecTest {
    @Test fun roundTripPreservesRichTextAndAssets() {
        val asset = SlxAsset("hero", "png", "image/png", byteArrayOf(1, 2, 3, 4))
        val source = SlxDocument(
            title = "Shared draft",
            body = RichTextDocument("Hello Slate", listOf(
                RichTextRange(RichTextStyle.Bold, 0, 5),
                RichTextRange(RichTextStyle.Link, 6, 11, "https://slate.iiankehn.com"),
            )),
            sourceDocumentId = "notes-1",
            sourceRevision = 42,
            assets = listOf(asset),
        )

        val decoded = SlxCodec.decode(SlxCodec.encode(source))

        assertEquals(source.title, decoded.title)
        assertEquals(source.body.normalized(), decoded.body)
        assertEquals(42, decoded.sourceRevision)
        assertEquals("notes", decoded.sourceProduct)
        assertEquals("image/png", decoded.assets.single().mimeType)
        assertArrayEquals(asset.bytes, decoded.assets.single().bytes)
    }
}
