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

        val imageView = lookup("#imageDisplayView").query<ImageView>()
        checkNotNull(imageView.image)
    }

    @Test
    fun displayDirectory() {
        setupDirectoryIterator("src/test/resources", true)

        clickOnFirstCell()

        val directoryContentDisplayButton = lookup("#directoryContentDisplayBuildButton").queryButton()
        checkNotNull(directoryContentDisplayButton)
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
        doubleClickOn(bounds.minX + 100, bounds.minY + 115)
    }
}