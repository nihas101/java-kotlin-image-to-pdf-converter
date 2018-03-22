package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import de.nihas101.imageToPdfConverter.tasks.SetupIteratorFromDragAndDropTask.SetupIteratorFromDragAndDropTaskFactory.createSetupIteratorTask
import de.nihas101.imageToPdfConverter.util.TrivialProgressUpdater
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class SetupIteratorFromDragAndDropTaskTest {
    @Test
    fun call() {
        var beforeExecuted = false
        var afterExecuted = false
        val directoryIterator = createDirectoryIterator(IteratorOptions())

        val setupIteratorTask = createSetupIteratorTask(
                directoryIterator,
                File("src/test/resources/images"),
                TrivialProgressUpdater(),
                { beforeExecuted = true },
                { afterExecuted = true }
        )

        setupIteratorTask.executeTask()

        assertEquals(true, beforeExecuted)
        assertEquals(true, afterExecuted)
        assertEquals(4, directoryIterator.numberOfFiles())
    }
}