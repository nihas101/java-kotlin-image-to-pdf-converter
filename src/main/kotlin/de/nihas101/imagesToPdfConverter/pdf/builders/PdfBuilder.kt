package de.nihas101.imagesToPdfConverter.pdf.builders

import de.nihas101.imagesToPdfConverter.util.ProgressUpdater
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator
import de.nihas101.imagesToPdfConverter.pdf.PdfWriterOptions
import java.io.File

abstract class PdfBuilder{
    abstract fun build(directoryIterator: DirectoryIterator, saveFile: File, pdfWriterOptions: PdfWriterOptions)
    abstract fun build(directoryIterator: DirectoryIterator, saveFile: File, pdfWriterOptions: PdfWriterOptions, progressUpdater: ProgressUpdater)
}