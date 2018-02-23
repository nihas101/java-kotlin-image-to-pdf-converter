package de.nihas101.imageToPdfConverter.pdf

import com.itextpdf.kernel.pdf.CompressionConstants.DEFAULT_COMPRESSION
import com.itextpdf.kernel.pdf.PdfVersion
import com.itextpdf.kernel.pdf.PdfVersion.PDF_1_7
import java.io.File

data class ImageToPdfOptions(val iteratorOptions: IteratorOptions, val pdfOptions: PdfOptions) {
    companion object OptionsFactory {
        fun createOptions(
                multipleDirectories: Boolean = false,
                zipFiles: Boolean = false,
                compressionLevel: Int = DEFAULT_COMPRESSION,
                pdfVersion: PdfVersion = PDF_1_7,
                saveLocation: File? = null
        ): ImageToPdfOptions {
            val iteratorOptions = IteratorOptions(multipleDirectories, zipFiles)
            val pdfOptions1 = PdfOptions(compressionLevel, pdfVersion, saveLocation)

            return ImageToPdfOptions(iteratorOptions, pdfOptions1)
        }
    }
}