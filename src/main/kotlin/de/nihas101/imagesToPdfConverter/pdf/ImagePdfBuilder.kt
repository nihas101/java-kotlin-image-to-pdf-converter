package de.nihas101.imagesToPdfConverter.pdf

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import de.nihas101.imagesToPdfConverter.ProgressUpdater
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator
import java.io.File

class ImagePdfBuilder: PdfBuilder() {
    companion object PdfBuilderFactory{
        fun createPdfImageBuilder() = ImagePdfBuilder()
    }

    override fun build(directoryIterator: DirectoryIterator, saveFile: File){
        directoryIterator.resetIndex()
        val imagePdf = ImagePdf.createPdf(saveFile.absolutePath)
        val nrOfFiles = directoryIterator.nrOfFiles()

        for (i in 0 until nrOfFiles)
            addNextFileToPDF(directoryIterator,imagePdf)

        imagePdf.close()
    }

    override fun build(directoryIterator: DirectoryIterator, saveFile: File, progressUpdater: ProgressUpdater){
        directoryIterator.resetIndex()
        val imagePdf = ImagePdf.createPdf(saveFile.absolutePath)
        val nrOfFiles = directoryIterator.nrOfFiles()

        for (i in 0 until nrOfFiles){
            addNextFileToPDF(directoryIterator, imagePdf)
            progressUpdater.updateProgress(i)
        }

        imagePdf.close()
    }

    private fun addNextFileToPDF(directoryIterator: DirectoryIterator, imagePdf: ImagePdf){
        val filePath = directoryIterator.nextFile().absolutePath
        val image = Image(ImageDataFactory.create(filePath))
        imagePdf.add(image)
    }
}