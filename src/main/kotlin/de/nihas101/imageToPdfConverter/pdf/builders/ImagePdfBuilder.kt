package de.nihas101.imageToPdfConverter.pdf.builders

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.pdf.ImagePdf
import de.nihas101.imageToPdfConverter.pdf.ImagePdf.ImagePdfFactory.createPdf
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.io.File

class ImagePdfBuilder : PdfBuilder() {
    companion object ImagePdfBuilderFactory {
        fun createImagePdfBuilder() = ImagePdfBuilder()
    }

    override fun build(directoryIterator: DirectoryIterator, imageToPdfOptions: ImageToPdfOptions, progressUpdater: ProgressUpdater) {
        directoryIterator.resetIndex()
        val imagePdf = createPdf(imageToPdfOptions)
        val nrOfFiles = directoryIterator.numberOfFiles()

        try {
            for (i in 1..nrOfFiles) {
                if (cancelled) throw InterruptedException()
                val file = directoryIterator.nextFile()
                addNextFileToPDF(file, imagePdf)
                progressUpdater.updateProgress(i.toDouble() / nrOfFiles.toDouble(), file)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        } finally {
            imagePdf.close()
        }
    }

    private fun addNextFileToPDF(file: File, imagePdf: ImagePdf) {
        val fileURL = file.toURI().toURL()
        imagePdf.add(Image(ImageDataFactory.create(fileURL)))
        System.gc()
    }
}