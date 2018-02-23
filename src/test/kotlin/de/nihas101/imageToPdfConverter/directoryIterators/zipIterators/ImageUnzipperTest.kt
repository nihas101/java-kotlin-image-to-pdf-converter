package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.File

class ImageUnzipperTest {

    @Test
    fun unzip() {
        val unzipInto = File("src/test/resources/zip/images")
        unzipInto.mkdir()
        unzipInto.deleteOnExit()

        ImageUnzipper.createImageUnzipper(File("src/test/resources/zip/images.zip")).unzip(unzipInto, true)

        assertEquals(4, unzipInto.listFiles().size)
    }
}