/*
 Image2PDF is a program for converting images to PDFs with the use of iText 7
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

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.MalformedPdfActionException
import de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction.IteratorAction
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions
import java.io.BufferedReader
import java.nio.file.Path
import java.nio.file.Paths

class PdfBuilderCommandLineInput private constructor(private val bufferedReader: BufferedReader) {
    companion object CommandLineInputFactory {
        fun createCommandLineInput(inputStream: BufferedReader) = PdfBuilderCommandLineInput(inputStream)
    }

    fun readPath(): Path = Paths.get(readReader())


    fun readAnswer(onFail: () -> Unit): Boolean {
        while (true) {
            val readLine = readReader()
            if ("yes" == readLine || "y" == readLine) return true
            else if ("no" == readLine || "n" == readLine) return false
            else onFail()
        }
    }

    fun readPdfModification(onFail: () -> Unit, imageToPdfOptions: ImageToPdfOptions): IteratorAction {
        while (true) {
            val readModificationArguments = readReader().split(Regex("\\s"))
            try {
                return IteratorAction.createIteratorAction(readModificationArguments, imageToPdfOptions)
            } catch (exception: MalformedPdfActionException) {
                onFail()
            }
        }
    }

    private fun readReader(): String {
        val readLine = bufferedReader.readLine()

        if ("exit" == readLine)
            throw UserWantsToExitException()
        return readLine
    }
}