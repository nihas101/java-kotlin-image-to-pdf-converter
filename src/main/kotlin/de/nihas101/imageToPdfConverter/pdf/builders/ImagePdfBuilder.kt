package de.nihas101.imageToPdfConverter.pdf.builders

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.pdf.ImagePdf
import de.nihas101.imageToPdfConverter.pdf.PdfWriterOptions
import de.nihas101.imageToPdfConverter.util.ProgressUpdater

class ImagePdfBuilder : PdfBuilder() {
    companion object ImagePdfBuilderFactory {
        fun createImagePdfBuilder() = ImagePdfBuilder()
    }

    override fun build(directoryIterator: DirectoryIterator, pdfWriterOptions: PdfWriterOptions) {
        directoryIterator.resetIndex()
        val imagePdf = ImagePdf.createPdf(pdfWriterOptions)
        val nrOfFiles = directoryIterator.numberOfFiles()

        try {
            for (i in 1..nrOfFiles) addNextFileToPDF(directoryIterator, imagePdf)
        } catch (exception: Exception) {
            exception.printStackTrace()
        } finally {
            imagePdf.close()
        }
    }

    override fun build(directoryIterator: DirectoryIterator, pdfWriterOptions: PdfWriterOptions, progressUpdater: ProgressUpdater) {
        directoryIterator.resetIndex()
        val imagePdf = ImagePdf.createPdf(pdfWriterOptions)
        val nrOfFiles = directoryIterator.numberOfFiles()

        try {
            for (i in 1..nrOfFiles) {
                addNextFileToPDF(directoryIterator, imagePdf)
                progressUpdater.updateProgress(i.toDouble() / nrOfFiles.toDouble())
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        } finally {
            imagePdf.close()
        }
    }

    private fun addNextFileToPDF(directoryIterator: DirectoryIterator, imagePdf: ImagePdf) {
        val fileURL = directoryIterator.nextFile().toURI().toURL()
        imagePdf.add(Image(ImageDataFactory.create(fileURL)))
        System.gc()
    }
}