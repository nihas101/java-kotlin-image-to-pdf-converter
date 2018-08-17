/*
 JaKoImage2PDF is a program for converting images to PDFs with the use of iText 7
 Copyright (C) 2018  Nikita Hasert

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.nihas101.imageToPdfConverter.util

import de.nihas101.imageToPdfConverter.gui.controller.MainWindowController
import javafx.application.Platform.runLater
import javafx.scene.paint.Color.WHITE
import javafx.scene.paint.Paint
import org.slf4j.LoggerFactory
import java.io.File

interface ProgressUpdater {
    fun updateProgress(progress: Double, file: File)
}

class TrivialProgressUpdater : ProgressUpdater {
    override fun updateProgress(progress: Double, file: File) {
        /* DO NOTHING */
    }
}

abstract class FileProgressUpdater(private val mainWindowController: MainWindowController) : ProgressUpdater {
    protected fun updateProgress(progress: Double, message: String) {
        mainWindowController.buildProgressBar.progress = progress
        runLater { mainWindowController.notifyUser(message, WHITE) }
    }
}

class BuildProgressUpdater(mainWindowController: MainWindowController) : FileProgressUpdater(mainWindowController) {
    private val logger = LoggerFactory.getLogger(BuildProgressUpdater::class.java)!!

    override fun updateProgress(progress: Double, file: File) {
        super.updateProgress(progress, "Building PDF:  ${file.name}")
        logger.info("Building PDF:  ${file.name}")
    }
}

class IteratorSetupProgressUpdater(mainWindowController: MainWindowController) : FileProgressUpdater(mainWindowController) {
    private val logger = LoggerFactory.getLogger(IteratorSetupProgressUpdater::class.java)!!

    override fun updateProgress(progress: Double, file: File) {
        super.updateProgress(progress, "Processing file(s):  ${file.name}")
        logger.info("Processing file(s):  ${file.name}")
    }
}

class LoadProgressUpdater(private val notifyUser: (String, Paint) -> Unit, private val numberOfFiles: Int) : ProgressUpdater {
    private val logger = LoggerFactory.getLogger(LoadProgressUpdater::class.java)!!

    override fun updateProgress(progress: Double, file: File) {
        runLater { notifyUser("Loading files... (${progress.toInt()}/$numberOfFiles)", WHITE) }
        logger.info("Loading files... (${progress.toInt()}/$numberOfFiles)")
    }
}