package de.nihas101.imageToPdfConverter.util

class NonPositiveMaxSearchDepth(maximalSearchDepth: Int) : Exception("The maximal search depth must be a natural number: $maximalSearchDepth")
