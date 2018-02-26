package de.nihas101.imageToPdfConverter.pdf

import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfOutputStream
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.WriterProperties
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions
import de.nihas101.imageToPdfConverter.util.Constants.NO_MARGIN
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImagePdf internal constructor(
        private val pdfWriter: PdfWriter,
        private var document: Document,
        private var pdf: PdfDocument
) {
    fun add(image: Image) {
        addNewImage(image)
        document.add(image)
        flush()
    }

    private fun addNewImage(image: Image) {
        pdf.addNewPage(PageSize(Rectangle(0F, 0F, image.imageWidth, image.imageHeight)))
    }

    private fun flush() {
        document.flush()
        if (pdf.numberOfPages > 1) pdf.getPage(pdf.numberOfPages - 1).flush(true)
        pdfWriter.flush()
        System.gc()
    }

    fun close() {
        pdf.close()
        pdfWriter.close()
        document.close()
        System.gc()
    }

    companion object ImagePdfFactory {
        fun createPdf(
                imageToPdfOptions: ImageToPdfOptions,
                outputStream: OutputStream = createFileOutputStream(imageToPdfOptions.getPdfOptions().saveLocation!!)
        ): ImagePdf {
            val writerProperties = WriterProperties()
            writerProperties.setPdfVersion(imageToPdfOptions.getPdfOptions().pdfVersion)
            writerProperties.setCompressionLevel(imageToPdfOptions.getPdfOptions().compressionLevel)

            val pdfWriter = PdfWriter(outputStream, writerProperties)
            val pdf = PdfDocument(pdfWriter)
            val document = Document(pdf, PageSize.A4, true)

            document.setMargins(NO_MARGIN, NO_MARGIN, NO_MARGIN, NO_MARGIN)
            return ImagePdf(pdfWriter, document, pdf)
        }

        private fun createFileOutputStream(file: File): OutputStream {
            return PdfOutputStream(FileOutputStream(file))
        }
    }
}