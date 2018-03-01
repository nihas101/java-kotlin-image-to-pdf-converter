package de.nihas101.imageToPdfConverter.ui

import de.nihas101.imageToPdfConverter.ui.commandLineIO.PdfBuilderCommandLineInterface
import de.nihas101.imageToPdfConverter.ui.commandLineIO.UserWantsToExitException

class PdfBuilderUI(private val pdfBuilderCommandLineInterface: PdfBuilderCommandLineInterface) {
    private var state: Int = 0

    companion object PdfBuilderUiFactory {
        fun createPdfBuilderUI(pdfBuilderCommandLineInterface: PdfBuilderCommandLineInterface) = PdfBuilderUI(pdfBuilderCommandLineInterface)
    }

    fun start() {
        try {
            while (true) nextState()
        } catch (exception: UserWantsToExitException) {
            return
        }
    }

    private fun nextState() {
        when (state) {
            0 -> execute { pdfBuilderCommandLineInterface.setup() }
            1 -> execute { pdfBuilderCommandLineInterface.readPath() }
            2 -> execute { pdfBuilderCommandLineInterface.readBuildOptions() }
            3 -> execute { pdfBuilderCommandLineInterface.readPdfContent() }
            4 -> execute { pdfBuilderCommandLineInterface.readCustomTargetPath() }
            5 -> execute { pdfBuilderCommandLineInterface.buildPdf() }
            else -> state = 0
        }
    }

    private fun execute(task: () -> Int) {
        state += task()
    }
}