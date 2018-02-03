package de.nihas101.imagesToPdfConverter.pdf

import de.nihas101.imagesToPdfConverter.ProgressUpdater
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator
import de.nihas101.imagesToPdfConverter.fileReader.ImageDirectoriesIterator.ImageDirectoriesIteratorFactory.createImageDirectoriesIterator
import de.nihas101.imagesToPdfConverter.fileReader.ImageFilesIterator
import de.nihas101.imagesToPdfConverter.fileReader.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesLoader
import de.nihas101.imagesToPdfConverter.pdf.ImagePdfBuilder.PdfBuilderFactory.createPdfImageBuilder
import java.io.File
import java.nio.file.Paths

class ImageDirectoriesPdfBuilder: PdfBuilder() {
    companion object PdfBuilderFactory {
        fun createPdfBuilderFactory() = ImageDirectoriesPdfBuilder()
    }

    override fun build(directoryIterator: DirectoryIterator, saveFile: File){
        val nrOfFiles = directoryIterator.nrOfFiles()
        if(nrOfFiles == 0) return

        for (i in 0 until nrOfFiles)
            buildNextPDF(createImageFilesLoader(directoryIterator.nextFile()), saveFile)
    }

    override fun build(directoryIterator: DirectoryIterator, saveFile: File, progressUpdater: ProgressUpdater){
        val nrOfFiles = directoryIterator.nrOfFiles()
        if(nrOfFiles == 0) return
        
        /* TODO: Maybe build them in multiple Threads? */

        for (i in 0 until nrOfFiles){
            buildNextPDF(createImageFilesLoader(directoryIterator.nextFile()), saveFile)
            progressUpdater.updateProgress(nrOfFiles.toDouble() / i.toDouble())
        }
    }

    private fun buildNextPDF(directoryIterator: DirectoryIterator, saveFile: File){
        if(directoryIterator.nrOfFiles() != 0) {
            val file = Paths.get(saveFile.absolutePath + "/" + directoryIterator.getParentDirectory().name + ".pdf").toFile()
            createPdfImageBuilder().build(directoryIterator, file)
        }
    }
}