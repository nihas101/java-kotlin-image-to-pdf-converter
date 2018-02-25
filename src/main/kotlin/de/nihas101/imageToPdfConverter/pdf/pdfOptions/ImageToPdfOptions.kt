package de.nihas101.imageToPdfConverter.pdf.pdfOptions

import com.itextpdf.kernel.pdf.PdfVersion
import java.io.File

data class ImageToPdfOptions(
        private var iteratorOptions: IteratorOptions = IteratorOptions(),
        private var pdfOptions: PdfOptions = PdfOptions()
) {

    companion object OptionsFactory {
        fun createOptions(
                iteratorOptions: IteratorOptions = IteratorOptions(),
                pdfOptions: PdfOptions = PdfOptions()
        ): ImageToPdfOptions {
            return ImageToPdfOptions(iteratorOptions, pdfOptions)
        }
    }

    fun setMultipleDirectories(multipleDirectories: Boolean) {
        iteratorOptions = iteratorOptions.copy(multipleDirectories = multipleDirectories)
    }

    fun setZipFiles(zipFiles: Boolean) {
        iteratorOptions = iteratorOptions.copy(zipFiles = zipFiles)
    }

    fun setDeleteOnExit(deleteOnExit: Boolean) {
        iteratorOptions = iteratorOptions.copy(deleteOnExit = deleteOnExit)
    }

    fun setSaveLocation(saveLocation: File) {
        pdfOptions = pdfOptions.copy(saveLocation = saveLocation)
    }

    fun setPdfVersion(pdfVersion: PdfVersion) {
        pdfOptions = pdfOptions.copy(pdfVersion = pdfVersion)
    }

    fun setCompressionLevel(compressionLevel: Int) {
        pdfOptions = pdfOptions.copy(compressionLevel = compressionLevel)
    }

    fun getIteratorOptions() = iteratorOptions

    fun getPdfOptions() = pdfOptions

    fun copyForJava() = copy()
}