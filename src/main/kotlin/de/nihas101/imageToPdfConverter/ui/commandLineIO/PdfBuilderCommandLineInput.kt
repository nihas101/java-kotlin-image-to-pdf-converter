package de.nihas101.imageToPdfConverter.ui.commandLineIO

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.MalformedPdfActionException
import de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction.IteratorAction
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions
import java.io.BufferedReader
import java.nio.file.Path
import java.nio.file.Paths

class PdfBuilderCommandLineInput private constructor(private val bufferedReader: BufferedReader) {
    companion object CommandLineInputFactory {
        fun createCommandLineInput(inputStream: BufferedReader) = PdfBuilderCommandLineInput(inputStream)
    }

    fun readPath(): Path = Paths.get(readReader())


    fun readAnswer(onFail: () -> Unit): Boolean {
        while (true) {
            val readLine = readReader()
            if ("yes" == readLine || "y" == readLine) return true
            else if ("no" == readLine || "n" == readLine) return false
            else onFail()
        }
    }

    fun readPdfModification(onFail: () -> Unit, imageToPdfOptions: ImageToPdfOptions): IteratorAction {
        while (true) {
            val readModificationArguments = readReader().split(Regex("\\s"))
            try {
                return IteratorAction.createIteratorAction(readModificationArguments, imageToPdfOptions)
            } catch (exception: MalformedPdfActionException) {
                onFail()
            }
        }
    }

    private fun readReader(): String {
        val readLine = bufferedReader.readLine()

        if ("exit" == readLine)
            throw UserWantsToExitException()
        return readLine
    }
}