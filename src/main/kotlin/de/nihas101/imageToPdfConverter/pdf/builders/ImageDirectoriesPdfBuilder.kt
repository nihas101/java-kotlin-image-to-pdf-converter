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

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import de.nihas101.imageToPdfConverter.pdf.builders.ImagePdfBuilder.ImagePdfBuilderFactory.createImagePdfBuilder
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.io.File
import java.nio.file.Paths

class ImageDirectoriesPdfBuilder : PdfBuilder() {
    var imagePdfBuilder: ImagePdfBuilder? = null

    override fun build(directoryIterator: DirectoryIterator, imageToPdfOptions: ImageToPdfOptions, progressUpdater: ProgressUpdater) {
        directoryIterator.resetIndex()
        val nrOfFiles = directoryIterator.numberOfFiles()
        if (nrOfFiles == 0) return

        for (i in 1..nrOfFiles) {
            if (cancelled) throw InterruptedException()
            val directory = directoryIterator.nextFile()
            buildNextPDF(createImageFilesIterator(), directory, imageToPdfOptions)
            progressUpdater.updateProgress(i.toDouble() / nrOfFiles.toDouble(), directory)
        }
    }

    private fun buildNextPDF(directoryIterator: DirectoryIterator, directory: File, imageToPdfOptions: ImageToPdfOptions) {
        directoryIterator.setupDirectory(directory)

        if (directoryIterator.numberOfFiles() != 0) {
            val file = Paths.get(
                    imageToPdfOptions.getPdfOptions().saveLocation!!.absolutePath + "/" + directoryIterator.getParentDirectory().name + ".pdf"
            ).toFile()

            val nextImageToPdfOptions = imageToPdfOptions.copy()
            nextImageToPdfOptions.setSaveLocation(file)

            imagePdfBuilder = createImagePdfBuilder()
            imagePdfBuilder!!.build(directoryIterator, nextImageToPdfOptions)
        }
    }

    override fun cancelTask() {
        super.cancelTask()
        imagePdfBuilder!!.cancelTask()
    }

    companion object PdfBuilderFactory {
        fun createImageDirectoriesPdfBuilder() = ImageDirectoriesPdfBuilder()
    }
}