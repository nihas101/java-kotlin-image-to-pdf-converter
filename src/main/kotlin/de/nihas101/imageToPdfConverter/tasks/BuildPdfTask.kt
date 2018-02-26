package de.nihas101.imageToPdfConverter.tasks

import javafx.concurrent.Task

class BuildPdfTask(
        private val build: () -> Unit
) : Task<Unit>() {
    override fun call() {
        build()
    }

    companion object BuildPdfThreadFactory {
        fun createBuildPdfThread(build: () -> Unit): Thread {
            return Thread(BuildPdfTask(build))
        }
    }
}