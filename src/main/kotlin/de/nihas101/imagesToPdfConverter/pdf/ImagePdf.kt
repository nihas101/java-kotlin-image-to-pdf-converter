package de.nihas101.imagesToPdfConverter.pdf

import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.WriterProperties
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import de.nihas101.imagesToPdfConverter.Constants.NO_MARGIN
import de.nihas101.imagesToPdfConverter.pdf.FullPageImageCropper.FullPageCropperFactory.createFullPageCropper
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.file.Paths
import java.text.Normalizer

class ImagePdf internal constructor(private val document: Document, private val pdf: PdfDocument, private val imagePdfPageFormatter: ImagePdfPageFormatter){
    companion object ImagePdfFactory {
        fun createPdf(
                pathName: String,
                imagePdfPageFormatter: ImagePdfPageFormatter = createFullPageCropper(),
                outputStream: OutputStream = createFileOutputStream(pathName)
        ): ImagePdf {
            val writerProperties = WriterProperties()
            /* TODO: Set properties like compression level and PDFVersion here */
            val pdf = PdfDocument(PdfWriter(outputStream, writerProperties))
            val document = Document(pdf)
            document.setMargins(NO_MARGIN, NO_MARGIN, NO_MARGIN, NO_MARGIN)
            return ImagePdf(document, pdf, imagePdfPageFormatter)
        }

        private fun createFileOutputStream(pathName: String): OutputStream {
            val file = Paths.get(normalize(pathName)).toFile()
            return FileOutputStream(file.toString())
        }

        private fun normalize(pathName: String): String = Normalizer.normalize(pathName, Normalizer.Form.NFD)
    }

    fun add(image: Image) {
        document.add(image)
        imagePdfPageFormatter.format(pdf.lastPage, image)
    }

    fun close() = document.close()
}