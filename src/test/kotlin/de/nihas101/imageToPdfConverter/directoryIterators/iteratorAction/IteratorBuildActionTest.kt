package de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction

import de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction.IteratorAction.PdfModificationFactory.createIteratorAction
import de.nihas101.imageToPdfConverter.pdf.ImageToPdfOptions
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
    fun buildActionCompressionTest() {
        val iteratorAction = createIteratorAction(listOf("b", "--c:best"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-1.7 compression: best", iteratorAction.toString())
    }

    @Test
    fun buildActionAllTest() {
        val iteratorAction = createIteratorAction(listOf("build", "--v:2.0", "--c:fast"), imageToPdfOptions = ImageToPdfOptions.createOptions())

        assertEquals("build version: PDF-2.0 compression: fast", iteratorAction.toString())
    }
}