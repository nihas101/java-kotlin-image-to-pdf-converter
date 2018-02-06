package de.nihas101.imagesToPdfConverter.ui

import de.nihas101.imagesToPdfConverter.ui.commandLineIO.PdfBuilderCommandLineInterface

class PdfBuilderUI(private val pdfBuilderCommandLineInterface: PdfBuilderCommandLineInterface){
    private var state: Int = 0
    private var doContinue: Boolean = true

    companion object PdfBuilderUiFactory{
        fun createPdfBuilderUI(pdfBuilderCommandLineInterface: PdfBuilderCommandLineInterface) = PdfBuilderUI(pdfBuilderCommandLineInterface)
    }

    fun start() {
        while(doContinue) nextState()
    }

    private fun nextState() {
        when(state){
            0 -> execute { pdfBuilderCommandLineInterface.setup() }
            1 -> execute { pdfBuilderCommandLineInterface.readDirectory() }
            2 -> execute { pdfBuilderCommandLineInterface.readBuildOptions() }
            3 -> execute { pdfBuilderCommandLineInterface.readPdfContent() }
            4 -> execute { pdfBuilderCommandLineInterface.buildPdf() }
            else -> state = 0
        }
    }

    private fun execute(task: () -> Int){
        state += task()
    }
}