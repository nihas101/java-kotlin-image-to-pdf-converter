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
    private val waitingPeriod = 1000L
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
        push(KeyCode.DELETE)

        assertEquals(3, directoryIterator.numberOfFiles())
    }

    @Test
    fun deleteDirectoryTest() {
        val directoryIterator = setupDirectoryIterator("src/test/resources", true)

        clickOnFirstCell(false)
        push(KeyCode.DELETE)

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

    @Test
    fun menuDeleteTest() {
        val directoryIterator = setupDirectoryIterator("src/test/resources/images")

        val coordinates = getCoordinatesOfFirstCell()
        rightClickOn(coordinates[0], coordinates[1])
        Thread.sleep(waitingPeriod)
        clickOnDelete()
        Thread.sleep(waitingPeriod)

        assertEquals(3, directoryIterator.numberOfFiles())
    }

    private fun clickOnDelete() {
        val coordinates = getCoordinatesOfFirstCell()
        clickOn(coordinates[0] + 10, coordinates[1] + 120)
    }

    private fun getCoordinatesOfFirstCell(): List<Double> {
        val bounds = mainWindow!!.root.localToScreen(mainWindowController!!.imageListView.boundsInLocal)
        return listOf(bounds.minX + bounds.width * .08, bounds.minY + bounds.height * .6)
    }

    @Test
    fun chooseNoFileBuild() {
        clickOn("#directoryButton")
        Thread.sleep(waitingPeriod)
        closeCurrentWindow()
        clickOn("#buildButton")
        Thread.sleep(waitingPeriod)
        assertEquals("Please choose a directory", mainWindowController!!.notificationText.text)
    }

    @Test
    fun dragTest() {
        val directoryIterator = setupDirectoryIterator("src/test/resources/images")
        val fileBefore = directoryIterator.getFile(0)

        val coordinates = getCoordinatesOfFirstCell()
        drag(coordinates[0], coordinates[1]).dropTo(coordinates[0] + 350, coordinates[1])

        assertEquals(fileBefore, directoryIterator.getFile(1))
    }

    @Test
    fun cancelSinglePDFBuild() {
        setupDirectoryIterator("src/test/resources/images")
        clickOn("#buildButton")
        closeWindow()
        Thread.sleep(waitingPeriod)
        assertEquals("Build cancelled by user", lookup("#notificationText").queryText().text)
    }

    @Test
    fun cancelMultiplePDFBuild() {
        clickOn("#optionsButton")
        Thread.sleep(waitingPeriod)
        clickOn("#multipleDirectoriesCheckBox")
        Thread.sleep(waitingPeriod)
        closeCurrentWindow()
        setupDirectoryIterator("src/test/resources", multipleDirectories = true)
        clickOn("#buildButton")
        closeWindow()
        Thread.sleep(waitingPeriod)
        assertEquals("Build cancelled by user", lookup("#notificationText").queryText().text)
    }

    @Test
    fun emptyBuild() {
        setupDirectoryIterator("src/test/resources/images", multipleDirectories = true)
        clickOn("#buildButton")
        Thread.sleep(waitingPeriod)
        assertEquals("There are no files to turn into a PDF", lookup("#notificationText").queryText().text)
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