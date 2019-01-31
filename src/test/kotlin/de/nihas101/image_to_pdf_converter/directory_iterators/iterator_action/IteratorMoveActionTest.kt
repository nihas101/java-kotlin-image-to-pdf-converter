package de.nihas101.image_to_pdf_converter.directory_iterators.iterator_action

import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator
import de.nihas101.image_to_pdf_converter.directory_iterators.iterator_action.IteratorAction.PdfModificationFactory.createIteratorAction
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.ImageToPdfOptions
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.IteratorOptions
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class IteratorMoveActionTest {
    @Test
    fun execute() {
        val directoryIterator = DirectoryIterator.createDirectoryIterator(
                File("src/test/resources/images"), IteratorOptions()
        )
        directoryIterator.getFileList().sort()

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
        val directoryIterator = DirectoryIterator.createDirectoryIterator(
                File("src/test/resources/images"), IteratorOptions()
        )
        directoryIterator.getFileList().sort()
        val iteratorAction = createIteratorAction(
                listOf("move", "0", "1"),
                imageToPdfOptions = ImageToPdfOptions.createOptions()
        )

        iteratorAction.execute(directoryIterator)

        assertEquals("2.png", directoryIterator.getFile(0).name)
        assertEquals("1.jpg", directoryIterator.getFile(1).name)
    }

    @Test
    fun toStringTest() {
        val iteratorAction = createIteratorAction(
                listOf("move", "0", "1"),
                imageToPdfOptions = ImageToPdfOptions.createOptions()
        )

        assertEquals("move <0> <1>", iteratorAction.toString())
    }
}