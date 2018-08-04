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

package de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction

import com.itextpdf.kernel.pdf.PdfVersion.*
import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.MalformedPdfActionException
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions
import java.util.zip.Deflater.*

class IteratorBuildAction private constructor(
        private val modificationArguments: List<String>,
        private var imageToPdfOptions: ImageToPdfOptions
) : IteratorAction() {
    private var compressionString = "default"

    init {
        parseArguments()
    }

    private fun parseArguments() {
        modificationArguments.forEach { string ->
            if (string.startsWith("--v")) parseVersion(string)
            if (string.startsWith("--c")) parseCompression(string)
        }
    }

    private fun parseCompression(compressionArg: String) {
        val versionArguments = compressionArg.split(":")
        var compressionLevel = DEFAULT_COMPRESSION

        if (versionArguments.size > 1)
            compressionLevel = when (versionArguments[1].toLowerCase()) {
                "best" -> {
                    compressionString = "best"
                    BEST_COMPRESSION
                }
                "fast" -> {
                    compressionString = "fast"
                    BEST_SPEED
                }
                else -> {
                    compressionString = "default"
                    DEFAULT_COMPRESSION
                }
            }

        imageToPdfOptions =
                imageToPdfOptions.copy(pdfOptions = imageToPdfOptions.getPdfOptions().copy(compressionLevel = compressionLevel))
    }

    private fun parseVersion(versionArg: String) {
        val versionArguments = versionArg.split(":")
        var pdfVersion = PDF_1_7

        if (versionArguments.size > 1)
            pdfVersion = when (versionArguments[1]) {
                "1.0" -> PDF_1_0
                "1.1" -> PDF_1_1
                "1.2" -> PDF_1_2
                "1.3" -> PDF_1_3
                "1.4" -> PDF_1_4
                "1.5" -> PDF_1_5
                "1.6" -> PDF_1_6
                "2.0" -> PDF_2_0
                else -> PDF_1_7
            }

        imageToPdfOptions =
                imageToPdfOptions.copy(pdfOptions = imageToPdfOptions.getPdfOptions().copy(pdfVersion = pdfVersion))
    }

    override fun execute(directoryIterator: DirectoryIterator) {
        /* NOTHING TO DO */
    }

    companion object IteratorBuildModificationFactory {
        fun createIteratorBuildModification(
                modificationArguments: List<String>,
                imageToPdfOptions: ImageToPdfOptions
        ): IteratorBuildAction {
            if (isFormedCorrectly(modificationArguments))
                return IteratorBuildAction(modificationArguments, imageToPdfOptions)
            else throw MalformedPdfActionException(modificationArguments)
        }

        internal fun isLeadingIteratorArgument(argument: String) = "b" == argument || "build" == argument

        private fun isFormedCorrectly(modificationArguments: List<String>): Boolean {
            if (modificationArguments.isEmpty()) return false

            return (isLeadingIteratorArgument(modificationArguments[0]))
        }

        fun getInstruction(): String =
                " * (build | b) [OPTIONAL FLAGS] - Build the PDF(s)\n" +
                        "\tOPTIONAL FLAGS:\n" +
                        "\t--v:[VERSION] Define a PDF version to use (Default: 1.7)\n" +
                        "\t\t [VERSION] = (1.0 | 1.1 | 1.2 | 1.3 | 1.4 | 1.5 | 1.6 | 1.7 | 2.0)\n" +
                        "\t--c:[COMPRESSION] Set the compression level of the PDF(s)\n" +
                        "\t\t [COMPRESSION] = (best | fast | default)\n"
    }

    override fun toString() =
            "build version: ${imageToPdfOptions.getPdfOptions().pdfVersion} compression: $compressionString"
}