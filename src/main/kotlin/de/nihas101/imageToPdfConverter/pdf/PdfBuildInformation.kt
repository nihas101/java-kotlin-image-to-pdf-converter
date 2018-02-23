package de.nihas101.imageToPdfConverter.pdf

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator
import de.nihas101.imageToPdfConverter.pdf.ImageToPdfOptions.OptionsFactory.createOptions
import java.io.File

data class PdfBuildInformation(
        var sourceFile: File? = null,
        private var imageToPdfOptions: ImageToPdfOptions = createOptions(),
        private var directoryIterator: DirectoryIterator? = null,
        var customTargetFile: Boolean = false
) {

    fun setupDirectoryIterator() {
        directoryIterator = if (!imageToPdfOptions.iteratorOptions.multipleDirectories)
            ImageFilesIterator.createImageFilesIterator(sourceFile!!)
        else
            ImageDirectoriesIterator.createImageDirectoriesIterator(sourceFile!!)
    }

    fun setMultipleDirectories(multipleDirectories: Boolean) {
        imageToPdfOptions = imageToPdfOptions.copy(iteratorOptions = imageToPdfOptions.iteratorOptions.copy(multipleDirectories = multipleDirectories))
    }

    fun setTargetFile(targetFile: File) {
        imageToPdfOptions = imageToPdfOptions.copy(pdfOptions = imageToPdfOptions.pdfOptions.copy(saveLocation = targetFile))
    }

    fun getMultipleDirectories() = imageToPdfOptions.iteratorOptions.multipleDirectories

    fun getPdfWriterOptions() = imageToPdfOptions

    fun getDirectoryIterator() = directoryIterator!!

    fun getTargetFile() = imageToPdfOptions.pdfOptions.saveLocation!!
}