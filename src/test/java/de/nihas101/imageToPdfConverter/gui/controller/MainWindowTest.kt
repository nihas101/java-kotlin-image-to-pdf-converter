package de.nihas101.imageToPdfConverter.gui.controller

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.gui.MainWindow
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest
import java.io.File


class MainWindowTest : ApplicationTest() {
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

    private fun setupDirectoryIterator(path: String, multipleDirectories: Boolean = false): DirectoryIterator {
        val directoryIterator = DirectoryIterator.createDirectoryIterator(
                File(path),
                IteratorOptions(multipleDirectories = multipleDirectories)
        )

        mainWindow!!.setupIterator(directoryIterator)
        mainWindowController!!.setupListView(directoryIterator)
        return directoryIterator
    }

    @Test
    fun deleteImageTest() {
        val directoryIterator = setupDirectoryIterator("src/test/resources/images")
        clickOnFirstCell(false)
        Thread.sleep(waitingPeriod)
        push(KeyCode.DELETE)
        Thread.sleep(waitingPeriod)

        assertEquals(3, directoryIterator.numberOfFiles())
    }

    @Test
    fun deleteDirectoryTest() {
        val directoryIterator = setupDirectoryIterator("src/test/resources", true)

        clickOnFirstCell(false)
        Thread.sleep(waitingPeriod)
        push(KeyCode.DELETE)
        Thread.sleep(waitingPeriod)

        assertEquals(0, directoryIterator.numberOfFiles())
    }

    private fun clickOnFirstCell(doubleClick: Boolean) {
        val coordinates = getCoordinatesOfFirstCell()
        if (doubleClick)
            doubleClickOn(coordinates[0], coordinates[1])
        else
            clickOn(coordinates[0], coordinates[1])
        Thread.sleep(waitingPeriod)
    }

    private fun getCoordinatesOfFirstCell(): List<Double> {
        val bounds = mainWindow!!.root.localToScreen(mainWindowController!!.imageListView.boundsInLocal)
        return listOf(bounds.minX + bounds.width * .08, bounds.minY + bounds.height * .6)
    }

    @Test
    fun buildSinglePdf() {
        val file = File("src/test/resources/images.pdf")
        setupDirectoryIterator("src/test/resources/images")

        mainWindowController!!.buildPdf(file)

        file.deleteOnExit()
        Thread.sleep(waitingPeriod)
        assertEquals(true, file.exists())
    }

    @Test
    fun buildMultiplePdf() {
        clickOn("#optionsButton")
        clickOn("#multipleDirectoriesCheckBox")
        closeCurrentWindow()
        val directory = File("src/test/resources/test")
        val pdf = File("src/test/resources/test/images.pdf")
        directory.mkdir()
        setupDirectoryIterator("src/test/resources", true)

        mainWindowController!!.buildPdf(directory)

        directory.deleteOnExit()
        pdf.deleteOnExit()
        Thread.sleep(waitingPeriod)
        assertEquals(true, pdf.exists())
    }
}