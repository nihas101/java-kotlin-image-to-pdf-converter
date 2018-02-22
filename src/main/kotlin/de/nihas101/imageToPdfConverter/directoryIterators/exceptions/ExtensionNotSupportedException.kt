package de.nihas101.imageToPdfConverter.directoryIterators.exceptions

class ExtensionNotSupportedException(extension: String) : Exception("The extension $extension is not supported")