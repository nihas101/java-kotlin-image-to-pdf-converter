package de.nihas101.imageToPdfConverter.directoryIterators.exceptions

import java.io.File

class FileIsDirectoryException(val file: File) : Exception("${file.absolutePath} leads to a directory")