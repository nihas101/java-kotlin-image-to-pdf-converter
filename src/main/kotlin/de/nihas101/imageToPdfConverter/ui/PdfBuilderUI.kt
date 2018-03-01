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

package de.nihas101.imageToPdfConverter.ui

import de.nihas101.imageToPdfConverter.ui.commandLineIO.PdfBuilderCommandLineInterface
import de.nihas101.imageToPdfConverter.ui.commandLineIO.UserWantsToExitException

class PdfBuilderUI(private val pdfBuilderCommandLineInterface: PdfBuilderCommandLineInterface) {
    private var state: Int = 0

    companion object PdfBuilderUiFactory {
        fun createPdfBuilderUI(pdfBuilderCommandLineInterface: PdfBuilderCommandLineInterface) = PdfBuilderUI(pdfBuilderCommandLineInterface)
    }

    fun start() {
        try {
            while (true) nextState()
        } catch (exception: UserWantsToExitException) {
            return
        }
    }

    private fun nextState() {
        when (state) {
            0 -> execute { pdfBuilderCommandLineInterface.setup() }
            1 -> execute { pdfBuilderCommandLineInterface.readPath() }
            2 -> execute { pdfBuilderCommandLineInterface.readBuildOptions() }
            3 -> execute { pdfBuilderCommandLineInterface.readPdfContent() }
            4 -> execute { pdfBuilderCommandLineInterface.readCustomTargetPath() }
            5 -> execute { pdfBuilderCommandLineInterface.buildPdf() }
            else -> state = 0
        }
    }

    private fun execute(task: () -> Int) {
        state += task()
    }
}