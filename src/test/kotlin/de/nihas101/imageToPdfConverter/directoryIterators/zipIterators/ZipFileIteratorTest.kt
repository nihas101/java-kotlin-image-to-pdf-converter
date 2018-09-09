package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreFilesException
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import org.junit.Test
import java.io.File

class ZipFileIteratorTest {
    @Test
    fun nextFile() {
        val zipFileIterator = createTestIterator()

        val files0 = mutableListOf<File?>(
                zipFileIterator.nextFile(),
                zipFileIterator.nextFile()
        )

        assertEquals("1.jpg", files0[0]!!.name)
        assertEquals("2.png", files0[1]!!.name)
    }

    @Test
    fun getFiles() {
        val zipFileIterator = createTestIterator()
        assertEquals("[1.jpg, 2.png, 3.png, の.png]",
                zipFileIterator.getFileList().map { file -> file.name }.toString())
    }

    @Test
    fun remove() {
        val zipFileIterator = createTestIterator()
        zipFileIterator.remove(File("src/test/resources/zip/images/の.png"))
        assertEquals(3, zipFileIterator.numberOfFiles())
    }

    @Test
    fun add() {
        val zipFileIterator = createTestIterator()
        zipFileIterator.add(File("src/test/resources/images/1.jpg"))
        assertEquals("1.jpg", zipFileIterator.getFile(4).name)
    }

    @Test
    fun addIndex() {
        val zipFileIterator = createTestIterator()
        zipFileIterator.add(0, File("src/test/resources/images/1.jpg"))
        assertEquals("1.jpg", zipFileIterator.getFile(0).name)
    }

    @Test
    fun addAll() {
        val zipFileIterator = createTestIterator()
        zipFileIterator.addAll(
                listOf(
                        File("src/test/resources/images/1.jpg"),
                        File("src/test/resources/images/2.png")
                )
        )
        assertEquals(6, zipFileIterator.numberOfFiles())
    }

    @Test
    fun numberOfFiles() {
        assertEquals(4, createTestIterator().numberOfFiles())
    }

    @Test
    fun getParentDirectory() {
        assertEquals("images", createTestIterator().getParentDirectory().name)
    }

    @Test
    fun resetIndex() {
        val zipFileIterator = createTestIterator()

        zipFileIterator.nextFile()
        zipFileIterator.nextFile()
        zipFileIterator.nextFile()
        zipFileIterator.nextFile()

        zipFileIterator.resetIndex()

        try {
            zipFileIterator.nextFile()
        } catch (exception: NoMoreFilesException) {
            fail("NoMoreImagesException was thrown")
        }
    }

    @Test
    fun clearTest() {
        val directoriesIterator = createTestIterator()

        directoriesIterator.clear()

        assertEquals(0, directoriesIterator.numberOfFiles())
    }

    private fun createTestIterator(): DirectoryIterator {
        return createDirectoryIterator(
                File("src/test/resources/zip/images.zip"),
                IteratorOptions(includeZipFiles = true, deleteOnExit = true)
        )
    }
}