package de.nihas101.imageToPdfConverter.util

import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser

/**
 * Sets up the [DirectoryChooser] instance
 */
fun createDirectoryChooser() = DirectoryChooser()

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
