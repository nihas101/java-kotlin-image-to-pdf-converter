package de.nihas101.imageToPdfConverter.ui.commandLineIO

import de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction.IteratorBuildAction
import de.nihas101.imageToPdfConverter.pdf.PdfBuildInformation
import de.nihas101.imageToPdfConverter.pdf.builders.ImageDirectoriesPdfBuilder
import de.nihas101.imageToPdfConverter.pdf.builders.ImagePdfBuilder
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.io.File
import java.nio.file.InvalidPathException

class PdfBuilderCommandLineInterface private constructor(
        private val pdfBuilderCommandLineInput: PdfBuilderCommandLineInput,
        private val pdfBuilderCommandLineOutput: PdfBuilderCommandLineOutput
) {
    private var pdfBuildInformation: PdfBuildInformation = PdfBuildInformation()

    fun setup(): Int {
        pdfBuilderCommandLineOutput.printInfo()
        return 1
    }

    fun readDirectory(): Int {
        pdfBuilderCommandLineOutput.printReadDirectoryInstructions()
        return try {
            pdfBuildInformation.sourceFile = pdfBuilderCommandLineInput.readPath().toFile()
            1
        } catch (exception: InvalidPathException) {
            pdfBuilderCommandLineOutput.printInvalidPath()
            0
        }
    }

    fun readBuildOptions(): Int {
        pdfBuilderCommandLineOutput.printBuildInstructions()
        pdfBuildInformation.setMultipleDirectories(
                pdfBuilderCommandLineInput.readAnswer {
                    pdfBuilderCommandLineOutput.printBuildInstructions()
                }
        )
        pdfBuildInformation.setupDirectoryIterator()

        return if (pdfBuildInformation.getDirectoryIterator().nrOfFiles() == 0) {
            pdfBuilderCommandLineOutput.printEmptyDirectoryError()
            -1
        } else 1
    }

    fun readPdfContent(): Int {
        pdfBuilderCommandLineOutput.printPdfModificationInstructions(pdfBuildInformation)
        val iteratorModification = pdfBuilderCommandLineInput.readPdfModification({
            pdfBuilderCommandLineOutput.printPdfModificationInstructions(pdfBuildInformation)
        },
                pdfBuildInformation.getPdfWriterOptions())

        println(iteratorModification.toString())

        return if (iteratorModification !is IteratorBuildAction) {
            iteratorModification.execute(pdfBuildInformation.getDirectoryIterator())
            0
        } else {
            1
        }
    }

    fun readCustomTargetPath(): Int {
        pdfBuilderCommandLineOutput.printCustomTargetFile()
        pdfBuildInformation.customTargetFile = pdfBuilderCommandLineInput.readAnswer { pdfBuilderCommandLineOutput.printCustomTargetFile() }
        return 1
    }

    fun buildPdf(): Int {
        if (pdfBuildInformation.customTargetFile) {
            try {
                pdfBuildInformation.targetFile = pdfBuilderCommandLineInput.readPath().toFile()
            } catch (exception: InvalidPathException) {
                pdfBuilderCommandLineOutput.printInvalidPath()
                return 0
            }
        } else pdfBuildInformation.targetFile = File(pdfBuildInformation.sourceFile!!.absolutePath)

        val progressUpdater = object : ProgressUpdater {
            override fun updateProgress(progress: Double) {
                if (progress == 1.toDouble()) pdfBuilderCommandLineOutput.printFinishedBuilding()
                else pdfBuilderCommandLineOutput.printProgress()
            }
        }

        if (!pdfBuildInformation.getMultipleDirectories() && pdfBuildInformation.targetFile!!.extension != "pdf")
            pdfBuildInformation.targetFile = File(pdfBuildInformation.targetFile!!.absolutePath + ".pdf")


        pdfBuilderCommandLineOutput.printBuildInfo(pdfBuildInformation)
        if (pdfBuildInformation.getPdfWriterOptions().multipleDirectories) {
            ImageDirectoriesPdfBuilder.createImageDirectoriesPdfBuilder().build(
                    pdfBuildInformation.getDirectoryIterator(),
                    pdfBuildInformation.targetFile!!,
                    pdfBuildInformation.getPdfWriterOptions(),
                    progressUpdater
            )
        } else {
            ImagePdfBuilder.createImagePdfBuilder().build(
                    pdfBuildInformation.getDirectoryIterator(),
                    pdfBuildInformation.targetFile!!,
                    pdfBuildInformation.getPdfWriterOptions(),
                    progressUpdater
            )
        }

        return 1
    }

    companion object CommandLineInterfaceFactory {
        fun createCommandLineInterface(pdfBuilderCommandLineInput: PdfBuilderCommandLineInput, pdfBuilderCommandLineOutput: PdfBuilderCommandLineOutput) = PdfBuilderCommandLineInterface(pdfBuilderCommandLineInput, pdfBuilderCommandLineOutput)
    }
}