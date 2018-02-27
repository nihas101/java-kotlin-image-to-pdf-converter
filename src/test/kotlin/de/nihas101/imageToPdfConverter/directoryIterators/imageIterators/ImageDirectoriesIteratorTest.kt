package de.nihas101.imageToPdfConverter.directoryIterators.imageIterators

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreDirectoriesException
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator.ImageDirectoriesIteratorFactory.createImageDirectoriesIterator
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import org.junit.Test
import java.io.File

class ImageDirectoriesIteratorTest {
    private val files = mutableListOf(File("src/test/resources/images"))

    @Test
    fun numberOfFiles() {
        assertEquals(1, createTestIterator().numberOfFiles())
    }

    @Test
    fun getParentDirectory() {
        assertEquals(files[0].parentFile, createTestIterator().getParentDirectory())
    }

    @Test
    fun getFiles() {
        assertEquals(files, createTestIterator().getFiles())
    }

    @Test
    fun add() {
        val directoriesIterator = createTestIterator()

        directoriesIterator.add(files[0])

        assertEquals(files[0], directoriesIterator.getFile(1))
    }

    @Test
    fun addAll() {
        val directoriesIterator = createTestIterator()

        directoriesIterator.addAll(files)

        assertEquals(files[0], directoriesIterator.getFile(1))
    }

    @Test
    fun remove() {
        val directoriesIterator = createTestIterator()

        directoriesIterator.remove(files[0])

        assertEquals(0, directoriesIterator.numberOfFiles())
    }

    @Test
    fun nextFile() {
        val directoriesIterator = createTestIterator()

        val file = directoriesIterator.nextFile()

        assertEquals(files[0], file)

        try {
            directoriesIterator.nextFile()
        } catch (exception: NoMoreDirectoriesException) {
            return
        }

        fail("NoMoreDirectoriesException was not thrown")
    }

    @Test
    fun resetIndex() {
        val directoriesIterator = createTestIterator()

        directoriesIterator.nextFile()
        directoriesIterator.resetIndex()

        try {
            directoriesIterator.nextFile()
        } catch (exception: NoMoreDirectoriesException) {
            fail("NoMoreDirectoriesException was thrown")
        }
    }

    @Test
    fun getFile() {
        val directoriesIterator = createTestIterator()

        assertEquals(files[0], directoriesIterator.getFile(0))
    }

    private fun createTestIterator(): DirectoryIterator {
        val directoryIterator = createImageDirectoriesIterator()
        directoryIterator.setupDirectory(File("src/test/resources"))
        return directoryIterator
    }
}