package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.util.ImageMap
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import javafx.concurrent.Task

class LoadImagesTask(
        private val directoryIterator: DirectoryIterator,
        private val imageMap: ImageMap,
        private val updater: ProgressUpdater,
        private val after: () -> Unit
) : Task<Unit>() {

    override fun call() {
        imageMap.loadImages(directoryIterator.getFiles(), updater)
        after()
    }

    companion object LoadImagesThreadFactory {
        fun createLoadImagesThread(
                directoryIterator: DirectoryIterator,
                imageMap: ImageMap,
                progressUpdater: ProgressUpdater,
                after: () -> Unit
        ): Thread {
            val thread = Thread(LoadImagesTask(directoryIterator, imageMap, progressUpdater, after))
            thread.isDaemon = true
            return thread
        }
    }
}