package de.nihas101.image_to_pdf_converter.util

class NonPositiveMaxSearchDepth(maximalSearchDepth: Int) : Exception("The maximal search depth must be a natural number: $maximalSearchDepth")
