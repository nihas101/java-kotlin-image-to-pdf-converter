package de.nihas101.imageToPdfConverter.util

import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
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