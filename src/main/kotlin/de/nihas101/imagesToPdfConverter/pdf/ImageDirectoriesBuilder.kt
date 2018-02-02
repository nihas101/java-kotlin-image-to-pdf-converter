package de.nihas101.imagesToPdfConverter.pdf

import de.nihas101.imagesToPdfConverter.ProgressUpdater
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator
import de.nihas101.imagesToPdfConverter.fileReader.ImageDirectoriesIterator
import de.nihas101.imagesToPdfConverter.pdf.ImagePdfBuilder.PdfBuilderFactory.createPdfImageBuilder

class ImageDirectoriesBuilder {
    companion object PdfBuilderFactory {
        fun createPdfBuilderFactory() = ImagePdfBuilder()
    }

    fun build(imageDirectoriesIterator: ImageDirectoriesIterator){
        val nrOfFiles = imageDirectoriesIterator.nrOfFiles()

        for (i in 0 until nrOfFiles)
            buildNextPDF(imageDirectoriesIterator.nextImageFilesIterator())
    }

    fun build(imageDirectoriesIterator: ImageDirectoriesIterator, progressUpdater: ProgressUpdater){
        val nrOfFiles = imageDirectoriesIterator.nrOfFiles()

        for (i in 0 until nrOfFiles){
            buildNextPDF(imageDirectoriesIterator.nextImageFilesIterator())
            progressUpdater.updateProgress(nrOfFiles.toDouble() / i.toDouble())
        }
    }

    private fun buildNextPDF(directoryIterator: DirectoryIterator){
        if(directoryIterator.nrOfFiles() != 0)
            createPdfImageBuilder().build(directoryIterator, directoryIterator.getParentDirectory())
    }
}