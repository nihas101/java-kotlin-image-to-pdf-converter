package de.nihas101.image_to_pdf_converter.tasks

import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.IteratorOptions
import de.nihas101.image_to_pdf_converter.tasks.SetupIteratorTask.SetupIteratorTaskFactory.createSetupIteratorTask
import de.nihas101.image_to_pdf_converter.util.TrivialProgressUpdater
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class SetupIteratorTaskTest {

    @Test
    fun call() {
        var beforeExecuted = false
        var afterExecuted = false
        val directoryIterator = createDirectoryIterator(IteratorOptions())

        val callClosure = CallClosure({ beforeExecuted = true }, { afterExecuted = true })

        val setupIteratorTask = createSetupIteratorTask(
                directoryIterator,
                File("src/test/resources/images"),
                TrivialProgressUpdater(),
                callClosure
        )

        setupIteratorTask.executeTask()

        assertEquals(true, beforeExecuted)
        assertEquals(true, afterExecuted)
        assertEquals(4, directoryIterator.numberOfFiles())
    }
}