package de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import org.junit.Test

import java.io.File

class IteratorRemoveActionTest {

    @Test
    fun execute() {
        val directoryIterator = DirectoryIterator.createDirectoryIterator(
                File("src/test/resources/images"), IteratorOptions()
        )
        val iteratorAction = IteratorAction.createIteratorAction(listOf("r", "0", "3"), ImageToPdfOptions.createOptions())

        iteratorAction.execute(directoryIterator)

        assertEquals("2.png", directoryIterator.getFile(0).name)
        assertEquals("3.png", directoryIterator.getFile(1).name)

        try {
            directoryIterator.getFile(2)
        } catch (exception: IndexOutOfBoundsException) {
            return
        }

        fail("directoryIterator holds more than 2 files")
    }

    @Test
    fun executeFullName() {
        val directoryIterator = DirectoryIterator.createDirectoryIterator(
                File("src/test/resources/images"), IteratorOptions()
        )
        val iteratorAction = IteratorAction.createIteratorAction(listOf("remove", "0", "3"), ImageToPdfOptions.createOptions())

        iteratorAction.execute(directoryIterator)

        assertEquals("2.png", directoryIterator.getFile(0).name)
        assertEquals("3.png", directoryIterator.getFile(1).name)

        try {
            directoryIterator.getFile(2)
        } catch (exception: IndexOutOfBoundsException) {
            return
        }

        fail("directoryIterator holds more than 2 files")
    }

    @Test
    fun toStringTest() {
        val iteratorAction = IteratorAction.createIteratorAction(listOf("remove", "0", "3", "4"), ImageToPdfOptions.createOptions())

        assertEquals("remove [0, 3, 4]", iteratorAction.toString())
    }
}