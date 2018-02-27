package de.nihas101.imageToPdfConverter.pdf.builders

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import de.nihas101.imageToPdfConverter.pdf.builders.ImagePdfBuilder.ImagePdfBuilderFactory.createImagePdfBuilder
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.io.File
import java.nio.file.Paths

class ImageDirectoriesPdfBuilder : PdfBuilder() {
    var imagePdfBuilder: ImagePdfBuilder? = null

    override fun build(directoryIterator: DirectoryIterator, imageToPdfOptions: ImageToPdfOptions, progressUpdater: ProgressUpdater) {
        directoryIterator.resetIndex()
        val nrOfFiles = directoryIterator.numberOfFiles()
        if (nrOfFiles == 0) return

        for (i in 1..nrOfFiles) {
            if (cancelled) throw InterruptedException()
            val directory = directoryIterator.nextFile()
            buildNextPDF(createImageFilesIterator(), directory, imageToPdfOptions)
            progressUpdater.updateProgress(i.toDouble() / nrOfFiles.toDouble(), directory)
        }
    }

    private fun buildNextPDF(directoryIterator: DirectoryIterator, directory: File, imageToPdfOptions: ImageToPdfOptions) {
        directoryIterator.setupDirectory(directory)

        if (directoryIterator.numberOfFiles() != 0) {
            val file = Paths.get(
                    imageToPdfOptions.getPdfOptions().saveLocation!!.absolutePath + "/" + directoryIterator.getParentDirectory().name + ".pdf"
            ).toFile()

            val nextImageToPdfOptions = imageToPdfOptions.copy()
            nextImageToPdfOptions.setSaveLocation(file)

            imagePdfBuilder = createImagePdfBuilder()
            imagePdfBuilder!!.build(directoryIterator, nextImageToPdfOptions)
        }
    }

    override fun cancelTask() {
        super.cancelTask()
        imagePdfBuilder!!.cancelTask()
    }

    companion object PdfBuilderFactory {
        fun createImageDirectoriesPdfBuilder() = ImageDirectoriesPdfBuilder()
    }
}