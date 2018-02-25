package de.nihas101.imageToPdfConverter.pdf.builders

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.pdf.ImageToPdfOptions
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import de.nihas101.imageToPdfConverter.util.TrivialProgressUpdate

abstract class PdfBuilder {
    abstract fun build(
            directoryIterator: DirectoryIterator,
            imageToPdfOptions: ImageToPdfOptions,
            progressUpdater: ProgressUpdater = TrivialProgressUpdate()
    )
}