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

package de.nihas101.image_to_pdf_converter.pdf.pdf_options

import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.ImageToPdfOptions.OptionsFactory.createOptions
import de.nihas101.image_to_pdf_converter.util.TrivialProgressUpdater
import java.io.File

data class PdfBuildInformation(
        var sourceFile: File? = null,
        private var imageToPdfOptions: ImageToPdfOptions = createOptions(),
        private var directoryIterator: DirectoryIterator? = null,
        var customTargetFile: Boolean = false
) {

    fun setupDirectoryIterator() {
        directoryIterator = DirectoryIterator.createDirectoryIterator(imageToPdfOptions.getIteratorOptions())
        directoryIterator!!.addDirectory(sourceFile!!, TrivialProgressUpdater())
    }

    fun setMultipleDirectories(multipleDirectories: Boolean) {
        imageToPdfOptions = imageToPdfOptions.copy(iteratorOptions = imageToPdfOptions.getIteratorOptions().copy(multipleDirectories = multipleDirectories))
    }

    fun setZipFiles(zipFiles: Boolean) = imageToPdfOptions.setZipFiles(zipFiles)

    fun setTargetFile(targetFile: File) {
        imageToPdfOptions = imageToPdfOptions.copy(pdfOptions = imageToPdfOptions.getPdfOptions().copy(saveLocation = targetFile))
        imageToPdfOptions.setCustomLocation(true)
    }

    fun getMultipleDirectories() = imageToPdfOptions.getIteratorOptions().multipleDirectories

    fun getImageToPdfOptions() = imageToPdfOptions

    fun getDirectoryIterator() = directoryIterator!!

    fun getTargetFile() = imageToPdfOptions.getPdfOptions().saveLocation!!
}