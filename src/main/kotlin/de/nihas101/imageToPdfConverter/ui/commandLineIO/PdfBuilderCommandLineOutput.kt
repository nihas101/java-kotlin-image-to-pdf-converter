package de.nihas101.imageToPdfConverter.ui.commandLineIO

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction.IteratorAction
import de.nihas101.imageToPdfConverter.pdf.PdfBuildInformation
import java.io.PrintStream

class PdfBuilderCommandLineOutput private constructor(private val printStream: PrintStream){
    fun printInfo() {
        printStream.println("Follow the instructions to build the PDF(s). To exit the program, simply type \"exit\".\n")
    }

    fun printReadDirectoryInstructions() = printMessage("Please supply a path to a source-directory from which to build the PDF(s)")

    fun printBuildInstructions() = printQuestion("Do you want to build multiple PDFs from this source?")

    fun printPdfModificationInstructions(pdfBuildInformation: PdfBuildInformation) {
        printStream.println("Instructions:")
        IteratorAction.getInstructions().forEach({ instruction -> printStream.println(instruction)})
        printStream.println()
        printPdfContent(pdfBuildInformation)
        printMessage("")
    }

    private fun printPdfContent(pdfBuildInformation: PdfBuildInformation){
        val directoryIterator: DirectoryIterator = pdfBuildInformation.getDirectoryIterator()

        for(index in 0 until directoryIterator.nrOfFiles())
            printStream.println("$index: ${directoryIterator.getFile(index).name}")
    }

    fun printBuildInfo(pdfBuildInformation: PdfBuildInformation) =
            if(pdfBuildInformation.getPdfWriterOptions().multipleDirectories) printStream.println("Building PDFs of version ${pdfBuildInformation.getPdfWriterOptions().pdfVersion}")
            else printStream.println("Building PDF of version ${pdfBuildInformation.getPdfWriterOptions().pdfVersion}")

    fun printProgress() = printStream.print(".")

    fun printFinishedBuilding() = printStream.println("\nFinished Building.")

    fun printInvalidPath() = printStream.println("The given path is invalid\n")

    fun printEmptyDirectoryError() = printStream.println("The given directory contains no images or doesn't exist\n")

    fun printCustomTargetFile() {
        printQuestion("Do you want to save the PDF at a different location than the source files?")
    }

    private fun printQuestion(message: String) = printMessage("$message (yes/no)")

    private fun printMessage(message: String) = printStream.print("$message\n> ")

    companion object CommandLineOutputFactory{
        fun createCommandLineOutput(outputStream: PrintStream) = PdfBuilderCommandLineOutput(outputStream)
    }
}