package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.pdf.builders.PdfBuilder
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions
import de.nihas101.imageToPdfConverter.util.ProgressUpdater

class BuildPdfTask(
        private val pdfBuilder: PdfBuilder,
        private val directoryIterator: DirectoryIterator,
        private val imageToPdfOptions: ImageToPdfOptions,
        private val progressUpdater: ProgressUpdater,
        before: () -> Unit,
        after: () -> Unit
) : CancellableTask(before, pdfBuilder, after) {

    override fun call() {
        before()
        try {
            pdfBuilder.build(directoryIterator, imageToPdfOptions, progressUpdater)
        } catch (exception: InterruptedException) {
            /* The task was cancelled */
        }
        after()
    }

    companion object BuildPdfTaskFactory {
        fun createBuildPdfTask(
                pdfBuilder: PdfBuilder,
                directoryIterator: DirectoryIterator,
                imageToPdfOptions: ImageToPdfOptions,
                progressUpdater: ProgressUpdater,
                before: () -> Unit,
                after: () -> Unit
        ): BuildPdfTask {
            return BuildPdfTask(pdfBuilder, directoryIterator, imageToPdfOptions, progressUpdater, before, after)
        }
    }
}