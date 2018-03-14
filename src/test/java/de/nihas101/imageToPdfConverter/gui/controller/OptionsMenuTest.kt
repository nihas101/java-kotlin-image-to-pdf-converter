package de.nihas101.imageToPdfConverter.gui.controller

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
        Thread.sleep(waitingPeriod)
        assertEquals(true, mainWindow!!.imageToPdfOptions.getIteratorOptions().zipFiles)
    }

    @Test
    fun multipleDirectoriesCheckBox() {
        clickOn("#optionsButton")
        Thread.sleep(waitingPeriod)
        clickOn("#multipleDirectoriesCheckBox")
        Thread.sleep(waitingPeriod)
        closeCurrentWindow()
        Thread.sleep(waitingPeriod)
        assertEquals(true, mainWindow!!.imageToPdfOptions.getIteratorOptions().multipleDirectories)
    }

    @Test
    fun deleteOnExitCheckBox() {
        clickOn("#optionsButton")
        Thread.sleep(waitingPeriod)
        clickOn("#deleteOnExitCheckBox")
        Thread.sleep(waitingPeriod)
        closeCurrentWindow()
        Thread.sleep(waitingPeriod)
        assertEquals(false, mainWindow!!.imageToPdfOptions.getIteratorOptions().deleteOnExit)
    }
}