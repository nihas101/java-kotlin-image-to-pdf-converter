package de.nihas101.imageToPdfConverter.pdf

import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfOutputStream
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.WriterProperties
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import de.nihas101.imageToPdfConverter.util.Constants.NO_MARGIN
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImagePdf internal constructor(
        private val outputStream: OutputStream,
        private var document: Document,
        private var pdf: PdfDocument
) {
    fun add(image: Image) {
        prepareForNewImage(image)
        document.add(image)
    }

    private fun prepareForNewImage(image: Image) {
        pdf.addNewPage(PageSize(Rectangle(0F, 0F, image.imageWidth, image.imageHeight)))
        if (pdf.numberOfPages > 1) flush()
    }

    private fun flush() {
        document.flush()
        outputStream.flush()
        System.gc()
    }

    fun close() {
        document.flush()
        outputStream.flush()
        document.close()
        pdf.close()
        outputStream.close()
        System.gc()
    }

    companion object ImagePdfFactory {
        fun createPdf(
                imageToPdfOptions: ImageToPdfOptions,
                fileOutputStream: OutputStream = createFileOutputStream(imageToPdfOptions.pdfOptions.saveLocation!!)
        ): ImagePdf {
            val writerProperties = WriterProperties()
            writerProperties.setPdfVersion(imageToPdfOptions.pdfOptions.pdfVersion)
            writerProperties.setCompressionLevel(imageToPdfOptions.pdfOptions.compressionLevel)

            val pdf = PdfDocument(PdfWriter(fileOutputStream, writerProperties))
            val document = Document(pdf, PageSize.A4, true)

            document.setMargins(NO_MARGIN, NO_MARGIN, NO_MARGIN, NO_MARGIN)
            return ImagePdf(fileOutputStream, document, pdf)
        }

        private fun createFileOutputStream(file: File): OutputStream {
            return PdfOutputStream(FileOutputStream(file))
        }
    }
}