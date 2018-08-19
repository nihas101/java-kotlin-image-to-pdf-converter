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

package de.nihas101.imageToPdfConverter.pdf.pdfOptions

import com.itextpdf.kernel.pdf.PdfVersion
import de.nihas101.imageToPdfConverter.util.JaKoLogger
import java.io.File

data class ImageToPdfOptions(
        private var iteratorOptions: IteratorOptions = IteratorOptions(),
        private var pdfOptions: PdfOptions = PdfOptions()
) {

    companion object OptionsFactory {
        val logger = JaKoLogger.createLogger(ImageToPdfOptions::class.java)

        fun createOptions(
                iteratorOptions: IteratorOptions = IteratorOptions(),
                pdfOptions: PdfOptions = PdfOptions()
        ): ImageToPdfOptions {
            return ImageToPdfOptions(iteratorOptions, pdfOptions)
        }
    }

    fun setMultipleDirectories(multipleDirectories: Boolean) {
        logger.info("Set multiple directories to {}", multipleDirectories)
        iteratorOptions = iteratorOptions.copy(multipleDirectories = multipleDirectories)
    }

    fun setZipFiles(zipFiles: Boolean) {
        logger.info("Set zip files to {}", zipFiles)
        iteratorOptions = iteratorOptions.copy(zipFiles = zipFiles)
    }

    fun setDeleteOnExit(deleteOnExit: Boolean) {
        logger.info("Set delete on exit to {}", deleteOnExit)
        iteratorOptions = iteratorOptions.copy(deleteOnExit = deleteOnExit)
    }

    fun setSaveLocation(saveLocation: File) {
        logger.info("Set save location to {}", saveLocation.absolutePath)
        pdfOptions = pdfOptions.copy(saveLocation = saveLocation)
    }

    fun setPdfVersion(pdfVersion: PdfVersion) {
        logger.info("Set PDF version to {}", pdfVersion)
        pdfOptions = pdfOptions.copy(pdfVersion = pdfVersion)
    }

    fun setCompressionLevel(compressionLevel: Int) {
        logger.info("Set compression level to {}", compressionLevel)
        pdfOptions = pdfOptions.copy(compressionLevel = compressionLevel)
    }

    fun setMaximalSearchDepth(maximalSearchDepth: Int) {
        iteratorOptions = iteratorOptions.copy(maximalSearchDepth = maximalSearchDepth)
    }

    fun setCustomLocation(useCustomLocation: Boolean) {
        pdfOptions = pdfOptions.copy(useCustomLocation = useCustomLocation)
    }

    fun getIteratorOptions() = iteratorOptions

    fun getPdfOptions() = pdfOptions

    fun copyForJava() = copy()
}