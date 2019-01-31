package de.nihas101.image_to_pdf_converter.tasks

import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator
import de.nihas101.image_to_pdf_converter.pdf.builders.PdfBuilder.PdfBuilderFactory.createPdfBBuilder
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.ImageToPdfOptions.OptionsFactory.createOptions
import de.nihas101.image_to_pdf_converter.tasks.BuildPdfTask.BuildPdfTaskFactory.createBuildPdfTask
import de.nihas101.image_to_pdf_converter.util.TrivialProgressUpdater
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.File


class BuildPdfTaskTest {
    @Test
    fun call() {
        var beforeExecuted = false
        var afterExecuted = true
        val imageToPdfOptions = createOptions()
        val sourceFile = File("src/test/resources/images")
        val targetFile = File("src/test/resources/images/buildTaskTest.pdf")
        targetFile.createNewFile()
        targetFile.deleteOnExit()
        imageToPdfOptions.setSaveLocation(targetFile)

        val callClosure = CallClosure({ beforeExecuted = true }, { afterExecuted = true })

        val buildPdfTask = createBuildPdfTask(
                createPdfBBuilder(imageToPdfOptions.getIteratorOptions()),
                createDirectoryIterator(sourceFile, imageToPdfOptions.getIteratorOptions()),
                imageToPdfOptions,
                TrivialProgressUpdater(),
                callClosure
        )

        buildPdfTask.executeTask()

        assertEquals(true, beforeExecuted)
        assertEquals(true, afterExecuted)
        assertEquals(true, targetFile.exists())
    }
}