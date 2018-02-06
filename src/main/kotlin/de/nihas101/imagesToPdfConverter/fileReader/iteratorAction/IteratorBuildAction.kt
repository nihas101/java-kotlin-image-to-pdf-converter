package de.nihas101.imagesToPdfConverter.fileReader.iteratorAction

import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator
import de.nihas101.imagesToPdfConverter.fileReader.exceptions.MalformedPdfModificationException
import java.io.File
import java.nio.file.Paths

class IteratorBuildAction private constructor(modificationArguments: List<String>) : IteratorAction() {
    val targetFile: File? = if(modificationArguments.size > 1) Paths.get(modificationArguments[1]).toFile()
                            else null

    override fun execute(directoryIterator: DirectoryIterator) {
        /* NOTHING TO DO */
    }

    companion object IteratorBuildModificationFactory {
        fun createIteratorMoveModification(modificationArguments: List<String>): IteratorBuildAction {
            if(isFormedCorrectly(modificationArguments)) return IteratorBuildAction(modificationArguments)
            else throw MalformedPdfModificationException(modificationArguments)
        }

        internal fun isLeadingIteratorArgument(argument: String) = "b" == argument || "build" == argument

        private fun isFormedCorrectly(modificationArguments: List<String>): Boolean {
            if(modificationArguments.isEmpty()) return false

            return (isLeadingIteratorArgument(modificationArguments[0]))
        }

        fun getInstruction(): String =
            " * (build | b) </path/to/target/file> - Build the PDF(s) and save them at the given path"
    }

    override fun toString() = if(targetFile == null) "build"
                            else "build ${targetFile.absolutePath}"
}