package de.nihas101.imageToPdfConverter.util

import java.io.File

interface ProgressUpdater {
    fun updateProgress(progress: Double, file: File)
}

class TrivialProgressUpdate : ProgressUpdater {
    override fun updateProgress(progress: Double, file: File) {
        /* DO NOTHING */
    }
}