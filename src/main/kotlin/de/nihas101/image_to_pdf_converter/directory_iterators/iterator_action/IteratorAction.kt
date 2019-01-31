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

package de.nihas101.image_to_pdf_converter.directory_iterators.iterator_action

import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator
import de.nihas101.image_to_pdf_converter.directory_iterators.exceptions.MalformedPdfActionException
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.ImageToPdfOptions

abstract class IteratorAction {
    abstract fun execute(directoryIterator: DirectoryIterator)

    companion object PdfModificationFactory {
        fun createIteratorAction(modificationArguments: List<String>, imageToPdfOptions: ImageToPdfOptions): IteratorAction {
            if (modificationArguments.isEmpty()) throw MalformedPdfActionException(modificationArguments)

            return when {
                IteratorMoveAction.isLeadingIteratorArgument(modificationArguments[0]) ->
                    IteratorMoveAction.createIteratorMoveModification(modificationArguments)
                IteratorRemoveAction.isLeadingIteratorArgument(modificationArguments[0]) ->
                    IteratorRemoveAction.createIteratorRemoveModification(modificationArguments)
                IteratorBuildAction.isLeadingIteratorArgument(modificationArguments[0]) ->
                    IteratorBuildAction.createIteratorBuildModification(modificationArguments, imageToPdfOptions)
                else -> throw MalformedPdfActionException(modificationArguments)
            }
        }

        internal fun isInt(string: String): Boolean {
            try {
                string.toInt()
            } catch (exception: NumberFormatException) {
                return false
            }

            return true
        }

        fun getInstructions(): List<String> {
            val moveString = IteratorMoveAction.getInstruction()
            val removeString = IteratorRemoveAction.getInstruction()
            val buildString = IteratorBuildAction.getInstruction()

            return listOf(moveString, removeString, buildString)
        }
    }
}