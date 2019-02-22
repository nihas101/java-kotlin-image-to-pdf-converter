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

import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator
import de.nihas101.image_to_pdf_converter.directory_iterators.image_iterators.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import de.nihas101.image_to_pdf_converter.pdf.builders.ImagePdfBuilder.ImagePdfBuilderFactory.createImagePdfBuilder
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.ImageToPdfOptions
import de.nihas101.image_to_pdf_converter.util.ProgressUpdater
import de.nihas101.image_to_pdf_converter.util.TrivialProgressUpdater
import java.io.File
import java.nio.file.Paths

class ImageDirectoriesPdfBuilder : PdfBuilder() {
    private var imagePdfBuilder: ImagePdfBuilder? = null

    override fun build(directoryIterator: DirectoryIterator, imageToPdfOptions: ImageToPdfOptions, progressUpdater: ProgressUpdater): Boolean {
        directoryIterator.resetIndex()
        val nrOfFiles = directoryIterator.numberOfFiles()
        if (nrOfFiles == 0) return true

        for (i in 1..nrOfFiles) {
            if (cancelled) throw InterruptedException()
            val directory = directoryIterator.nextFile()
            progressUpdater.updateProgress(i.toDouble() / nrOfFiles.toDouble(), directory)
            val wasSuccess = buildNextPDF(createImageFilesIterator(imageToPdfOptions.getIteratorOptions()), directory, imageToPdfOptions)
            if (!wasSuccess) return false
        }

        return true
    }

    private fun buildNextPDF(directoryIterator: DirectoryIterator, directory: File, imageToPdfOptions: ImageToPdfOptions): Boolean {
        directoryIterator.addDirectory(directory, TrivialProgressUpdater())

        if (directoryIterator.numberOfFiles() != 0) {
            val file = Paths.get(
                    imageToPdfOptions.getPdfOptions().saveLocation!!.absolutePath + "/" + directoryIterator.getParentDirectory().name + ".pdf"
            ).toFile()

            val nextImageToPdfOptions = imageToPdfOptions.copy()
            nextImageToPdfOptions.setSaveLocation(file)

            imagePdfBuilder = createImagePdfBuilder()
            return imagePdfBuilder!!.build(directoryIterator, nextImageToPdfOptions)
        }
        return false
    }

    override fun cancelTask() {
        super.cancelTask()
        if (imagePdfBuilder != null) imagePdfBuilder!!.cancelTask()
    }

    companion object PdfBuilderFactory {
        fun createImageDirectoriesPdfBuilder() = ImageDirectoriesPdfBuilder()
    }
}