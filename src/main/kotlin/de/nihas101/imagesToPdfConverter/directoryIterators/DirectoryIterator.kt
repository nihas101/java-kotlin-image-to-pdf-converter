package de.nihas101.imagesToPdfConverter.directoryIterators

import java.io.File

interface DirectoryIterator {
    fun nextFile(): File
    fun getFile(index: Int): File
    fun getFiles(): MutableList<File>
    fun nrOfFiles(): Int
    fun getParentDirectory(): File
    fun resetIndex()
}