package de.nihas101.imagesToPdfConverter.directoryIterators.exceptions

class MalformedPdfModificationException(arguments: List<String>) : Exception("The modification was malformed: $arguments")