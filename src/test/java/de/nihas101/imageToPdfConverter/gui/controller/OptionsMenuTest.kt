package de.nihas101.imageToPdfConverter.gui.controller

import com.itextpdf.kernel.pdf.CompressionConstants
import com.itextpdf.kernel.pdf.PdfVersion
import de.nihas101.imageToPdfConverter.gui.MainWindow
import javafx.scene.control.CheckBox
import javafx.stage.Stage
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest

class OptionsMenuTest : ApplicationTest() {
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
        clickOn("#zipFilesCheckBox")
        closeCurrentWindow()
        assertEquals(true, mainWindow!!.imageToPdfOptions.getIteratorOptions().zipFiles)
    }

    @Test
    fun multipleDirectoriesCheckBox() {
        clickOn("#optionsButton")
        clickOn("#multipleDirectoriesCheckBox")
        closeCurrentWindow()
        assertEquals(true, mainWindow!!.imageToPdfOptions.getIteratorOptions().multipleDirectories)
    }

    @Test
    fun deleteOnExitCheckBox() {
        clickOn("#optionsButton")
        clickOn("#deleteOnExitCheckBox")
        closeCurrentWindow()
        assertEquals(false, mainWindow!!.imageToPdfOptions.getIteratorOptions().deleteOnExit)
    }

    @Test
    fun pdfVersionToggle() {
        clickOn("#optionsButton")
        clickOn("#pdfMenu")
        clickOn("#PDF_1_0_item")
        closeCurrentWindow()
        assertEquals(PdfVersion.PDF_1_0, mainWindow!!.imageToPdfOptions.getPdfOptions().pdfVersion)
    }

    @Test
    fun pdfBestCompressionToggle() {
        clickOn("#optionsButton")
        clickOn("#compressionMenu")
        clickOn("#bestCompression_item")
        closeCurrentWindow()
        assertEquals(CompressionConstants.BEST_COMPRESSION, mainWindow!!.imageToPdfOptions.getPdfOptions().compressionLevel)
    }

    @Test
    fun pdfBestCompressionTogglePersistance() {
        clickOn("#optionsButton")
        clickOn("#compressionMenu")
        clickOn("#bestCompression_item")
        closeCurrentWindow()

        clickOn("#optionsButton")
        closeCurrentWindow()
        assertEquals(CompressionConstants.BEST_COMPRESSION, mainWindow!!.imageToPdfOptions.getPdfOptions().compressionLevel)
    }

    @Test
    fun pdfNoCompressionTogglePersistance() {
        clickOn("#optionsButton")
        clickOn("#compressionMenu")
        clickOn("#noCompression_item")
        closeCurrentWindow()

        clickOn("#optionsButton")
        closeCurrentWindow()
        assertEquals(CompressionConstants.NO_COMPRESSION, mainWindow!!.imageToPdfOptions.getPdfOptions().compressionLevel)
    }

    @Test
    fun optionsPersistance() {
        clickOn("#optionsButton")
        clickOn("#zipFilesCheckBox")
        clickOn("#multipleDirectoriesCheckBox")
        clickOn("#deleteOnExitCheckBox")
        clickOn("#pdfMenu")
        clickOn("#PDF_2_0_item")
        clickOn("#compressionMenu")
        clickOn("#speedCompression_item")
        closeCurrentWindow()

        assertEquals(PdfVersion.PDF_2_0, mainWindow!!.imageToPdfOptions.getPdfOptions().pdfVersion)
        assertEquals(CompressionConstants.BEST_SPEED, mainWindow!!.imageToPdfOptions.getPdfOptions().compressionLevel)

        clickOn("#optionsButton")

        assertEquals(true, lookup("#zipFilesCheckBox").query<CheckBox>().isSelected)
        assertEquals(true, lookup("#multipleDirectoriesCheckBox").query<CheckBox>().isSelected)
        assertEquals(false, lookup("#deleteOnExitCheckBox").query<CheckBox>().isSelected)

        clickOn("#pdfMenu")
        clickOn("#PDF_1_0_item")
        clickOn("#compressionMenu")
        clickOn("#noCompression_item")

        closeCurrentWindow()

        assertEquals(PdfVersion.PDF_1_0, mainWindow!!.imageToPdfOptions.getPdfOptions().pdfVersion)
        assertEquals(CompressionConstants.NO_COMPRESSION, mainWindow!!.imageToPdfOptions.getPdfOptions().compressionLevel)
    }
}