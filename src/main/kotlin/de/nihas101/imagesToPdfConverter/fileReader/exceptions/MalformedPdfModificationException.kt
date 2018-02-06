package de.nihas101.imagesToPdfConverter.fileReader.exceptions

class MalformedPdfModificationException(arguments: List<String>) : Exception("The modification was malformed: $arguments")