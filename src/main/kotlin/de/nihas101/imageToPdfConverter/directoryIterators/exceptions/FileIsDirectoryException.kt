package de.nihas101.imageToPdfConverter.directoryIterators.exceptions

import java.io.File

class FileIsDirectoryException(file: File) : Exception("${file.absolutePath} leads to a directory")