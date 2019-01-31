/*
 JaKoImage2PDF is a program for converting images to PDFs with the use of iText 7
 Copyright (C) 2018  Nikita Hasert

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.nihas101.image_to_pdf_converter.pdf

import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.*
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import de.nihas101.image_to_pdf_converter.pdf.PdfPageFlushList.PdfPageFlushListFactory.createPdfPageFlushList
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.ImageToPdfOptions
import de.nihas101.image_to_pdf_converter.util.Constants.NO_MARGIN
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImagePdf internal constructor(
        private val pdfWriter: PdfWriter,
        private var document: Document,
        private var pdf: PdfDocument,
        private val pdfPageFlushList: PdfPageFlushList = createPdfPageFlushList(mutableListOf(), document, pdfWriter)
) {
    fun add(image: Image) {
        val newPage = addNewPage(image.imageWidth, image.imageHeight)
        document.add(image)
        pdfPageFlushList.add(newPage)
    }

    private fun addNewPage(width: Float, height: Float): PdfPage {
        pdf.addNewPage(PageSize(Rectangle(0F, 0F, width, height)))
        return pdf.lastPage
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

        fun createFileOutputStream(file: File): OutputStream {
            return PdfOutputStream(FileOutputStream(file))
        }
    }
}