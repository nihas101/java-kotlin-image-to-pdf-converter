package de.nihas101.imageToPdfConverter.util


import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser

/**
 * Sets up the [DirectoryChooser] instance
 */
fun createDirectoryChooser(iteratorOptions: IteratorOptions): DirectoryChooser {
    val directoryChooser = DirectoryChooser()
    setTitle(directoryChooser, iteratorOptions)
    return directoryChooser
}

private fun setTitle(directoryChooser: DirectoryChooser, iteratorOptions: IteratorOptions) {
    when {
        iteratorOptions.multipleDirectories -> directoryChooser.title = "Choose a directory of directories to turn into a PDF"
        else -> directoryChooser.title = "Choose a directory to turn into a PDF"
    }
}

fun createZipFileChooser(): FileChooser {
    val fileChooser = FileChooser()
    fileChooser.title = "Choose a zip-directory to turn into a PDF"
    return fileChooser
}

/**
 * Sets up the [FileChooser] instance
 */
fun createSaveFileChooser(): FileChooser {
    val saveFileChooser = FileChooser()
    saveFileChooser.title = "Choose a save location for the PDF"
    val extFilter = FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
    saveFileChooser.extensionFilters.add(extFilter)

    return saveFileChooser
}
