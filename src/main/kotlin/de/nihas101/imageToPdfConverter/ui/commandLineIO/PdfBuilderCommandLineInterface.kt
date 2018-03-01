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

package de.nihas101.imageToPdfConverter.ui.commandLineIO

import de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction.IteratorBuildAction
import de.nihas101.imageToPdfConverter.pdf.builders.ImageDirectoriesPdfBuilder
import de.nihas101.imageToPdfConverter.pdf.builders.ImagePdfBuilder
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.PdfBuildInformation
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.InvalidPathException

class PdfBuilderCommandLineInterface private constructor(
        private val pdfBuilderCommandLineInput: PdfBuilderCommandLineInput,
        private val pdfBuilderCommandLineOutput: PdfBuilderCommandLineOutput
) {
    private var pdfBuildInformation: PdfBuildInformation = PdfBuildInformation()

    fun setup(): Int {
        pdfBuilderCommandLineOutput.printInfo()
        return 1
    }

    fun readPath(): Int {
        pdfBuilderCommandLineOutput.printReadPathInstructions()
        return try {
            pdfBuildInformation.sourceFile = pdfBuilderCommandLineInput.readPath().toFile()
            1
        } catch (exception: InvalidPathException) {
            pdfBuilderCommandLineOutput.printInvalidPath()
            0
        }
    }

    fun readBuildOptions(): Int {
        readMultipleDirectoriesOption()
        readZipFilesOption()

        pdfBuilderCommandLineOutput.printSetupIteratorInformation()

        try {
            pdfBuildInformation.setupDirectoryIterator()
        } catch (exception: FileNotFoundException) {
            pdfBuilderCommandLineOutput.printEmptyDirectoryError()
            -2
        }

        return if (pdfBuildInformation.getDirectoryIterator().numberOfFiles() == 0) {
            pdfBuilderCommandLineOutput.printEmptyDirectoryError()
            -2
        } else 1
    }

    private fun readMultipleDirectoriesOption() {
        pdfBuilderCommandLineOutput.printBuildInstructions()
        pdfBuildInformation.setMultipleDirectories(
                pdfBuilderCommandLineInput.readAnswer {
                    pdfBuilderCommandLineOutput.printBuildInstructions()
                }
        )
    }

    private fun readZipFilesOption() {
        pdfBuilderCommandLineOutput.printZipFilesInstructions(pdfBuildInformation.getMultipleDirectories())
        pdfBuildInformation.setZipFiles(
                pdfBuilderCommandLineInput.readAnswer {
                    pdfBuilderCommandLineOutput.printZipFilesInstructions(pdfBuildInformation.getMultipleDirectories())
                }
        )
    }

    fun readPdfContent(): Int {
        pdfBuilderCommandLineOutput.printPdfModificationInstructions(pdfBuildInformation)
        val iteratorModification = pdfBuilderCommandLineInput.readPdfModification({
            pdfBuilderCommandLineOutput.printPdfModificationInstructions(pdfBuildInformation)
        },
                pdfBuildInformation.getImageToPdfOptions())

        return if (iteratorModification !is IteratorBuildAction) {
            iteratorModification.execute(pdfBuildInformation.getDirectoryIterator())
            0
        } else {
            1
        }
    }

    fun readCustomTargetPath(): Int {
        pdfBuilderCommandLineOutput.printCustomTargetFile()
        pdfBuildInformation.customTargetFile = pdfBuilderCommandLineInput.readAnswer { pdfBuilderCommandLineOutput.printCustomTargetFile() }
        return 1
    }

    fun buildPdf(): Int {
        try {
            setupTargetFile()
        } catch (exception: InvalidPathException) {
            pdfBuilderCommandLineOutput.printInvalidPath()
            return 0
        }

        val progressUpdater = setupProgressUpdater()

        if (!pdfBuildInformation.getMultipleDirectories() && pdfBuildInformation.getTargetFile().extension != "pdf")
            pdfBuildInformation.setTargetFile(File(pdfBuildInformation.getTargetFile().absolutePath + ".pdf"))


        pdfBuilderCommandLineOutput.printBuildInfo(pdfBuildInformation)
        build(progressUpdater)

        return 1
    }

    private fun setupProgressUpdater(): ProgressUpdater {
        return object : ProgressUpdater {
            override fun updateProgress(progress: Double, file: File) {
                if (progress == 1.toDouble()) pdfBuilderCommandLineOutput.printFinishedBuilding()
                else pdfBuilderCommandLineOutput.printProgress()
            }
        }
    }

    private fun setupTargetFile() {
        if (pdfBuildInformation.customTargetFile)
            pdfBuildInformation.setTargetFile(pdfBuilderCommandLineInput.readPath().toFile())
        else
            pdfBuildInformation.setTargetFile(File("${pdfBuildInformation.sourceFile!!.parent}/${pdfBuildInformation.sourceFile!!.nameWithoutExtension}"))
    }

    private fun build(progressUpdater: ProgressUpdater) {
        if (pdfBuildInformation.getImageToPdfOptions().getIteratorOptions().multipleDirectories) {
            ImageDirectoriesPdfBuilder.createImageDirectoriesPdfBuilder().build(
                    pdfBuildInformation.getDirectoryIterator(),
                    pdfBuildInformation.getImageToPdfOptions(),
                    progressUpdater
            )
        } else {
            ImagePdfBuilder.createImagePdfBuilder().build(
                    pdfBuildInformation.getDirectoryIterator(),
                    pdfBuildInformation.getImageToPdfOptions(),
                    progressUpdater
            )
        }
    }

    companion object CommandLineInterfaceFactory {
        fun createCommandLineInterface(pdfBuilderCommandLineInput: PdfBuilderCommandLineInput, pdfBuilderCommandLineOutput: PdfBuilderCommandLineOutput) = PdfBuilderCommandLineInterface(pdfBuilderCommandLineInput, pdfBuilderCommandLineOutput)
    }
}