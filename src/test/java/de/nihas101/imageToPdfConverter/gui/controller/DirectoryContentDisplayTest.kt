package de.nihas101.imageToPdfConverter.gui.controller

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.gui.MainWindow
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import javafx.scene.control.ListView
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest
import java.io.File

class DirectoryContentDisplayTest : ApplicationTest() {
    private var waitingPeriod = 1000L
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
    fun dragTest() {
        setupDirectoryIterator("src/test/resources", true)

        clickOnFirstCell(true)
        Thread.sleep(waitingPeriod)

        val itemBefore = getDirectoryContentDisplayImageListView().items[0]

        val coordinates = getCoordinatesOfFirstCell()
        drag(coordinates[0], coordinates[1]).dropTo(coordinates[0] + 350, coordinates[1])
        Thread.sleep(waitingPeriod)

        assertItemMoved(itemBefore)
    }

    private fun getDirectoryContentDisplayImageListView(): ListView<File> {
        return if (lookup("#imageListView").nth(0).queryListView<File>().items.size > 1)
            lookup("#imageListView").nth(0).queryListView<File>()
        else lookup("#imageListView").nth(1).queryListView<File>()
    }

    private fun assertItemMoved(itemBefore: File) {
        assertEquals(true, fileMoved(itemBefore, getDirectoryContentDisplayImageListView()))
        closeCurrentWindow()

    }

    private fun fileMoved(file: File, listView: ListView<File>): Boolean {
        return try {
            0 != listView.items.indexOf(file)
        } catch (exception: IndexOutOfBoundsException) {
            println("out of bounds")
            false
        }
    }

    @Test
    fun displayImageOfDirectory() {
        setupDirectoryIterator("src/test/resources", true)

        clickOnFirstCell(true)
        clickOnFirstCell(true)

        assertImageIsDisplayed()
        closeCurrentWindow()
    }

    @Test
    fun delete() {
        setupDirectoryIterator("src/test/resources", true)

        clickOnFirstCell(true)
        clickOnFirstCell(false)

        push(KeyCode.DELETE)

        assertEquals(3, getDirectoryContentDisplayImageListView().items.size)
        closeCurrentWindow()
    }

    @Test
    fun menuDelete() {
        setupDirectoryIterator("src/test/resources", true)

        clickOnFirstCell(true)

        val coordinates = getCoordinatesOfFirstCell()
        rightClickOn(coordinates[0], coordinates[1])
        clickOnDelete()

        assertEquals(3, getDirectoryContentDisplayImageListView().items.size)
        closeCurrentWindow()
    }

    private fun clickOnDelete() {
        val coordinates = getCoordinatesOfFirstCell()
        clickOn(coordinates[0] + 10, coordinates[1] + 120)
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