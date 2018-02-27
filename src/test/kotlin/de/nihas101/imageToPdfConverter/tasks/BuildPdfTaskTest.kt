package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator
import de.nihas101.imageToPdfConverter.pdf.builders.PdfBuilder.PdfBuilderFactory.createPdfBBuilder
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions.OptionsFactory.createOptions
import de.nihas101.imageToPdfConverter.tasks.BuildPdfTask.BuildPdfTaskFactory.createBuildPdfTask
import de.nihas101.imageToPdfConverter.util.TrivialProgressUpdater
import javafx.embed.swing.JFXPanel
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.swing.SwingUtilities


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
    fun cancelBuild() {
        var beforeExecuted = false
        val imageToPdfOptions = createOptions()
        val pdfBuilder = createPdfBBuilder(imageToPdfOptions.getIteratorOptions())
        val sourceFile = File("src/test/resources/images")
        val targetFile = File("src/test/resources/images/buildTaskTestCancelled.pdf")
        val countDownLatch = CountDownLatch(1)
        val taskManager = TaskManager.createTaskManager()
        targetFile.createNewFile()
        targetFile.deleteOnExit()
        imageToPdfOptions.setSaveLocation(targetFile)

        initToolkit()

        val buildPdfTask = createBuildPdfTask(
                pdfBuilder,
                createDirectoryIterator(sourceFile, imageToPdfOptions.getIteratorOptions()),
                imageToPdfOptions,
                TrivialProgressUpdater(),
                {
                    Thread.sleep(1000)
                    beforeExecuted = true
                },
                {
                    assertEquals(true, beforeExecuted)
                    countDownLatch.countDown()
                }
        )

        taskManager.start(buildPdfTask, false)
        buildPdfTask.cancelTask()

        countDownLatch.await(2, TimeUnit.SECONDS)
        assertEquals(true, buildPdfTask.isCancelled)
    }

    private fun initToolkit() {
        /* Source: stackoverflow.com/questions/11273773/javafx-2-1-toolkit-not-initialized */
        val latch = CountDownLatch(1)
        SwingUtilities.invokeLater {
            JFXPanel() // initializes JavaFX environment
            latch.countDown()
        }
        latch.await(2, TimeUnit.SECONDS)
    }
}