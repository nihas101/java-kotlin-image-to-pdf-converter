package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator
import javafx.concurrent.Task
import java.io.File

class CreateImageFilesIteratorTask(private val file: File, private val after: (DirectoryIterator) -> Unit) : Task<Unit>() {
    override fun call() {
        val imageFilesIterator = ImageFilesIterator.createImageFilesIterator(file)
        after(imageFilesIterator)
    }

    companion object CreateImageFilesIteratorThreadFactory {
        fun createCreateImageFilesIteratorThread(file: File, after: (DirectoryIterator) -> Unit): Thread {
            val thread = Thread(CreateImageFilesIteratorTask(file, after))
            thread.isDaemon = true
            return thread
        }
    }
}