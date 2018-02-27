package de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.MalformedPdfActionException
import de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction.IteratorAction.PdfModificationFactory.createIteratorAction
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions
import junit.framework.TestCase.fail
import org.junit.Assert.assertEquals
import org.junit.Test

class IteratorBuildActionTest {

    @Test
    fun buildActionTest() {
        val iteratorAction = createIteratorAction(listOf("build"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-1.7 compression: default", iteratorAction.toString())
    }

    @Test
    fun buildActionVersionTest() {
        val iteratorAction = createIteratorAction(listOf("b", "--v:1.0"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-1.0 compression: default", iteratorAction.toString())
    }

    @Test
    fun buildActionVersion1Test() {
        val iteratorAction = createIteratorAction(listOf("b", "--v:1.1"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-1.1 compression: default", iteratorAction.toString())
    }

    @Test
    fun buildActionVersion2Test() {
        val iteratorAction = createIteratorAction(listOf("b", "--v:1.2"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-1.2 compression: default", iteratorAction.toString())
    }

    @Test
    fun buildActionVersion3Test() {
        val iteratorAction = createIteratorAction(listOf("b", "--v:1.3"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-1.3 compression: default", iteratorAction.toString())
    }

    @Test
    fun buildActionVersion4Test() {
        val iteratorAction = createIteratorAction(listOf("b", "--v:1.4"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-1.4 compression: default", iteratorAction.toString())
    }

    @Test
    fun buildActionVersion5Test() {
        val iteratorAction = createIteratorAction(listOf("b", "--v:1.5"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-1.5 compression: default", iteratorAction.toString())
    }

    @Test
    fun buildActionVersion6Test() {
        val iteratorAction = createIteratorAction(listOf("b", "--v:1.6"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-1.6 compression: default", iteratorAction.toString())
    }

    @Test
    fun buildActionVersion7Test() {
        val iteratorAction = createIteratorAction(listOf("b", "--v:1.7"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-1.7 compression: default", iteratorAction.toString())
    }

    @Test
    fun buildActionVersion8Test() {
        val iteratorAction = createIteratorAction(listOf("b", "--v:2.0"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-2.0 compression: default", iteratorAction.toString())
    }

    @Test
    fun buildActionCompressionTest() {
        val iteratorAction = createIteratorAction(listOf("b", "--c:best"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-1.7 compression: best", iteratorAction.toString())
    }

    @Test
    fun buildActionDefaultCompressionTest() {
        val iteratorAction = createIteratorAction(listOf("b", "--c:default"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-1.7 compression: default", iteratorAction.toString())
    }

    @Test
    fun buildActionDefault1CompressionTest() {
        val iteratorAction = createIteratorAction(listOf("b", "--c:wrong"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-1.7 compression: default", iteratorAction.toString())
    }

    @Test
    fun buildActionAllTest() {
        val iteratorAction = createIteratorAction(listOf("build", "--v:2.0", "--c:fast"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-2.0 compression: fast", iteratorAction.toString())
    }

    @Test
    fun malformedAction() {
        try {
            createIteratorAction(listOf("uild", "--v:2.0", "--c:fast"), imageToPdfOptions = ImageToPdfOptions.createOptions())
        } catch (exception: MalformedPdfActionException) {
            return
        }

        fail("MalformedPdfActionException was not thrown")
    }
}