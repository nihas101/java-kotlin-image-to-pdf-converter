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
        val directoryIterator = DirectoryIterator.createDirectoryIterator(
                File("src/test/resources/images"),
                IteratorOptions()
        )

        mainWindow!!.setupIterator(directoryIterator)
        mainWindowController!!.setupListView(directoryIterator)

        doubleClickOn(mainWindowController!!.imageListView)

        val imageView = lookup("#imageDisplayView").query<ImageView>()

        checkNotNull(imageView.image)
    }
}