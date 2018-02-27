package de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.MalformedPdfActionException
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions

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