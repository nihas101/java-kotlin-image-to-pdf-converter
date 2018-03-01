package de.nihas101.imageToPdfConverter.ui.commandLineIO

import de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction.IteratorBuildAction
import de.nihas101.imageToPdfConverter.pdf.builders.ImageDirectoriesPdfBuilder
import de.nihas101.imageToPdfConverter.pdf.builders.ImagePdfBuilder
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.PdfBuildInformation
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.InvalidPathException

class PdfBuilderCommandLineInterface private constructor(
        private val pdfBuilderCommandLineInput: PdfBuilderCommandLineInput,
        private val pdfBuilderCommandLineOutput: PdfBuilderCommandLineOutput
) {
    private var pdfBuildInformation: PdfBuildInformation = PdfBuildInformation()

    /* TODO: Implement zip directory usage! */

    fun setup(): Int {
        pdfBuilderCommandLineOutput.printInfo()
        return 1
    }

    fun readPath(): Int {
        pdfBuilderCommandLineOutput.printReadPathInstructions()
        return try {
            pdfBuildInformation.sourceFile = pdfBuilderCommandLineInput.readPath().toFile()
            1
        } catch (exception: InvalidPathException) {
            pdfBuilderCommandLineOutput.printInvalidPath()
            0
        }
    }

    fun readBuildOptions(): Int {
        readMultipleDirectoriesOption()
        readZipFilesOption()

        pdfBuilderCommandLineOutput.printSetupIteratorInformation()

        try {
            pdfBuildInformation.setupDirectoryIterator()
        } catch (exception: FileNotFoundException) {
            pdfBuilderCommandLineOutput.printEmptyDirectoryError()
            -2
        }

        return if (pdfBuildInformation.getDirectoryIterator().numberOfFiles() == 0) {
            pdfBuilderCommandLineOutput.printEmptyDirectoryError()
            -2
        } else 1
    }

    private fun readMultipleDirectoriesOption() {
        pdfBuilderCommandLineOutput.printBuildInstructions()
        pdfBuildInformation.setMultipleDirectories(
                pdfBuilderCommandLineInput.readAnswer {
                    pdfBuilderCommandLineOutput.printBuildInstructions()
                }
        )
    }

    private fun readZipFilesOption() {
        pdfBuilderCommandLineOutput.printZipFilesInstructions(pdfBuildInformation.getMultipleDirectories())
        pdfBuildInformation.setZipFiles(
                pdfBuilderCommandLineInput.readAnswer {
                    pdfBuilderCommandLineOutput.printZipFilesInstructions(pdfBuildInformation.getMultipleDirectories())
                }
        )
    }

    fun readPdfContent(): Int {
        pdfBuilderCommandLineOutput.printPdfModificationInstructions(pdfBuildInformation)
        val iteratorModification = pdfBuilderCommandLineInput.readPdfModification({
            pdfBuilderCommandLineOutput.printPdfModificationInstructions(pdfBuildInformation)
        },
                pdfBuildInformation.getImageToPdfOptions())

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
        try {
            setupTargetFile()
        } catch (exception: InvalidPathException) {
            pdfBuilderCommandLineOutput.printInvalidPath()
            return 0
        }

        val progressUpdater = setupProgressUpdater()

        if (!pdfBuildInformation.getMultipleDirectories() && pdfBuildInformation.getTargetFile().extension != "pdf")
            pdfBuildInformation.setTargetFile(File(pdfBuildInformation.getTargetFile().absolutePath + ".pdf"))


        pdfBuilderCommandLineOutput.printBuildInfo(pdfBuildInformation)
        build(progressUpdater)

        return 1
    }

    private fun setupProgressUpdater(): ProgressUpdater {
        return object : ProgressUpdater {
            override fun updateProgress(progress: Double, file: File) {
                if (progress == 1.toDouble()) pdfBuilderCommandLineOutput.printFinishedBuilding()
                else pdfBuilderCommandLineOutput.printProgress()
            }
        }
    }

    private fun setupTargetFile() {
        if (pdfBuildInformation.customTargetFile)
            pdfBuildInformation.setTargetFile(pdfBuilderCommandLineInput.readPath().toFile())
        else
            pdfBuildInformation.setTargetFile(File("${pdfBuildInformation.sourceFile!!.parent}/${pdfBuildInformation.sourceFile!!.nameWithoutExtension}"))
    }

    private fun build(progressUpdater: ProgressUpdater) {
        if (pdfBuildInformation.getImageToPdfOptions().getIteratorOptions().multipleDirectories) {
            ImageDirectoriesPdfBuilder.createImageDirectoriesPdfBuilder().build(
                    pdfBuildInformation.getDirectoryIterator(),
                    pdfBuildInformation.getImageToPdfOptions(),
                    progressUpdater
            )
        } else {
            ImagePdfBuilder.createImagePdfBuilder().build(
                    pdfBuildInformation.getDirectoryIterator(),
                    pdfBuildInformation.getImageToPdfOptions(),
                    progressUpdater
            )
        }
    }

    companion object CommandLineInterfaceFactory {
        fun createCommandLineInterface(pdfBuilderCommandLineInput: PdfBuilderCommandLineInput, pdfBuilderCommandLineOutput: PdfBuilderCommandLineOutput) = PdfBuilderCommandLineInterface(pdfBuilderCommandLineInput, pdfBuilderCommandLineOutput)
    }
}