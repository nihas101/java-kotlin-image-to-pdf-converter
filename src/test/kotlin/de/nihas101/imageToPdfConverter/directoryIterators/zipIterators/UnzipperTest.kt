package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.ImagePdfTest.TestOutputStream
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.Unzipper.ZipFileIteratorFactory.createUnzipper
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.File

class UnzipperTest {
    @Test
    fun unzip() {
        val output = StringBuilder()
        val fileNameList = MutableList(0, { _ -> "" })
        val unzipper = createUnzipper(File("src/test/resources/test.zip"))

        unzipper.unzip(outputStreamFactoryFactory(output, fileNameList))

        assertEquals("test/", fileNameList[0])
        assertEquals("test/1.txt", fileNameList[1])
        assertEquals("test/2.txt", fileNameList[2])
        assertEquals("test/3.txt", fileNameList[3])
    }

    private fun outputStreamFactoryFactory(output: StringBuilder, fileNameList: MutableList<String>): (String) -> TestOutputStream {
        return { fileName ->
            fileNameList.add(fileName)
            TestOutputStream(output)
        }
    }
}