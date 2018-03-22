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

package de.nihas101.imageToPdfConverter.pdf.builders

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.pdf.ImagePdf
import de.nihas101.imageToPdfConverter.pdf.ImagePdf.ImagePdfFactory.createPdf
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.io.File

class ImagePdfBuilder : PdfBuilder() {
    companion object ImagePdfBuilderFactory {
        fun createImagePdfBuilder() = ImagePdfBuilder()
    }

    override fun build(directoryIterator: DirectoryIterator, imageToPdfOptions: ImageToPdfOptions, progressUpdater: ProgressUpdater) {
        directoryIterator.resetIndex()
        val imagePdf = createPdf(imageToPdfOptions)
        val nrOfFiles = directoryIterator.numberOfFiles()

        try {
            for (i in 1..nrOfFiles) {
                if (cancelled) throw InterruptedException()
                val file = directoryIterator.nextFile()
                progressUpdater.updateProgress(i.toDouble() / nrOfFiles.toDouble(), file)
                addNextFileToPDF(file, imagePdf)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        } finally {
            imagePdf.close()
        }
    }

    private fun addNextFileToPDF(file: File, imagePdf: ImagePdf) {
        val fileURL = file.toURI().toURL()
        imagePdf.add(Image(ImageDataFactory.create(fileURL)))
        System.gc()
    }
}