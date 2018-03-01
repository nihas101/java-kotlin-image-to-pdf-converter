package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.ExtensionNotSupportedException
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ImageUnZipper.ZipFileIteratorFactory.createImageUnZipper
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import org.junit.Test
import java.io.File

class ImageUnZipperTest {

    @Test
    fun unzip() {
        val unzipInto = File("src/test/RESOURCES/zip/images")
        unzipInto.mkdir()
        unzipInto.deleteOnExit()

        createImageUnZipper(File("src/test/RESOURCES/zip/images.zip")).unzip(unzipInto, true)

        assertEquals(4, unzipInto.listFiles().size)
    }

    @Test
    fun notSupportedException() {
        val unzipInto = File("src/test/RESOURCES/zip/images")
        unzipInto.mkdir()
        unzipInto.deleteOnExit()

        try {
            createImageUnZipper(File("src/test/RESOURCES/images/1.jpg")).unzip(unzipInto, true)
        } catch (exception: ExtensionNotSupportedException) {
            return
        }

        fail("ExtensionNotSupportedException was not thrown")
    }
}