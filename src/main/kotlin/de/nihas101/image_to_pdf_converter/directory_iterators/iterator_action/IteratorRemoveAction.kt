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

class IteratorRemoveAction private constructor(modificationArguments: List<String>) : IteratorAction() {
    private val removeIndices = modificationArguments.filterIndexed { index, _ -> index > 0 }.map { argument -> argument.toInt() }

    override fun execute(directoryIterator: DirectoryIterator) {
        removeIndices.sortedDescending().forEach { removeIndex ->
            if (removeIndex in 0 until directoryIterator.numberOfFiles())
                directoryIterator.getFileList().removeAt(removeIndex)
        }
    }

    companion object IteratorMoveModificationFactory {
        fun createIteratorRemoveModification(modificationArguments: List<String>): IteratorRemoveAction {
            if (isFormedCorrectly(modificationArguments)) return IteratorRemoveAction(modificationArguments)
            else throw MalformedPdfActionException(modificationArguments)
        }

        internal fun isLeadingIteratorArgument(argument: String) = "r" == argument || "remove" == argument

        private fun isFormedCorrectly(modificationArguments: List<String>): Boolean {
            if (modificationArguments.size < 2) return false

            return isLeadingIteratorArgument(modificationArguments[0]) && isIntList(modificationArguments.filterIndexed { index, _ -> index > 0 })
        }

        private fun isIntList(argumentList: List<String>): Boolean {
            argumentList.forEach { argument ->
                if (!isInt(argument)) return false
            }

            return true
        }

        fun getInstruction(): String =
                " * (remove | r) [index1] [index2] ... - Remove the files with the specified indices from the PDF"
    }

    override fun toString(): String = "remove $removeIndices"
}