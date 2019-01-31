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

package de.nihas101.image_to_pdf_converter.ui.command_line_io

import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator
import de.nihas101.image_to_pdf_converter.directory_iterators.iterator_action.IteratorAction
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.PdfBuildInformation
import java.io.PrintStream

class PdfBuilderCommandLineOutput private constructor(private val printStream: PrintStream) {
    fun printInfo() {
        printStream.println("Follow the instructions to build the PDF(s). To exit the program, simply type \"exit\".\n")
    }

    fun printReadPathInstructions() = printMessage("Please supply a path to a source-directory or file from which to build the PDF(s)")

    fun printBuildInstructions() = printQuestion("Do you want to build multiple PDFs from this source?")

    fun printZipFilesInstructions(multipleDirectories: Boolean) {
        if (multipleDirectories) printQuestion("Do you want to build the PDFs from ZIP-files?")
        else printQuestion("Do you want to build the PDF from a ZIP-file?")
    }

    fun printSetupIteratorInformation() = printStream.println("Preparing files... (This may take a while)")

    fun printPdfModificationInstructions(pdfBuildInformation: PdfBuildInformation) {
        printStream.println("Instructions:")
        IteratorAction.getInstructions().forEach({ instruction -> printStream.println(instruction) })
        printStream.println()
        printPdfContent(pdfBuildInformation)
        printMessage("")
    }

    private fun printPdfContent(pdfBuildInformation: PdfBuildInformation) {
        val directoryIterator: DirectoryIterator = pdfBuildInformation.getDirectoryIterator()

        for (index in 0 until directoryIterator.numberOfFiles())
            printStream.println("$index: ${directoryIterator.getFile(index).name}")
    }

    fun printBuildInfo(pdfBuildInformation: PdfBuildInformation) =
            if (pdfBuildInformation.getImageToPdfOptions().getIteratorOptions().multipleDirectories)
                printStream.println(
                        "Building PDFs of version ${pdfBuildInformation.getImageToPdfOptions().getPdfOptions().pdfVersion}"
                )
            else
                printStream.println(
                        "Building PDF of version ${pdfBuildInformation.getImageToPdfOptions().getPdfOptions().pdfVersion}"
                )

    fun printProgress() = printStream.print(".")

    fun printFinishedBuilding() = printStream.println("\nFinished Building.")

    fun printInvalidPath() = printStream.println("The given path is invalid\n")

    fun printEmptyDirectoryError() = printStream.println("The given directory contains no images or doesn't exist\n")

    fun printCustomTargetFile() {
        printQuestion("Do you want to save the PDF at a different location than the source files?")
    }

    private fun printQuestion(message: String) = printMessage("$message (yes/no)")

    private fun printMessage(message: String) = printStream.print("$message\n> ")

    companion object CommandLineOutputFactory {
        fun createCommandLineOutput(outputStream: PrintStream) = PdfBuilderCommandLineOutput(outputStream)
    }
}