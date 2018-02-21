package de.nihas101.imageToPdfConverter.pdf.builders

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import de.nihas101.imageToPdfConverter.pdf.PdfWriterOptions
import de.nihas101.imageToPdfConverter.pdf.builders.ImagePdfBuilder.ImagePdfBuilderFactory.createImagePdfBuilder
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.nio.file.Paths

class ImageDirectoriesPdfBuilder : PdfBuilder() {
    companion object PdfBuilderFactory {
        fun createImageDirectoriesPdfBuilder() = ImageDirectoriesPdfBuilder()
    }

    override fun build(directoryIterator: DirectoryIterator, pdfWriterOptions: PdfWriterOptions) {
        directoryIterator.resetIndex()
        val nrOfFiles = directoryIterator.nrOfFiles()
        if (nrOfFiles == 0) return

        for (i in 1..nrOfFiles)
            buildNextPDF(createImageFilesIterator(directoryIterator.nextFile()), pdfWriterOptions)
    }

    override fun build(directoryIterator: DirectoryIterator, pdfWriterOptions: PdfWriterOptions, progressUpdater: ProgressUpdater) {
        directoryIterator.resetIndex()
        val nrOfFiles = directoryIterator.nrOfFiles()
        if (nrOfFiles == 0) return

        for (i in 1..nrOfFiles) {
            buildNextPDF(createImageFilesIterator(directoryIterator.nextFile()), pdfWriterOptions)
            progressUpdater.updateProgress(i.toDouble() / nrOfFiles.toDouble())
        }
    }

    private fun buildNextPDF(directoryIterator: DirectoryIterator, pdfWriterOptions: PdfWriterOptions) {
        if (directoryIterator.nrOfFiles() != 0) {
            val pdfWriterOptionsInstance = pdfWriterOptions.copy(
                    saveLocation = Paths.get(pdfWriterOptions.saveLocation!!.absolutePath + "/" + directoryIterator.getParentDirectory().name + ".pdf").toFile()
            )
            createImagePdfBuilder().build(directoryIterator, pdfWriterOptionsInstance)
        }
    }
}