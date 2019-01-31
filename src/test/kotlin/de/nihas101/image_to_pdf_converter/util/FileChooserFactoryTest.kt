package de.nihas101.image_to_pdf_converter.util

import de.nihas101.image_to_pdf_converter.pdf.pdf_options.IteratorOptions
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest

class FileChooserFactoryTest : ApplicationTest() {

    @Test
    fun createDirectoryChooserTest() {
        val iteratorOptions = IteratorOptions()
        createDirectoryChooser(iteratorOptions)
    }

    @Test
    fun createZipFileChooserTest() {
        createZipFileChooser()
    }

    @Test
    fun createSaveFileChooserTest() {
        createSaveFileChooser()
    }
}