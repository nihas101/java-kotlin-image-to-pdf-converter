package de.nihas101.imageToPdfConverter.pdf.builders

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import de.nihas101.imageToPdfConverter.pdf.ImageToPdfOptions
import de.nihas101.imageToPdfConverter.pdf.builders.ImagePdfBuilder.ImagePdfBuilderFactory.createImagePdfBuilder
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.nio.file.Paths

class ImageDirectoriesPdfBuilder : PdfBuilder() {
    companion object PdfBuilderFactory {
        fun createImageDirectoriesPdfBuilder() = ImageDirectoriesPdfBuilder()
    }

    override fun build(directoryIterator: DirectoryIterator, imageToPdfOptions: ImageToPdfOptions) {
        directoryIterator.resetIndex()
        val nrOfFiles = directoryIterator.numberOfFiles()
        if (nrOfFiles == 0) return

        for (i in 1..nrOfFiles)
            buildNextPDF(createImageFilesIterator(directoryIterator.nextFile()), imageToPdfOptions)
    }

    override fun build(directoryIterator: DirectoryIterator, imageToPdfOptions: ImageToPdfOptions, progressUpdater: ProgressUpdater) {
        directoryIterator.resetIndex()
        val nrOfFiles = directoryIterator.numberOfFiles()
        if (nrOfFiles == 0) return

        for (i in 1..nrOfFiles) {
            buildNextPDF(createImageFilesIterator(directoryIterator.nextFile()), imageToPdfOptions)
            progressUpdater.updateProgress(i.toDouble() / nrOfFiles.toDouble())
        }
    }

    private fun buildNextPDF(directoryIterator: DirectoryIterator, imageToPdfOptions: ImageToPdfOptions) {
        if (directoryIterator.numberOfFiles() != 0) {
            val file = Paths.get(
                    imageToPdfOptions.pdfOptions.saveLocation!!.absolutePath + "/" + directoryIterator.getParentDirectory().name + ".pdf"
            ).toFile()

            val pdfWriterOptionsInstance =
                    imageToPdfOptions.copy(pdfOptions = imageToPdfOptions.pdfOptions.copy(saveLocation = file))
            createImagePdfBuilder().build(directoryIterator, pdfWriterOptionsInstance)
        }
    }
}