package de.nihas101.imageToPdfConverter.pdf.pdfOptions

import com.itextpdf.kernel.pdf.CompressionConstants
import com.itextpdf.kernel.pdf.PdfVersion
import java.io.File

data class PdfOptions(
        val compressionLevel: Int = CompressionConstants.DEFAULT_COMPRESSION,
        val pdfVersion: PdfVersion = PdfVersion.PDF_1_7,
        val saveLocation: File? = null
)