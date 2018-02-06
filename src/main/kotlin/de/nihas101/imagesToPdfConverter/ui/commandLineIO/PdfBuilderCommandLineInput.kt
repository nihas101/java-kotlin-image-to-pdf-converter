package de.nihas101.imagesToPdfConverter.ui.commandLineIO

import de.nihas101.imagesToPdfConverter.fileReader.iteratorAction.IteratorAction
import de.nihas101.imagesToPdfConverter.fileReader.exceptions.MalformedPdfModificationException
import java.io.BufferedReader
import java.nio.file.Path
import java.nio.file.Paths

class PdfBuilderCommandLineInput private constructor(private val bufferedReader: BufferedReader){
    companion object CommandLineInputFactory{
        fun createCommandLineInput(inputStream: BufferedReader) = PdfBuilderCommandLineInput(inputStream)
    }

    fun readPath(): Path = Paths.get(readReader())


    fun readAnswer(onFail: () -> Unit): Boolean {
        while(true) {
            val readLine = readReader()
            if("yes" == readLine || "y" == readLine) return true
            else if("no" == readLine || "n" == readLine) return false
            else onFail()
        }
    }

    fun readPdfModification(onFail: () -> Unit): IteratorAction {
        while (true){
            val readModificationArguments = readReader().split(Regex("\\s"))
            try{
                return IteratorAction.createIteratorModification(readModificationArguments)
            }catch(exception: MalformedPdfModificationException){
                onFail()
            }
        }
    }

    private fun readReader(): String{
        val readLine = bufferedReader.readLine()

        if("exit" == readLine) System.exit(0)
        return readLine
    }
}