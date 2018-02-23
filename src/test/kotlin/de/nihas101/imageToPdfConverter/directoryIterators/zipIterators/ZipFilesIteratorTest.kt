package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreDirectoriesException
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ZipFilesIterator.ZipFilesIteratorFactory.createZipFilesIterator
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.io.File

class ZipFilesIteratorTest {
    @Test
    fun nextFile() {
        val zipFilesIterator = createTestIterator()

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

        assertEquals("images", file.name)
    }

    @Test
    fun getFiles() {
        val zipFilesIterator = createTestIterator()

        val files = zipFilesIterator.getFiles()

        assertEquals("images", files[0].name)
    }

    @Test
    fun remove() {
        val zipFilesIterator = createTestIterator()

        zipFilesIterator.remove(File("src/test/resources/zip/images"))

        assertEquals(0, zipFilesIterator.numberOfFiles())
    }

    @Test
    fun add() {
        val zipFilesIterator = createTestIterator()

        zipFilesIterator.add(File("src/test/resources/images"))

        assertEquals(2, zipFilesIterator.numberOfFiles())
    }

    @Test
    fun addAll() {
        val zipFilesIterator = createTestIterator()

        zipFilesIterator.addAll(mutableListOf(File("src/test/resources/images")))

        assertEquals(2, zipFilesIterator.numberOfFiles())
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

    private fun createTestIterator() = createZipFilesIterator(File("src/test/resources/zip"), true)
}