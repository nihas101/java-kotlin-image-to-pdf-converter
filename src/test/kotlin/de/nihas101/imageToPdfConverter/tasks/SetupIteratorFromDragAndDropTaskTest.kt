package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class SetupIteratorFromDragAndDropTaskTest {
    @Test
    fun call() {
        var beforeExecuted = false
        var afterExecuted = false
        val directoryIterator = createDirectoryIterator(IteratorOptions())

        val setupIteratorTask = SetupIteratorTask.createSetupIteratorTask(
                directoryIterator,
                File("src/test/resources/images"),
                { beforeExecuted = true },
                { afterExecuted = true }
        )

        setupIteratorTask.executeTask()

        assertEquals(true, beforeExecuted)
        assertEquals(true, afterExecuted)
        assertEquals(4, directoryIterator.numberOfFiles())
    }
}