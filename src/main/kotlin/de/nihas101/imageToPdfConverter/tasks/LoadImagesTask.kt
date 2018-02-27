package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.util.ImageMap
import de.nihas101.imageToPdfConverter.util.ProgressUpdater

class LoadImagesTask(
        before: () -> Unit,
        private val imageMap: ImageMap,
        private val directoryIterator: DirectoryIterator,
        private val updater: ProgressUpdater,
        after: () -> Unit
) : CancellableTask(before, imageMap, after) {

    override fun call() {
        before()
        try {
            imageMap.loadImages(directoryIterator.getFiles(), updater)
        } catch (exception: InterruptedException) {
            /* The task was cancelled */
        }

        after()
    }

    companion object LoadImagesTaskFactory {
        fun createLoadImagesTask(
                imageMap: ImageMap,
                directoryIterator: DirectoryIterator,
                progressUpdater: ProgressUpdater,
                after: () -> Unit
        ): LoadImagesTask {
            return LoadImagesTask({}, imageMap, directoryIterator, progressUpdater, after)
        }
    }
}