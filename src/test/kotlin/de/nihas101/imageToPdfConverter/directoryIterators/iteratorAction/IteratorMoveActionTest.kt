package de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction.IteratorAction.PdfModificationFactory.createIteratorAction
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class IteratorMoveActionTest {
    @Test
    fun execute() {
        val directoryIterator = DirectoryIterator.createDirectoryIterator(IteratorOptions())
        directoryIterator.setupDirectory(File("src/test/resources/images"))
        val iteratorAction = createIteratorAction(
                listOf("m", "0", "1"),
                imageToPdfOptions = ImageToPdfOptions.createOptions()
        )

        iteratorAction.execute(directoryIterator)

        assertEquals("2.png", directoryIterator.getFile(0).name)
        assertEquals("1.jpg", directoryIterator.getFile(1).name)
    }

    @Test
    fun executeFullName() {
        val directoryIterator = DirectoryIterator.createDirectoryIterator(IteratorOptions())
        directoryIterator.setupDirectory(File("src/test/resources/images"))
        val iteratorAction = createIteratorAction(
                listOf("move", "0", "1"),
                imageToPdfOptions = ImageToPdfOptions.createOptions()
        )

        iteratorAction.execute(directoryIterator)

        assertEquals("2.png", directoryIterator.getFile(0).name)
        assertEquals("1.jpg", directoryIterator.getFile(1).name)
    }
}