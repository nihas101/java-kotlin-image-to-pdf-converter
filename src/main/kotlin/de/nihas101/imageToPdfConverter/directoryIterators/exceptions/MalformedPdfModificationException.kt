package de.nihas101.imageToPdfConverter.directoryIterators.exceptions

class MalformedPdfModificationException(arguments: List<String>) : Exception("The modification was malformed: $arguments")