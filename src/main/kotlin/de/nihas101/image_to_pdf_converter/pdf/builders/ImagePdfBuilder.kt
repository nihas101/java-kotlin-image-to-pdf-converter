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

package de.nihas101.image_to_pdf_converter.pdf.builders

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator
import de.nihas101.image_to_pdf_converter.pdf.ImagePdf
import de.nihas101.image_to_pdf_converter.pdf.ImagePdf.ImagePdfFactory.createPdf
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.ImageToPdfOptions
import de.nihas101.image_to_pdf_converter.util.JaKoLogger.createLogger
import de.nihas101.image_to_pdf_converter.util.ProgressUpdater
import java.io.File

class ImagePdfBuilder : PdfBuilder() {
    companion object ImagePdfBuilderFactory {
        private val logger = createLogger(ImagePdfBuilder::class.java)
        fun createImagePdfBuilder() = ImagePdfBuilder()
    }

    override fun build(directoryIterator: DirectoryIterator, imageToPdfOptions: ImageToPdfOptions, progressUpdater: ProgressUpdater) : Boolean {
        directoryIterator.resetIndex()

        val imagePdf = if (imageToPdfOptions.getPdfOptions().useCustomLocation) createPdf(imageToPdfOptions)
        else createPdf(
                imageToPdfOptions,
                ImagePdf.createFileOutputStream(createFileAtSameLocation(directoryIterator))
        )

        val nrOfFiles = directoryIterator.numberOfFiles()
        var wasSuccess = true

        var file = directoryIterator.nextFile()
        try {
            for (i in 1..nrOfFiles) {
                if (cancelled) throw InterruptedException()
                progressUpdater.updateProgress(i.toDouble() / nrOfFiles.toDouble(), file)
                addNextFileToPDF(file, imagePdf)
                file = directoryIterator.nextFile()
            }
        } catch (exception: Exception) {
            logException(file, exception)
            wasSuccess = false
        } finally {
            imagePdf.close()
            return wasSuccess
        }
    }

    private fun logException(file: File, exception: Exception) {
        val args = Array<Any>(2) {}
        args[0] = file.absolutePath
        args[1] = exception

        logger.error("Exception caused by: {}\n{}", args)
    }

    private fun createFileAtSameLocation(directoryIterator: DirectoryIterator): File {
        return File("${directoryIterator.getParentDirectory().parent}/${directoryIterator.getParentDirectory().name}.pdf")
    }

    private fun addNextFileToPDF(file: File, imagePdf: ImagePdf) {
        val fileURL = file.toURI().toURL()
        imagePdf.add(Image(ImageDataFactory.create(fileURL)))
        System.gc()
    }
}