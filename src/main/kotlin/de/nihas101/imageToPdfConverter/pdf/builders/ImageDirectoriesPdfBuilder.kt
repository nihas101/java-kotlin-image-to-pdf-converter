package de.nihas101.imageToPdfConverter.pdf.builders

import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import de.nihas101.imageToPdfConverter.pdf.builders.ImagePdfBuilder.ImagePdfBuilderFactory.createImagePdfBuilder
import de.nihas101.imageToPdfConverter.pdf.PdfWriterOptions
import java.io.File
import java.nio.file.Paths

class ImageDirectoriesPdfBuilder: PdfBuilder() {
    companion object PdfBuilderFactory {
        fun createImageDirectoriesPdfBuilder() = ImageDirectoriesPdfBuilder()
    }

    override fun build(directoryIterator: DirectoryIterator, saveFile: File, pdfWriterOptions: PdfWriterOptions){
        directoryIterator.resetIndex()
        val nrOfFiles = directoryIterator.nrOfFiles()
        if(nrOfFiles == 0) return

        for (i in 1..nrOfFiles)
            buildNextPDF(createImageFilesIterator(directoryIterator.nextFile()), saveFile, pdfWriterOptions)
    }

    override fun build(directoryIterator: DirectoryIterator, saveFile: File, pdfWriterOptions: PdfWriterOptions, progressUpdater: ProgressUpdater){
        directoryIterator.resetIndex()
        val nrOfFiles = directoryIterator.nrOfFiles()
        if(nrOfFiles == 0) return

        for (i in 1..nrOfFiles){
            buildNextPDF(createImageFilesIterator(directoryIterator.nextFile()), saveFile, pdfWriterOptions)
            progressUpdater.updateProgress(i.toDouble()/nrOfFiles.toDouble())
        }
    }

    private fun buildNextPDF(directoryIterator: DirectoryIterator, saveFile: File, pdfWriterOptions: PdfWriterOptions){
        if(directoryIterator.nrOfFiles() != 0) {
            val file = Paths.get(saveFile.absolutePath + "/" + directoryIterator.getParentDirectory().name + ".pdf").toFile()
            createImagePdfBuilder().build(directoryIterator, file, pdfWriterOptions = pdfWriterOptions)
        }
    }
}