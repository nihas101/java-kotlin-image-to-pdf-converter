package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import java.io.File

class SetupIteratorFromDragAndDropTask(
        directoryIterator: DirectoryIterator,
        directory: File,
        before: () -> Unit,
        after: () -> Unit
) : SetupIteratorTask(directoryIterator, directory, before, after) {


    companion object SetupIteratorFromDragAndDropTaskFactory {
        fun createSetupIteratorTask(directoryIterator: DirectoryIterator, directory: File, before: () -> Unit, after: () -> Unit): SetupIteratorFromDragAndDropTask {
            return SetupIteratorFromDragAndDropTask(
                    directoryIterator,
                    directory,
                    before,
                    after
            )
        }
    }
}