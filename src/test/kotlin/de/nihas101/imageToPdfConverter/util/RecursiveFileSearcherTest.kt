package de.nihas101.imageToPdfConverter.util

import de.nihas101.imageToPdfConverter.util.RecursiveFileSearcher.RecursiveFileSearcherFactory.createRecursiveFileSearcher
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class RecursiveFileSearcherTest {
    @Test
    fun getFilesInCurrentDepth() {
        val recursiveFileSearcher = createRecursiveFileSearcher(File("src/test/resources"))

        assertArrayEquals(File("src/test/resources").listFiles(), recursiveFileSearcher.getFilesInCurrentDepth().toTypedArray())
    }


    @Test
    fun searchRecursivelyDirectories() {
        val recursiveFileSearcher = createRecursiveFileSearcher(File("src/test"))

        val foundDirectories = recursiveFileSearcher.searchRecursively(1, TrivialProgressUpdater()) { file -> file.isDirectory }.toTypedArray()
        val expectedDirectories = File("src/test").listFiles().filter { file -> file.isDirectory }.toTypedArray()

        assertArrayEquals(expectedDirectories, foundDirectories)
    }

    @Test
    fun searchRecursivelyKt() {
        val recursiveFileSearcher = createRecursiveFileSearcher(File("src/test/java/de/nihas101/imageToPdfConverter/gui/controller"))

        val foundDirectories = recursiveFileSearcher.searchRecursively(1, TrivialProgressUpdater()) { file -> file.extension == "Kt" }.toTypedArray()
        val expectedDirectories = File("src/test/java/de/nihas101/imageToPdfConverter/gui/controller").listFiles().filter { file -> file.extension == "png" }.toTypedArray()

        assertArrayEquals(expectedDirectories, foundDirectories)
    }

    @Test
    fun searchRecursivelyDepth() {
        val recursiveFileSearcher = createRecursiveFileSearcher(File("src/test/java"))

        val foundDirectories = recursiveFileSearcher.searchRecursively(10, TrivialProgressUpdater()) { file -> file.name == "gui" }.toTypedArray()

        assertEquals(1, foundDirectories.size)
    }

    @Test
    fun searchRecursivelyPNGNotEnoughDepth() {
        val recursiveFileSearcher = createRecursiveFileSearcher(File("src/test"))

        val foundDirectories = recursiveFileSearcher.searchRecursively(1, TrivialProgressUpdater()) { file -> file.extension == "png" }.toTypedArray()

        assertEquals(0, foundDirectories.size)
    }

    @Test
    fun searchRecursivelyNonPositiveSearchDepth() {
        val recursiveFileSearcher = createRecursiveFileSearcher(File("src/test"))

        try {
            recursiveFileSearcher.searchRecursively(-100, TrivialProgressUpdater()) { file -> file.extension == "png" }
        } catch (exception: NonPositiveMaxSearchDepth) {
            return
        }

        fail("The expected Exception was not thrown")
    }

    @Test
    fun searchRecursivelyNonExistentFile() {
        try {
            createRecursiveFileSearcher(File("src/test/nonExistent"))
        } catch (exception: NoSuchFileException) {
            return
        }

        fail("The expected Exception was not thrown")
    }
}