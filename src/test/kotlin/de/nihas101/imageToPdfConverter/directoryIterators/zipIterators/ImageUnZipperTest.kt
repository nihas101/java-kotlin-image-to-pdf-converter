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
        val unzipInto = File("src/test/resources/zip/images")
        unzipInto.mkdir()
        unzipInto.deleteOnExit()

        createImageUnZipper(File("src/test/resources/zip/images.zip")).unzip(unzipInto, deleteOnExit = true)

        assertEquals(4, unzipInto.listFiles().size)
    }

    @Test
    fun unzipWithTrailingSpace() {
        val unzipInto = File("src/test/resources/zip/imagesTrailing")
        unzipInto.mkdir()
        unzipInto.deleteOnExit()

        createImageUnZipper(File("src/test/resources/zip/imagesTrailing .zip")).unzip(unzipInto, deleteOnExit = true)

        assertEquals(4, unzipInto.listFiles().size)
    }

    @Test
    fun notSupportedException() {
        val unzipInto = File("src/test/resources/zip/images")
        unzipInto.mkdir()
        unzipInto.deleteOnExit()

        try {
            createImageUnZipper(File("src/test/resources/images/1.jpg")).unzip(unzipInto, deleteOnExit = true)
        } catch (exception: ExtensionNotSupportedException) {
            return
        }

        fail("ExtensionNotSupportedException was not thrown")
    }
}