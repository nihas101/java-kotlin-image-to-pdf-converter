package de.nihas101.imagesToPdfConverter.fileReader

import java.io.File

class NoMoreDirectoriesException(file: File) : Exception("There are no more directories to be found at: ${file.absolutePath}")