package de.nihas101.imageToPdfConverter.gui.controller

import com.itextpdf.kernel.pdf.CompressionConstants
import com.itextpdf.kernel.pdf.PdfVersion
import de.nihas101.imageToPdfConverter.gui.MainWindow
import javafx.stage.Stage
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest

class OptionsMenuTest : ApplicationTest() {
    private val waitingPeriod = 500L
    private var mainWindow: MainWindow? = null
    private var mainWindowController: MainWindowController? = null

    override fun start(stage: Stage?) {
        mainWindow = MainWindow()
        mainWindow!!.start(Stage())
        mainWindowController = mainWindow!!.mainWindowController
    }

    @After
    fun closeWindow() {
        closeCurrentWindow()
    }

    @Test
    fun zipFilesCheckBox() {
        clickOn("#optionsButton")
        Thread.sleep(waitingPeriod)
        clickOn("#zipFilesCheckBox")
        Thread.sleep(waitingPeriod)
        closeCurrentWindow()
        assertEquals(true, mainWindow!!.imageToPdfOptions.getIteratorOptions().zipFiles)
    }

    @Test
    fun multipleDirectoriesCheckBox() {
        clickOn("#optionsButton")
        Thread.sleep(waitingPeriod)
        clickOn("#multipleDirectoriesCheckBox")
        Thread.sleep(waitingPeriod)
        closeCurrentWindow()
        assertEquals(true, mainWindow!!.imageToPdfOptions.getIteratorOptions().multipleDirectories)
    }

    @Test
    fun deleteOnExitCheckBox() {
        clickOn("#optionsButton")
        Thread.sleep(waitingPeriod)
        clickOn("#deleteOnExitCheckBox")
        Thread.sleep(waitingPeriod)
        closeCurrentWindow()
        assertEquals(false, mainWindow!!.imageToPdfOptions.getIteratorOptions().deleteOnExit)
    }

    @Test
    fun pdfVersionToggle() {
        clickOn("#optionsButton")
        Thread.sleep(waitingPeriod)
        clickOn("#pdfMenu")
        Thread.sleep(waitingPeriod)
        clickOn("#PDF_1_0_item")
        Thread.sleep(waitingPeriod)
        closeCurrentWindow()
        assertEquals(PdfVersion.PDF_1_0, mainWindow!!.imageToPdfOptions.getPdfOptions().pdfVersion)
    }

    @Test
    fun pdfBestCompressionToggle() {
        clickOn("#optionsButton")
        Thread.sleep(waitingPeriod)
        clickOn("#compressionMenu")
        Thread.sleep(waitingPeriod)
        clickOn("#bestCompression_item")
        Thread.sleep(waitingPeriod)
        closeCurrentWindow()
        assertEquals(CompressionConstants.BEST_COMPRESSION, mainWindow!!.imageToPdfOptions.getPdfOptions().compressionLevel)
    }
}