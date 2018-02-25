package de.nihas101.imageToPdfConverter.util

import de.nihas101.imageToPdfConverter.gui.controller.MainWindowController
import javafx.scene.paint.Color.BLACK
import java.io.File

interface ProgressUpdater {
    fun updateProgress(progress: Double, file: File)
}

class TrivialProgressUpdater : ProgressUpdater {
    override fun updateProgress(progress: Double, file: File) {
        /* DO NOTHING */
    }
}

class MainWindowProgressUpdater(private val mainWindowController: MainWindowController) : ProgressUpdater {
    override fun updateProgress(progress: Double, file: File) {
        mainWindowController.buildProgressBar.progress = progress
        mainWindowController.notifyUser("Building PDF: " + file.name, BLACK)
    }
}