package de.nihas101.imageToPdfConverter.directoryIterators.exceptions

class MalformedPdfActionException(arguments: List<String>) : Exception("The modification was malformed: $arguments")