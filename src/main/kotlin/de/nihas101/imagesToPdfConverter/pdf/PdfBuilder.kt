package de.nihas101.imagesToPdfConverter.pdf

import de.nihas101.imagesToPdfConverter.ProgressUpdater
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator
import java.io.File

abstract class PdfBuilder{
    abstract fun build(directoryIterator: DirectoryIterator, saveFile: File, pdfWriterOptions: PdfWriterOptions)
    abstract fun build(directoryIterator: DirectoryIterator, saveFile: File, pdfWriterOptions: PdfWriterOptions, progressUpdater: ProgressUpdater)
}