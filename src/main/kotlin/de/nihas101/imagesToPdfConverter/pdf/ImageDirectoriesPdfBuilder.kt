package de.nihas101.imagesToPdfConverter.pdf

import de.nihas101.imagesToPdfConverter.ProgressUpdater
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator
import de.nihas101.imagesToPdfConverter.fileReader.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import de.nihas101.imagesToPdfConverter.pdf.ImagePdfBuilder.PdfBuilderFactory.createPdfImageBuilder
import java.io.File
import java.nio.file.Paths

class ImageDirectoriesPdfBuilder: PdfBuilder() {
    companion object PdfBuilderFactory {
        fun createPdfBuilderFactory() = ImageDirectoriesPdfBuilder()
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
            createPdfImageBuilder().build(directoryIterator, file, pdfWriterOptions = pdfWriterOptions)
        }
    }
}