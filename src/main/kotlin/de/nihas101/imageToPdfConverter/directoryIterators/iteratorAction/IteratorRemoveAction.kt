package de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.MalformedPdfModificationException

class IteratorRemoveAction private constructor(modificationArguments: List<String>) : IteratorAction() {
    private val removeIndices = modificationArguments.filterIndexed { index, _ -> index > 0 }.map { argument -> argument.toInt() }

    override fun execute(directoryIterator: DirectoryIterator) {
        removeIndices.sortedDescending().forEach { removeIndex ->
            if (removeIndex in 0 until directoryIterator.numberOfFiles())
                directoryIterator.getFiles().removeAt(removeIndex)
        }
    }

    companion object IteratorMoveModificationFactory {
        fun createIteratorRemoveModification(modificationArguments: List<String>): IteratorRemoveAction {
            if (isFormedCorrectly(modificationArguments)) return IteratorRemoveAction(modificationArguments)
            else throw MalformedPdfModificationException(modificationArguments)
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