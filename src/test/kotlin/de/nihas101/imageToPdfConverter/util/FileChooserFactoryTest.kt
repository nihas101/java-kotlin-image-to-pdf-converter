package de.nihas101.imageToPdfConverter.util

import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import javafx.application.Platform.runLater
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest

class FileChooserFactoryTest : ApplicationTest() {

    @Test
    fun createDirectoryChooserTest() {
        runLater {
            val iteratorOptions = IteratorOptions()
            createDirectoryChooser(iteratorOptions).showDialog(this.targetWindow())
        }
    }

    @Test
    fun createZipFileChooserTest() {
        runLater {
            createZipFileChooser().showOpenDialog(this.targetWindow())
        }
    }

    @Test
    fun createSaveFileChooserTest() {
        runLater {
            createSaveFileChooser().showOpenDialog(this.targetWindow())
        }
    }
}