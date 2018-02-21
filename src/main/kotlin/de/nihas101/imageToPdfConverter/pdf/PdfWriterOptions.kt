package de.nihas101.imageToPdfConverter.pdf

import com.itextpdf.kernel.pdf.CompressionConstants.DEFAULT_COMPRESSION
import com.itextpdf.kernel.pdf.PdfVersion
import com.itextpdf.kernel.pdf.PdfVersion.PDF_1_7
import java.io.File

data class PdfWriterOptions(val multipleDirectories: Boolean, val compressionLevel: Int, val pdfVersion: PdfVersion, val saveLocation: File?) {
    companion object OptionsFactory {
        fun createOptions(multipleDirectories: Boolean = false, compressionLevel: Int = DEFAULT_COMPRESSION, pdfVersion: PdfVersion = PDF_1_7, saveLocation: File? = null): PdfWriterOptions =
                PdfWriterOptions(multipleDirectories, compressionLevel, pdfVersion, saveLocation)
    }
}