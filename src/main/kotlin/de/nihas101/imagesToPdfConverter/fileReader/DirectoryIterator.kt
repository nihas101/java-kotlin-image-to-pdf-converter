package de.nihas101.imagesToPdfConverter.fileReader

import java.io.File

interface DirectoryIterator {
    fun nextFile(): File
    fun getFile(index: Int): File
    fun getFiles(): List<File>
    fun nrOfFiles(): Int
    fun getParentDirectory(): File
}