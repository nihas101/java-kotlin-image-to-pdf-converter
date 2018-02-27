package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import java.io.File

open class SetupIteratorTask protected constructor(
        protected val directoryIterator: DirectoryIterator,
        protected val directory: File,
        before: () -> Unit,
        after: () -> Unit
) : CancellableTask(before, directoryIterator, after) {

    override fun call() {
        before()
        try {
            directoryIterator.setupDirectory(directory)
        } catch (exception: InterruptedException) {
            /* The task was cancelled */
        }
        after()
    }

    companion object SetupIteratorTaskFactory {
        fun createSetupIteratorTask(
                directoryIterator: DirectoryIterator,
                file: File,
                before: () -> Unit = {},
                after: () -> Unit = {}
        ): SetupIteratorTask {
            return SetupIteratorTask(
                    directoryIterator,
                    file,
                    before,
                    after
            )
        }
    }
}