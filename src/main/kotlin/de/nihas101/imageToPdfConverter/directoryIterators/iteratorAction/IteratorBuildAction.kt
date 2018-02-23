package de.nihas101.imageToPdfConverter.directoryIterators.iteratorAction

import com.itextpdf.kernel.pdf.CompressionConstants.*
import com.itextpdf.kernel.pdf.PdfVersion.*
import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.MalformedPdfModificationException
import de.nihas101.imageToPdfConverter.pdf.ImageToPdfOptions

class IteratorBuildAction private constructor(
        private val modificationArguments: List<String>,
        private var imageToPdfOptions: ImageToPdfOptions
) : IteratorAction() {
    private var compressionString = "default"

    init {
        parseArguments()
    }

    private fun parseArguments() {
        modificationArguments.forEach { string ->
            if (string.startsWith("--v")) parseVersion(string)
            if (string.startsWith("--c")) parseCompression(string)
        }
    }

    private fun parseCompression(compressionArg: String) {
        val versionArguments = compressionArg.split(":")
        var compressionLevel = DEFAULT_COMPRESSION

        if (versionArguments.size > 1)
            compressionLevel = when (versionArguments[1].toLowerCase()) {
                "best" -> {
                    compressionString = "best"
                    BEST_COMPRESSION
                }
                "fast" -> {
                    compressionString = "fast"
                    BEST_SPEED
                }
                else -> {
                    compressionString = "default"
                    DEFAULT_COMPRESSION
                }
            }

        imageToPdfOptions =
                imageToPdfOptions.copy(pdfOptions = imageToPdfOptions.pdfOptions.copy(compressionLevel = compressionLevel))
    }

    private fun parseVersion(versionArg: String) {
        val versionArguments = versionArg.split(":")
        var pdfVersion = PDF_1_7

        if (versionArguments.size > 1)
            pdfVersion = when (versionArguments[1]) {
                "1.0" -> PDF_1_0
                "1.1" -> PDF_1_1
                "1.2" -> PDF_1_2
                "1.3" -> PDF_1_3
                "1.4" -> PDF_1_4
                "1.5" -> PDF_1_5
                "1.6" -> PDF_1_6
                "2.0" -> PDF_2_0
                else -> PDF_1_7
            }

        imageToPdfOptions =
                imageToPdfOptions.copy(pdfOptions = imageToPdfOptions.pdfOptions.copy(pdfVersion = pdfVersion))
    }

    override fun execute(directoryIterator: DirectoryIterator) {
        /* NOTHING TO DO */
    }

    companion object IteratorBuildModificationFactory {
        fun createIteratorBuildModification(
                modificationArguments: List<String>,
                imageToPdfOptions: ImageToPdfOptions
        ): IteratorBuildAction {
            if (isFormedCorrectly(modificationArguments))
                return IteratorBuildAction(modificationArguments, imageToPdfOptions)
            else throw MalformedPdfModificationException(modificationArguments)
        }

        internal fun isLeadingIteratorArgument(argument: String) = "b" == argument || "build" == argument

        private fun isFormedCorrectly(modificationArguments: List<String>): Boolean {
            if (modificationArguments.isEmpty()) return false

            return (isLeadingIteratorArgument(modificationArguments[0]))
        }

        fun getInstruction(): String =
                " * (build | b) [FLAG]* - Build the PDF(s)\n" +
                        "\tFLAG:\n" +
                        "\t--v:[VERSION] Define a PDF version to use (Default: 1.7)\n" +
                        "\t\t [VERSION] = (1.0 | 1.1 | 1.2 | 1.3 | 1.4 | 1.5 | 1.6 | 1.7 | 2.0)\n" +
                        "\t--c:[COMPRESSION] Set the compression level of the PDF(s)\n" +
                        "\t\t [COMPRESSION] = (best | fast | default)\n"
    }

    override fun toString() =
            "build version: ${imageToPdfOptions.pdfOptions.pdfVersion} compression: $compressionString"
}