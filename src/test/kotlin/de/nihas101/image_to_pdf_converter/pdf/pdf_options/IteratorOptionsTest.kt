package de.nihas101.image_to_pdf_converter.pdf.pdf_options

import org.junit.Assert.assertEquals
import org.junit.Test

class IteratorOptionsTest {
    @Test
    fun changedNothing() {
        val iteratorOptions = IteratorOptions()
        changeAssert(iteratorOptions, false, false, true, false)
    }

    @Test
    fun changedMultipleDirectories() {
        val iteratorOptions = IteratorOptions()
        changeAssert(iteratorOptions, true, false, true, true)
    }

    @Test
    fun changedZip() {
        val iteratorOptions = IteratorOptions()
        changeAssert(iteratorOptions, false, true, true, true)
    }

    @Test
    fun changedDelete() {
        val iteratorOptions = IteratorOptions()
        changeAssert(iteratorOptions, false, false, false, true)
    }

    @Test
    fun changedAll() {
        val iteratorOptions = IteratorOptions()
        changeAssert(iteratorOptions, true, true, false, true)
    }

    private fun changeAssert(iteratorOptions: IteratorOptions, multipleDirectories: Boolean, zipFiles: Boolean, deleteOnExit: Boolean, expected: Boolean) {
        val other = iteratorOptions.copy(multipleDirectories, zipFiles, deleteOnExit)

        assertEquals(expected, iteratorOptions.changed(other))
    }
}