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

package de.nihas101.image_to_pdf_converter.util


import de.nihas101.image_to_pdf_converter.pdf.pdf_options.IteratorOptions
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter

val pdfZipFilter = ExtensionFilter("PDF files (*.pdf), ZIP files (*.zip)", "*.pdf", "*.zip")
val pdfFilter = ExtensionFilter("PDF files (*.pdf)", "*pdf")

/**
 * Sets up the [DirectoryChooser] instance
 */
fun createDirectoryChooser(iteratorOptions: IteratorOptions): DirectoryChooser {
    val directoryChooser = DirectoryChooser()
    setTitle(directoryChooser, iteratorOptions)
    return directoryChooser
}

private fun setTitle(directoryChooser: DirectoryChooser, iteratorOptions: IteratorOptions) {
    when {
        iteratorOptions.multipleDirectories -> directoryChooser.title = "Choose a directory of directories to turn into a PDF"
        else -> directoryChooser.title = "Choose a directory to turn into a PDF"
    }
}

fun createZipFileChooser(): FileChooser {
    val fileChooser = FileChooser()
    fileChooser.title = "Choose a zip-directory to turn into a PDF"
    return fileChooser
}

/**
 * Sets up the [FileChooser] instance
 */
fun createSaveFileChooser(): FileChooser {
    val saveFileChooser = FileChooser()
    saveFileChooser.title = "Choose a save location for the PDF"

    return saveFileChooser
}

fun addExtensionFilters(fileChooser: FileChooser, iteratorOptions: IteratorOptions) {
    fileChooser.extensionFilters.clear()

    val extensionFilter: ExtensionFilter = if (iteratorOptions.includeZipFiles) pdfZipFilter else pdfFilter

    fileChooser.extensionFilters.add(extensionFilter)
}
