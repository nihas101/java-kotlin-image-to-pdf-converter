package de.nihas101.imagesToPdfConverter.directoryIterators.iteratorAction

import de.nihas101.imagesToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imagesToPdfConverter.directoryIterators.exceptions.MalformedPdfModificationException
import de.nihas101.imagesToPdfConverter.pdf.PdfWriterOptions

abstract class IteratorAction {
    abstract fun execute(directoryIterator: DirectoryIterator)

    companion object PdfModificationFactory {
        fun createIteratorModification(modificationArguments: List<String>, pdfWriterOptions: PdfWriterOptions): IteratorAction {
            if(modificationArguments.isEmpty()) throw MalformedPdfModificationException(modificationArguments)

            return when {
                IteratorMoveAction.isLeadingIteratorArgument(modificationArguments[0]) ->
                    IteratorMoveAction.createIteratorMoveModification(modificationArguments)
                IteratorRemoveAction.isLeadingIteratorArgument(modificationArguments[0]) ->
                    IteratorRemoveAction.createIteratorRemoveModification(modificationArguments)
                IteratorBuildAction.isLeadingIteratorArgument(modificationArguments[0]) ->
                    IteratorBuildAction.createIteratorBuildModification(modificationArguments, pdfWriterOptions)
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