package de.nihas101.imagesToPdfConverter.pdf

import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import de.nihas101.imagesToPdfConverter.pdf.FullPageImageCropper.FullPageCropperFactory.createFullPageCropper
import java.io.File

class ImagePdf internal constructor(private val document: Document, private val pdf: PdfDocument, private val imagePdfPageFormatter: ImagePdfPageFormatter){
    companion object ImagePdfFactory {
        fun createPdf(pathName: String, imagePdfPageFormatter: ImagePdfPageFormatter = createFullPageCropper()): ImagePdf {
            val pdf = PdfDocument(PdfWriter(File(pathName)))
            val document = Document(pdf)
            document.setMargins(0F, 0F, 0F, 0F)
            return ImagePdf(document, pdf, imagePdfPageFormatter)
        }
    }

    fun add(image: Image) {
        document.add(image)
        imagePdfPageFormatter.format(pdf.lastPage, image)
    }

    fun close() = document.close()
}