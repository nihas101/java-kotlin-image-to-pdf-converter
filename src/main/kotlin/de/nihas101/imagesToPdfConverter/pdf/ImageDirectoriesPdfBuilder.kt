package de.nihas101.imagesToPdfConverter.pdf

import de.nihas101.imagesToPdfConverter.ProgressUpdater
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator
import de.nihas101.imagesToPdfConverter.fileReader.ImageDirectoriesIterator
import de.nihas101.imagesToPdfConverter.pdf.ImagePdfBuilder.PdfBuilderFactory.createPdfImageBuilder
import java.io.File
import java.nio.file.Paths

class ImageDirectoriesPdfBuilder {
    companion object PdfBuilderFactory {
        fun createPdfBuilderFactory() = ImageDirectoriesPdfBuilder()
    }

    fun build(imageDirectoriesIterator: ImageDirectoriesIterator){
        val nrOfFiles = imageDirectoriesIterator.nrOfFiles()

        for (i in 0 until nrOfFiles)
            buildNextPDF(imageDirectoriesIterator.nextImageFilesIterator())
    }

    fun build(imageDirectoriesIterator: ImageDirectoriesIterator, progressUpdater: ProgressUpdater){
        val nrOfFiles = imageDirectoriesIterator.getFiles().size
        if(nrOfFiles == 0) return

        /* TODO: Maybe build them in multiple Threads? */

        for (i in 0 until nrOfFiles){
            buildNextPDF(imageDirectoriesIterator.nextImageFilesIterator())
            progressUpdater.updateProgress(nrOfFiles.toDouble() / i.toDouble())
        }
    }

    private fun buildNextPDF(directoryIterator: DirectoryIterator){
        if(directoryIterator.nrOfFiles() != 0) {
            val file = Paths.get(directoryIterator.getParentDirectory().absolutePath + ".pdf").toFile()
            createPdfImageBuilder().build(directoryIterator, file)
        }
    }
}