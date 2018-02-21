package de.nihas101.imageToPdfConverter.pdf

import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfOutputStream
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.WriterProperties
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import de.nihas101.imageToPdfConverter.pdf.formatters.FullPageImageCropper.FullPageCropperFactory.createFullPageCropper
import de.nihas101.imageToPdfConverter.pdf.formatters.ImagePdfPageFormatter
import de.nihas101.imageToPdfConverter.util.Constants.NO_MARGIN
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.file.Paths

class ImagePdf internal constructor(
        private val outputStream: OutputStream,
        private var document: Document,
        private var pdf: PdfDocument,
        private val imagePdfPageFormatter: ImagePdfPageFormatter
) {
    companion object ImagePdfFactory {
        fun createPdf(
                directoryPath: String,
                imagePdfPageFormatter: ImagePdfPageFormatter = createFullPageCropper(),
                outputStream: OutputStream = createFileOutputStream(directoryPath),
                pdfWriterOptions: PdfWriterOptions = PdfWriterOptions.createOptions()
        ): ImagePdf {
            val writerProperties = WriterProperties()
            writerProperties.setPdfVersion(pdfWriterOptions.pdfVersion)
            writerProperties.setCompressionLevel(pdfWriterOptions.compressionLevel)
            val pdf = PdfDocument(PdfWriter(outputStream, writerProperties))
            val document = Document(pdf)
            document.setMargins(NO_MARGIN, NO_MARGIN, NO_MARGIN, NO_MARGIN)
            return ImagePdf(outputStream, document, pdf, imagePdfPageFormatter)
        }

        private fun createFileOutputStream(pathName: String): OutputStream {
            val file = Paths.get(pathName).toFile()
            return PdfOutputStream(FileOutputStream(file.toString()))
        }
    }

    fun add(image: Image) {
        prepareForNewImage()

        document.add(image)
        imagePdfPageFormatter.format(pdf.lastPage, image)

        /* TODO: Close document, so images are hopefully flushed, then reopen the document or something */
    }

    private fun prepareForNewImage() {
        if (pdf.numberOfPages > 0) {
            pdf.addNewPage()
            flush()
        } else pdf.addNewPage()
    }

    fun flush() {
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
    }
}