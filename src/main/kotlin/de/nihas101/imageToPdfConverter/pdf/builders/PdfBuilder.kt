package de.nihas101.imageToPdfConverter.pdf.builders

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.pdf.PdfWriterOptions
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.io.File

abstract class PdfBuilder {
    abstract fun build(directoryIterator: DirectoryIterator, saveFile: File, pdfWriterOptions: PdfWriterOptions)
    abstract fun build(directoryIterator: DirectoryIterator, saveFile: File, pdfWriterOptions: PdfWriterOptions, progressUpdater: ProgressUpdater)
}