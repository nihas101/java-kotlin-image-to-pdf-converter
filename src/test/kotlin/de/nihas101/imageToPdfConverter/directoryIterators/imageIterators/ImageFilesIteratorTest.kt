package de.nihas101.imageToPdfConverter.directoryIterators.imageIterators

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreImagesException
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import org.junit.Test
import java.io.File

class ImageFilesIteratorTest {
    private val files = mutableListOf(
            File("src/test/resources/images/1.jpg"),
            File("src/test/resources/images/2.png"),
            File("src/test/resources/images/3.png"),
            File("src/test/resources/images/の.png")
    )

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

        assertEquals(files, imageFilesIterator.getFiles())
    }

    @Test
    fun add() {
        val imageFilesIterator = createTestIterator()

        imageFilesIterator.add(files[0])

        assertEquals(files[0], imageFilesIterator.getFile(4))
    }

    @Test
    fun addAll() {
        val imageFilesIterator = createTestIterator()

        imageFilesIterator.addAll(files)

        assertEquals(files, imageFilesIterator.getFiles().subList(4, 8))
    }

    @Test
    fun remove() {
        val imageFilesIterator = createTestIterator()

        imageFilesIterator.remove(files[0])

        assertEquals(files.subList(1, 4), imageFilesIterator.getFiles())
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
        } catch (exception: NoMoreImagesException) {
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
        } catch (exception: NoMoreImagesException) {
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


    private fun createTestIterator(): DirectoryIterator {
        return DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator(
                File("src/test/resources/images"), IteratorOptions()
        )
    }
}