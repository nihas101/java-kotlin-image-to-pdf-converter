package de.nihas101.imageToPdfConverter.util

import de.nihas101.imageToPdfConverter.gui.controller.MainWindowController
import javafx.application.Platform.runLater
import javafx.scene.paint.Color.BLACK
import javafx.scene.paint.Paint
import java.io.File

interface ProgressUpdater {
    fun updateProgress(progress: Double, file: File)
}

class TrivialProgressUpdater : ProgressUpdater {
    override fun updateProgress(progress: Double, file: File) {
        /* DO NOTHING */
    }
}

class BuildProgressUpdater(private val mainWindowController: MainWindowController) : ProgressUpdater {
    override fun updateProgress(progress: Double, file: File) {
        mainWindowController.buildProgressBar.progress = progress
        runLater { mainWindowController.notifyUser("Building PDF: " + file.name, BLACK) }
    }
}

class LoadProgressUpdater(private val notifyUser: (String, Paint) -> Unit, private val numberOfFiles: Int) : ProgressUpdater {
    override fun updateProgress(progress: Double, file: File) {
        runLater { notifyUser("Loading files... (${progress.toInt()}/$numberOfFiles)", BLACK) }
    }
}