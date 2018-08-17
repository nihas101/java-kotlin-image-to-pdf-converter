package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreDirectoriesException
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import junit.framework.TestCase
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.io.File

class ZipFilesIteratorTest {
    @Test
    fun nextFile() {
        val zipFilesIterator = createTestIterator()

        zipFilesIterator.nextFile()
        zipFilesIterator.nextFile()
        zipFilesIterator.nextFile()

        try {
            zipFilesIterator.nextFile()
        } catch (exception: NoMoreDirectoriesException) {
            return
        }

        fail("NoMoreDirectoriesException was not thrown")
    }

    @Test
    fun getFile() {
        val zipFilesIterator = createTestIterator()

        val file = zipFilesIterator.getFile(0)


        assertEquals(true,
                "images" == file.name || // windows
                        "imagesTrailing" == file.name   // linux
        )
    }

    @Test
    fun getFiles() {
        val zipFilesIterator = createTestIterator()

        val files = zipFilesIterator.getFiles()

        assertEquals(true,
                "images" == files[0].name || // windows
                        "imagesTrailing" == files[0].name // linux
        )
    }

    @Test
    fun remove() {
        val zipFilesIterator = createTestIterator()

        val removed = zipFilesIterator.remove(File("src/test/resources/zip/images"))

        assertEquals(true, removed)
        assertEquals(2, zipFilesIterator.numberOfFiles())
    }

    @Test
    fun add() {
        val zipFilesIterator = createTestIterator()

        zipFilesIterator.add(File("src/test/resources/images"))

        assertEquals(4, zipFilesIterator.numberOfFiles())
    }

    @Test
    fun addIndex() {
        val zipFilesIterator = createTestIterator()

        zipFilesIterator.add(0, File("src/test/resources/images"))

        assertEquals("images", zipFilesIterator.getFile(0).name)
    }

    @Test
    fun addAll() {
        val zipFilesIterator = createTestIterator()

        zipFilesIterator.addAll(mutableListOf(File("src/test/resources/images")))

        assertEquals(4, zipFilesIterator.numberOfFiles())
    }

    @Test
    fun getParentDirectory() {
        val zipFilesIterator = createTestIterator()

        assertEquals("zip", zipFilesIterator.getParentDirectory().name)
    }

    @Test
    fun resetIndex() {
        val zipFilesIterator = createTestIterator()

        zipFilesIterator.nextFile()

        zipFilesIterator.resetIndex()

        try {
            zipFilesIterator.nextFile()
        } catch (exception: NoMoreDirectoriesException) {
            fail("NoMoreDirectoriesException was not thrown")
        }
    }

    @Test
    fun clearTest() {
        val directoriesIterator = createTestIterator()

        directoriesIterator.clear()

        TestCase.assertEquals(0, directoriesIterator.numberOfFiles())
    }

    private fun createTestIterator(): DirectoryIterator {
        return createDirectoryIterator(
                File("src/test/resources/zip"),
                IteratorOptions(true, true, true)
        )
    }
}