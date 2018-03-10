package de.nihas101.imageToPdfConverter.gui.controller

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.gui.MainWindow
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import javafx.scene.image.ImageView
import javafx.stage.Stage
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

        clickOnFirstCell()

        assertImageIsDisplayed()
    }

    private fun assertImageIsDisplayed() {
        val imageView = lookup("#imageDisplayView").query<ImageView>()
        checkNotNull(imageView.image)
    }

    @Test
    fun displayDirectory() {
        setupDirectoryIterator("src/test/resources", true)

        clickOnFirstCell()

        assertDirectoryIsDisplayed()
    }

    private fun assertDirectoryIsDisplayed() {
        val listView = lookup("#imageListView").nth(1).queryListView<File>()
        checkNotNull(listView)
    }

    private fun setupDirectoryIterator(path: String, multipleDirectories: Boolean = false) {
        val directoryIterator = DirectoryIterator.createDirectoryIterator(
                File(path),
                IteratorOptions(multipleDirectories = multipleDirectories)
        )

        mainWindow!!.setupIterator(directoryIterator)
        mainWindowController!!.setupListView(directoryIterator)
    }

    private fun clickOnFirstCell() {
        val bounds = mainWindow!!.root.localToScreen(mainWindowController!!.imageListView.boundsInLocal)
        doubleClickOn(bounds.minX + bounds.width * .08, bounds.minY + bounds.height * .6)
        Thread.sleep(1000)
    }
}