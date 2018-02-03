package de.nihas101.imagesToPdfConverter.pdf

import de.nihas101.imagesToPdfConverter.ProgressUpdater
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator
import java.io.File

abstract class PdfBuilder{
    abstract fun build(directoryIterator: DirectoryIterator, saveFile: File)
    abstract fun build(directoryIterator: DirectoryIterator, saveFile: File, progressUpdater: ProgressUpdater)
}