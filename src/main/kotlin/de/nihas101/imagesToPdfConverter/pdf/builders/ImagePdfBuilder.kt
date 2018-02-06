package de.nihas101.imagesToPdfConverter.pdf.builders

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import de.nihas101.imagesToPdfConverter.util.ProgressUpdater
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator
import de.nihas101.imagesToPdfConverter.pdf.ImagePdf
import de.nihas101.imagesToPdfConverter.pdf.PdfWriterOptions
import java.io.File

class ImagePdfBuilder: PdfBuilder() {
    companion object ImagePdfBuilderFactory {
        fun createImagePdfBuilder() = ImagePdfBuilder()
    }

    override fun build(directoryIterator: DirectoryIterator, saveFile: File, pdfWriterOptions: PdfWriterOptions){
        directoryIterator.resetIndex()
        val imagePdf = ImagePdf.createPdf(saveFile.absolutePath, pdfWriterOptions = pdfWriterOptions)
        val nrOfFiles = directoryIterator.nrOfFiles()

        for (i in 1..nrOfFiles)
            addNextFileToPDF(directoryIterator,imagePdf)

        imagePdf.close()
    }

    override fun build(directoryIterator: DirectoryIterator, saveFile: File, pdfWriterOptions: PdfWriterOptions, progressUpdater: ProgressUpdater){
        directoryIterator.resetIndex()
        val imagePdf = ImagePdf.createPdf(saveFile.absolutePath, pdfWriterOptions = pdfWriterOptions)
        val nrOfFiles = directoryIterator.nrOfFiles()

        for (i in 1..nrOfFiles){
            addNextFileToPDF(directoryIterator, imagePdf)
            progressUpdater.updateProgress(i.toDouble()/nrOfFiles.toDouble())
        }

        imagePdf.close()
    }

    private fun addNextFileToPDF(directoryIterator: DirectoryIterator, imagePdf: ImagePdf){
        val filePath = directoryIterator.nextFile().absolutePath
        val image = Image(ImageDataFactory.create(filePath))
        imagePdf.add(image)
    }
}