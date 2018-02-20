package de.nihas101.imageToPdfConverter.pdf.formatters

import com.itextpdf.kernel.pdf.PdfPage
import com.itextpdf.layout.element.Image

abstract class ImagePdfPageFormatter {
    abstract fun format(pdfPage: PdfPage, image: Image)
}