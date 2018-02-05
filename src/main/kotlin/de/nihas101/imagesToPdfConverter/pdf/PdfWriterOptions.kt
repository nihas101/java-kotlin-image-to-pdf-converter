package de.nihas101.imagesToPdfConverter.pdf

import com.itextpdf.kernel.pdf.CompressionConstants.DEFAULT_COMPRESSION
import com.itextpdf.kernel.pdf.PdfVersion
import com.itextpdf.kernel.pdf.PdfVersion.PDF_1_7

data class PdfWriterOptions(val multipleDirectories: Boolean, val compressionLevel: Int, val pdfVersion: PdfVersion){
    companion object OptionsFactory{
        fun createOptions(multipleDirectories: Boolean = false, compressionLevel: Int = DEFAULT_COMPRESSION, pdfVersion: PdfVersion = PDF_1_7): PdfWriterOptions =
                PdfWriterOptions(multipleDirectories, compressionLevel, pdfVersion)
    }
}