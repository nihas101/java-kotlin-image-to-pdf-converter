package de.nihas101.imageToPdfConverter.pdf.pdfOptions

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions.OptionsFactory.createOptions
import java.io.File

data class PdfBuildInformation(
        var sourceFile: File? = null,
        private var imageToPdfOptions: ImageToPdfOptions = createOptions(),
        private var directoryIterator: DirectoryIterator? = null,
        var customTargetFile: Boolean = false
) {

    fun setupDirectoryIterator() {
        directoryIterator = if (!imageToPdfOptions.getIteratorOptions().multipleDirectories)
            ImageFilesIterator.createImageFilesIterator(sourceFile!!)
        else
            ImageDirectoriesIterator.createImageDirectoriesIterator(sourceFile!!)
    }

    fun setMultipleDirectories(multipleDirectories: Boolean) {
        imageToPdfOptions = imageToPdfOptions.copy(iteratorOptions = imageToPdfOptions.getIteratorOptions().copy(multipleDirectories = multipleDirectories))
    }

    fun setTargetFile(targetFile: File) {
        imageToPdfOptions = imageToPdfOptions.copy(pdfOptions = imageToPdfOptions.getPdfOptions().copy(saveLocation = targetFile))
    }

    fun getMultipleDirectories() = imageToPdfOptions.getIteratorOptions().multipleDirectories

    fun getPdfWriterOptions() = imageToPdfOptions

    fun getDirectoryIterator() = directoryIterator!!

    fun getTargetFile() = imageToPdfOptions.getPdfOptions().saveLocation!!
}