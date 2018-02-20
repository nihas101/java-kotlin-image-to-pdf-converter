package de.nihas101.imageToPdfConverter.pdf

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.ImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.ImageFilesIterator
import de.nihas101.imageToPdfConverter.pdf.PdfWriterOptions.OptionsFactory.createOptions
import java.io.File

data class PdfBuildInformation(
        var sourceFile: File? = null,
        private var pdfWriterOptions: PdfWriterOptions = createOptions(),
        private var directoryIterator: DirectoryIterator? = null,
        var customTargetFile: Boolean = false,
        var targetFile: File? = null
) {

    fun setupDirectoryIterator() {
        directoryIterator = if (!pdfWriterOptions.multipleDirectories)
            ImageFilesIterator.createImageFilesIterator(sourceFile!!)
        else
            ImageDirectoriesIterator.createImageDirectoriesIterator(sourceFile!!)
    }

    fun setMultipleDirectories(multipleDirectories: Boolean) {
        pdfWriterOptions = pdfWriterOptions.copy(multipleDirectories = multipleDirectories)
    }

    fun getMultipleDirectories() = pdfWriterOptions.multipleDirectories

    fun getPdfWriterOptions() = pdfWriterOptions

    fun getDirectoryIterator() = directoryIterator!!
}