package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.ToolkitInitializer.Companion.initalizeToolkit
import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator
import de.nihas101.imageToPdfConverter.pdf.builders.PdfBuilder.PdfBuilderFactory.createPdfBBuilder
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions.OptionsFactory.createOptions
import de.nihas101.imageToPdfConverter.tasks.BuildPdfTask.BuildPdfTaskFactory.createBuildPdfTask
import de.nihas101.imageToPdfConverter.util.TrivialProgressUpdater
import org.junit.Assert.assertEquals
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


        val buildPdfTask = createBuildPdfTask(
                createPdfBBuilder(imageToPdfOptions.getIteratorOptions()),
                createDirectoryIterator(sourceFile, imageToPdfOptions.getIteratorOptions()),
                imageToPdfOptions,
                TrivialProgressUpdater(),
                { beforeExecuted = true },
                { afterExecuted = true }
        )

        buildPdfTask.executeTask()

        assertEquals(true, beforeExecuted)
        assertEquals(true, afterExecuted)
        assertEquals(true, targetFile.exists())
    }

    @Test
    fun cancelTask() {
        var beforeExecuted = false
        var afterExecuted = true
        val imageToPdfOptions = createOptions()
        val sourceFile = File("src/test/resources/images")
        val targetFile = File("src/test/resources/images/buildTaskTest.pdf")
        targetFile.createNewFile()
        targetFile.deleteOnExit()
        imageToPdfOptions.setSaveLocation(targetFile)

        initalizeToolkit()

        val buildPdfTask = createBuildPdfTask(
                createPdfBBuilder(imageToPdfOptions.getIteratorOptions()),
                createDirectoryIterator(sourceFile, imageToPdfOptions.getIteratorOptions()),
                imageToPdfOptions,
                TrivialProgressUpdater(),
                { beforeExecuted = true },
                { afterExecuted = true }
        )

        buildPdfTask.cancel()
        buildPdfTask.executeTask()

        assertEquals(true, beforeExecuted)
        assertEquals(true, afterExecuted)
        assertEquals(true, targetFile.exists())

    }
}