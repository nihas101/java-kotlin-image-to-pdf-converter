package de.nihas101.imagesToPdfConverter.directoryIterators.iteratorAction

import de.nihas101.imagesToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imagesToPdfConverter.directoryIterators.exceptions.MalformedPdfModificationException

class IteratorMoveAction private constructor(pdfModificationArguments: List<String>) : IteratorAction() {
    private val moveFromIndex: Int = pdfModificationArguments[1].toInt()
    private val moveToIndex: Int = pdfModificationArguments[2].toInt()

    override fun execute(directoryIterator: DirectoryIterator) {
        if(moveFromIndex in 0 until directoryIterator.nrOfFiles() && moveToIndex in 0 until directoryIterator.nrOfFiles()) {
            val file = directoryIterator.getFiles().removeAt(moveFromIndex)
            directoryIterator.getFiles().add(moveToIndex, file)
        }
    }

    companion object IteratorMoveModificationFactory {
        fun createIteratorMoveModification(modificationArguments: List<String>): IteratorMoveAction {
            if(isFormedCorrectly(modificationArguments)) return IteratorMoveAction(modificationArguments)
            else throw MalformedPdfModificationException(modificationArguments)
        }

        internal fun isLeadingIteratorArgument(argument: String) = "m" == argument || "move" == argument

        private fun isFormedCorrectly(modificationArguments: List<String>): Boolean {
            if(modificationArguments.size < 3) return false

            return isLeadingIteratorArgument(modificationArguments[0]) &&
                    isInt(modificationArguments[1]) && isInt(modificationArguments[2])
        }

        fun getInstruction(): String =
                " * (move | m) [moveFromIndex] [moveToIndex] - Move the file at moveFromIndex to moveToIndex"
    }

    override fun toString() = "move <$moveFromIndex> <$moveToIndex>"
}

