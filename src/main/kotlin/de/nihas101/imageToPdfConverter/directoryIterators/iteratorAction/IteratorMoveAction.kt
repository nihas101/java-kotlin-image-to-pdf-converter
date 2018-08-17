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

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.MalformedPdfActionException

class IteratorMoveAction private constructor(pdfModificationArguments: List<String>) : IteratorAction() {
    private val moveFromIndex: Int = pdfModificationArguments[1].toInt()
    private val moveToIndex: Int = pdfModificationArguments[2].toInt()

    override fun execute(directoryIterator: DirectoryIterator) {
        if (moveFromIndex in 0 until directoryIterator.numberOfFiles() && moveToIndex in 0 until directoryIterator.numberOfFiles()) {
            val file = directoryIterator.getFileList().removeAt(moveFromIndex)
            directoryIterator.getFileList().add(moveToIndex, file)
        }
    }

    companion object IteratorMoveModificationFactory {
        fun createIteratorMoveModification(modificationArguments: List<String>): IteratorMoveAction {
            if (isFormedCorrectly(modificationArguments)) return IteratorMoveAction(modificationArguments)
            else throw MalformedPdfActionException(modificationArguments)
        }

        internal fun isLeadingIteratorArgument(argument: String) = "m" == argument || "move" == argument

        private fun isFormedCorrectly(modificationArguments: List<String>): Boolean {
            if (modificationArguments.size < 3) return false

            return isLeadingIteratorArgument(modificationArguments[0]) &&
                    isInt(modificationArguments[1]) && isInt(modificationArguments[2])
        }

        fun getInstruction(): String =
                " * (move | m) [moveFromIndex] [moveToIndex] - Move the directory at moveFromIndex to moveToIndex"
    }

    override fun toString() = "move <$moveFromIndex> <$moveToIndex>"
}

