package de.nihas101.imagesToPdfConverter.pdf

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import de.nihas101.imagesToPdfConverter.ProgressUpdater
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator
import java.io.File

class ImagePdfBuilder {
    companion object PdfBuilderFactory{
        fun createPdfImageBuilder() = ImagePdfBuilder()
    }

    fun build(directoryIterator: DirectoryIterator, saveFile: File){
        val imagePdf = ImagePdf.createPdf(saveFile.absolutePath)
        val nrOfFiles = directoryIterator.nrOfFiles()

        for (i in 0 until nrOfFiles)
            addNextFileToPDF(directoryIterator,imagePdf)

        imagePdf.close()
    }

    fun build(directoryIterator: DirectoryIterator, saveFile: File, progressUpdater: ProgressUpdater){
        val imagePdf = ImagePdf.createPdf(saveFile.absolutePath)
        val nrOfFiles = directoryIterator.nrOfFiles()

        for (i in 0 until nrOfFiles){
            addNextFileToPDF(directoryIterator, imagePdf)
            progressUpdater.updateProgress(nrOfFiles.toDouble() / i.toDouble())
        }

        imagePdf.close()
    }

    private fun addNextFileToPDF(directoryIterator: DirectoryIterator, imagePdf: ImagePdf){
        val filePath = directoryIterator.nextFile().absolutePath
        val image = Image(ImageDataFactory.create(filePath))
        imagePdf.add(image)
    }
}