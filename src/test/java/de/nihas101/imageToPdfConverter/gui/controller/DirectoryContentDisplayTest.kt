package de.nihas101.imageToPdfConverter.gui.controller

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.gui.MainWindow
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import javafx.scene.image.ImageView
import javafx.stage.Stage
import org.junit.After
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest
import java.io.File

class DirectoryContentDisplayTest : ApplicationTest() {
    private var waitingPeriod = 500L
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
    fun displayImage() {
        setupDirectoryIterator("src/test/resources/images")

        clickOnFirstCell(true)

        assertImageIsDisplayed()
    }

    private fun assertImageIsDisplayed() {
        val imageView = lookup("#imageDisplayView").query<ImageView>()
        checkNotNull(imageView.image)
        closeCurrentWindow()
    }

    @Test
    fun displayDirectory() {
        setupDirectoryIterator("src/test/resources", true)

        clickOnFirstCell(true)
        Thread.sleep(waitingPeriod)

        assertDirectoryIsDisplayed()
    }

    @Test
    fun displayImageOfDirectory() {
        setupDirectoryIterator("src/test/resources", true)

        clickOnFirstCell(true)
        Thread.sleep(waitingPeriod)
        clickOnFirstCell(true)
        Thread.sleep(waitingPeriod)

        assertImageIsDisplayed()
        closeCurrentWindow()
    }

    private fun assertDirectoryIsDisplayed() {
        val listView = lookup("#imageListView").nth(1).queryListView<File>()
        checkNotNull(listView)
        closeCurrentWindow()
    }

    private fun clickOnFirstCell(doubleClick: Boolean) {
        val coordinates = getCoordinatesOfFirstCell()
        if (doubleClick) doubleClickOn(coordinates[0], coordinates[1])
        else clickOn(coordinates[0], coordinates[1])
    }

    private fun getCoordinatesOfFirstCell(): List<Double> {
        val bounds = mainWindow!!.root.localToScreen(mainWindowController!!.imageListView.boundsInLocal)
        return listOf(bounds.minX + bounds.width * .08, bounds.minY + bounds.height * .6)
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
}