package de.nihas101.imageToPdfConverter

import de.nihas101.imageToPdfConverter.gui.MainWindow
import de.nihas101.imageToPdfConverter.ui.PdfBuilderUI
import de.nihas101.imageToPdfConverter.ui.commandLineIO.PdfBuilderCommandLineInput
import de.nihas101.imageToPdfConverter.ui.commandLineIO.PdfBuilderCommandLineInterface
import de.nihas101.imageToPdfConverter.ui.commandLineIO.PdfBuilderCommandLineOutput
import java.io.BufferedReader
import java.io.InputStreamReader

object Main {
    /* TODO: Add copyright notice to every file */
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty()) MainWindow().show()
        else if ("--nogui" == args[0].toLowerCase()) {
            val commandLineOutput = PdfBuilderCommandLineOutput.createCommandLineOutput(System.out)
            val commandLineInput = PdfBuilderCommandLineInput.createCommandLineInput(BufferedReader(InputStreamReader(System.`in`)))
            val commandLineInterface = PdfBuilderCommandLineInterface.createCommandLineInterface(commandLineInput, commandLineOutput)
            PdfBuilderUI.createPdfBuilderUI(commandLineInterface).start()
        }
    }
}
