package de.nihas101.image_to_pdf_converter.directory_iterators.image_iterators

import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator
import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator
import de.nihas101.image_to_pdf_converter.directory_iterators.exceptions.NoMoreFilesException
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.IteratorOptions
import junit.framework.TestCase.*
import org.junit.BeforeClass
import org.junit.Test
import java.io.File

class ImageFilesIteratorTest {
    companion object {
        val files = mutableListOf(
                File("src/test/resources/images/1.jpg"),
                File("src/test/resources/images/2.png"),
                File("src/test/resources/images/3.png"),
                File("src/test/resources/images/の.png")
        )

        @BeforeClass
        @JvmStatic
        fun sortFiles() {
            files.sort()
        }
    }

    @Test
    fun getFile() {
        val imageFilesIterator = createTestIterator()

        assertEquals(files[0], imageFilesIterator.getFile(0))
        assertEquals(files[1], imageFilesIterator.getFile(1))
        assertEquals(files[2], imageFilesIterator.getFile(2))
        assertEquals(files[3], imageFilesIterator.getFile(3))
    }

    @Test
    fun getFiles() {
        val imageFilesIterator = createTestIterator()

        assertEquals(files, imageFilesIterator.getFileList())
    }

    @Test
    fun addImage() {
        val imageFilesIterator = createTestIterator()

        assertTrue(imageFilesIterator.add(files[0]))
        assertEquals(files[0], imageFilesIterator.getFile(4))
    }

    @Test
    fun addDirectory() {
        val imageFilesIterator = createTestIterator()
        val before = imageFilesIterator.numberOfFiles()
        val directory = File("src/test/resources/images")

        assertTrue(imageFilesIterator.add(directory))
        assertEquals(before + 4, imageFilesIterator.numberOfFiles())
    }

    @Test
    fun addZip() {
        val imageFilesIterator = createTestIterator()
        val before = imageFilesIterator.numberOfFiles()

        val zip = File("src/test/resources/zip/images.zip")

        assertTrue(imageFilesIterator.add(zip))
        assertEquals(before + 4, imageFilesIterator.numberOfFiles())
    }

    @Test
    fun addTheUnaddable() {
        val imageFilesIterator = createTestIterator()

        val zip = File("src/test/kotlin/de/nihas101/image_to_pdf_converter/directory_iterators/image_iterators/ImageFilesIteratorTest.kt")

        assertFalse(imageFilesIterator.add(zip))
    }

    @Test
    fun addIndex() {
        val imageFilesIterator = createTestIterator()

        imageFilesIterator.add(2, files[0])

        assertEquals(files[0], imageFilesIterator.getFile(2))
    }

    @Test
    fun addAll() {
        val imageFilesIterator = createTestIterator()

        imageFilesIterator.addAll(files)

        assertEquals(files, imageFilesIterator.getFileList().subList(4, 8))
    }

    @Test
    fun addAllZip() {
        val imageFilesIterator = createTestIterator()
        val zipFiles = File("src/test/resources/zip").listFiles().toList()
        val before = imageFilesIterator.numberOfFiles()

        imageFilesIterator.addAll(zipFiles)

        assertEquals(before + 12, imageFilesIterator.numberOfFiles())
    }

    @Test
    fun remove() {
        val imageFilesIterator = createTestIterator()

        imageFilesIterator.remove(files[0])

        assertEquals(files.subList(1, 4), imageFilesIterator.getFileList())
    }

    @Test
    fun removeFalse() {
        val directoriesIterator = createTestIterator()

        assertEquals(false, directoriesIterator.remove(File("")))

        assertEquals(4, directoriesIterator.numberOfFiles())
    }

    @Test
    fun nextFile() {
        val imageFilesIterator = createTestIterator()

        val files0 = mutableListOf<File?>(
                imageFilesIterator.nextFile(),
                imageFilesIterator.nextFile(),
                imageFilesIterator.nextFile(),
                imageFilesIterator.nextFile()
        )

        assertEquals(files, files0)

        try {
            imageFilesIterator.nextFile()
        } catch (exception: NoMoreFilesException) {
            return
        }

        fail("NoMoreImagesException was not thrown")
    }

    @Test
    fun resetIndex() {
        val imageFilesIterator = createTestIterator()

        val files0 = mutableListOf<File?>(
                imageFilesIterator.nextFile(),
                imageFilesIterator.nextFile(),
                imageFilesIterator.nextFile(),
                imageFilesIterator.nextFile()
        )

        assertEquals(files, files0)

        imageFilesIterator.resetIndex()

        try {
            imageFilesIterator.nextFile()
        } catch (exception: NoMoreFilesException) {
            fail("NoMoreImagesException was thrown")
        }
    }

    @Test
    fun numberOfFiles() {
        assertEquals(4, createTestIterator().numberOfFiles())
    }

    @Test
    fun getParentDirectory() {
        assertEquals(files[0].parentFile, createTestIterator().getParentDirectory())
    }

    @Test
    fun clearTest() {
        val directoriesIterator = createTestIterator()

        directoriesIterator.clear()

        assertEquals(0, directoriesIterator.numberOfFiles())
    }

    @Test
    fun noImageFile() {
        val imageFilesIterator = createTestIterator()

        assertEquals(false, imageFilesIterator.add(File("src/test/kotlin/de/nihas101/image_to_pdf_converter/directory_iterators/image_iterators")))
    }


    private fun createTestIterator(): DirectoryIterator {
        val testIterator = createDirectoryIterator(File("src/test/resources/images"), IteratorOptions())
        testIterator.getFileList().sort()
        return testIterator
    }
}