package de.nihas101.imageToPdfConverter.pdf.builders

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import de.nihas101.imageToPdfConverter.tasks.Cancellable
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import de.nihas101.imageToPdfConverter.util.TrivialProgressUpdater

abstract class PdfBuilder : Cancellable {
    protected var cancelled = false

    abstract fun build(
            directoryIterator: DirectoryIterator,
            imageToPdfOptions: ImageToPdfOptions,
            progressUpdater: ProgressUpdater = TrivialProgressUpdater()
    )

    override fun cancelTask() {
        cancelled = true
    }

    companion object PdfBuilderFactory {
        fun createPdfBBuilder(iteratorOptions: IteratorOptions): PdfBuilder {
            return when (iteratorOptions.multipleDirectories) {
                true -> ImageDirectoriesPdfBuilder.createImageDirectoriesPdfBuilder()
                false -> ImagePdfBuilder.createImagePdfBuilder()
            }
        }
    }
}