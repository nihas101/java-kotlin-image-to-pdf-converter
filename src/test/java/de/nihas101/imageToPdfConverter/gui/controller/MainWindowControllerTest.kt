package de.nihas101.imageToPdfConverter.gui.controller

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.gui.MainWindow
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest
import java.io.File


class MainWindowTest : ApplicationTest() {
    private var mainWindow: MainWindow? = null
    private var mainWindowController: MainWindowController? = null

    override fun start(stage: Stage?) {
        mainWindow = MainWindow()
        mainWindow!!.start(Stage())
        mainWindowController = mainWindow!!.mainWindowController
    }

    @Test
    fun displayImage() {
        setupDirectoryIterator("src/test/resources/images")

        clickOnFirstCell(true)

        assertImageIsDisplayed()
    }

    private fun assertImageIsDisplayed() {
        val imageView = lookup("#imageDisplayView").query<ImageView>()
        checkNotNull(imageView.image)
    }

    @Test
    fun displayDirectory() {
        setupDirectoryIterator("src/test/resources", true)

        clickOnFirstCell(true)

        assertDirectoryIsDisplayed()
    }

    private fun assertDirectoryIsDisplayed() {
        val listView = lookup("#imageListView").nth(1).queryListView<File>()
        checkNotNull(listView)
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
        Thread.sleep(1000)
    }

    private fun getCoordinatesOfFirstCell(): List<Double> {
        val bounds = mainWindow!!.root.localToScreen(mainWindowController!!.imageListView.boundsInLocal)
        return listOf(bounds.minX + bounds.width * .08, bounds.minY + bounds.height * .6)
    }
}