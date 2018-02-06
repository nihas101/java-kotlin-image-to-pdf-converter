package de.nihas101.imagesToPdfConverter.fileReader.iteratorAction

import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator
import de.nihas101.imagesToPdfConverter.fileReader.exceptions.MalformedPdfModificationException

abstract class IteratorAction {
    abstract fun execute(directoryIterator: DirectoryIterator)

    companion object PdfModificationFactory {
        fun createIteratorModification(modificationArguments: List<String>): IteratorAction {
            if(modificationArguments.isEmpty()) throw MalformedPdfModificationException(modificationArguments)

            return when {
                IteratorMoveAction.isLeadingIteratorArgument(modificationArguments[0]) ->
                    IteratorMoveAction.createIteratorMoveModification(modificationArguments)
                IteratorRemoveAction.isLeadingIteratorArgument(modificationArguments[0]) ->
                    IteratorRemoveAction.createIteratorRemoveModification(modificationArguments)
                IteratorBuildAction.isLeadingIteratorArgument(modificationArguments[0]) ->
                    IteratorBuildAction.createIteratorMoveModification(modificationArguments)
                else -> throw MalformedPdfModificationException(modificationArguments)
            }
        }

        internal fun isInt(string: String): Boolean {
            try{ string.toInt() }
            catch (exception: NumberFormatException){ return false }

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