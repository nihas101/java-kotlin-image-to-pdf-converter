package de.nihas101.imagesToPdfConverter.pdf

import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator

abstract class PdfBuilder{
    abstract fun build(directoryIterator: DirectoryIterator)
}