package de.nihas101.image_to_pdf_converter.directory_iterators.zip_iterators

import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator
import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator
import de.nihas101.image_to_pdf_converter.directory_iterators.exceptions.NoMoreFilesException
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.IteratorOptions
import junit.framework.TestCase
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.io.File

class ZipFilesIteratorTest {
    @Test
    fun nextFile() {
        val zipFilesIterator = createTestIterator(true)

        for (tries in 0 until 10) {
            try {
                zipFilesIterator.nextFile()
            } catch (exception: NoMoreFilesException) {
                return
            }
        }

        fail("NoMoreFilesException was not thrown")
    }

    @Test
    fun getFile() {
        val zipFilesIterator = createTestIterator(true)

        val file = zipFilesIterator.getFile(0)

        assertEquals(true,
                "images" == file.name || // windows
                        "imagesTrailing" == file.name   // linux
        )
    }

    @Test
    fun getFiles() {
        val zipFilesIterator = createTestIterator(true)

        val file = zipFilesIterator.getFile(0)

        assertEquals(true,
                "images" == file.name || // windows
                        "imagesTrailing" == file.name // linux
        )
    }

    @Test
    fun remove() {
        val zipFilesIterator = createTestIterator(true)
        val before = zipFilesIterator.numberOfFiles()

        val removed = zipFilesIterator.remove(File("src/test/resources/zip/images"))

        assertEquals(true, removed)
        assertEquals(before - 1, zipFilesIterator.numberOfFiles())
    }

    @Test
    fun add() {
        val zipFilesIterator = createTestIterator(true)
        val before = zipFilesIterator.numberOfFiles()

        zipFilesIterator.add(File("src/test/resources/images"))

        assertEquals(before + 1, zipFilesIterator.numberOfFiles())
    }

    @Test
    fun addIndex() {
        val zipFilesIterator = createTestIterator(true)

        zipFilesIterator.add(0, File("src/test/resources/images"))

        assertEquals("images", zipFilesIterator.getFile(0).name)
    }

    @Test
    fun addAll() {
        val zipFilesIterator = createTestIterator(true)
        val before = zipFilesIterator.numberOfFiles()

        zipFilesIterator.addAll(mutableListOf(File("src/test/resources/images")))

        assertEquals(before + 1, zipFilesIterator.numberOfFiles())
    }

    @Test
    fun getParentDirectory() {
        val zipFilesIterator = createTestIterator(true)

        assertEquals("zip", zipFilesIterator.getParentDirectory().name)
    }

    @Test
    fun resetIndex() {
        val zipFilesIterator = createTestIterator(true)

        zipFilesIterator.nextFile()

        zipFilesIterator.resetIndex()

        try {
            zipFilesIterator.nextFile()
        } catch (exception: NoMoreFilesException) {
            fail("NoMoreFilesException was not thrown")
        }
    }

    @Test
    fun clearTest() {
        val directoriesIterator = createTestIterator(true)

        directoriesIterator.clear()

        TestCase.assertEquals(0, directoriesIterator.numberOfFiles())
    }

    private fun createTestIterator(includeZipFiles: Boolean = false): DirectoryIterator {
        return createDirectoryIterator(
                File("src/test/resources/zip"),
                IteratorOptions(true, includeZipFiles, true)
        )
    }
}