package de.nihas101.imageToPdfConverter.pdf

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator
import de.nihas101.imageToPdfConverter.pdf.PdfWriterOptions.OptionsFactory.createOptions
import java.io.File

data class PdfBuildInformation(
        var sourceFile: File? = null,
        private var pdfWriterOptions: PdfWriterOptions = createOptions(),
        private var directoryIterator: DirectoryIterator? = null,
        var customTargetFile: Boolean = false
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

    fun setTargetFile(targetFile: File) {
        pdfWriterOptions = pdfWriterOptions.copy(saveLocation = targetFile)
    }

    fun getMultipleDirectories() = pdfWriterOptions.multipleDirectories

    fun getPdfWriterOptions() = pdfWriterOptions

    fun getDirectoryIterator() = directoryIterator!!

    fun getTargetFile() = pdfWriterOptions.saveLocation!!
}