package de.nihas101.image_to_pdf_converter.util

import java.io.File

class RecursiveFileSearcher private constructor(
        private val filesInCurrentDepth: MutableList<File> = mutableListOf()
) {
    fun searchRecursively(maximalSearchDepth: Int, progressUpdater: ProgressUpdater, matches: (File) -> Boolean): List<File> {
        if (maximalSearchDepth < 1) throw NonPositiveMaxSearchDepth(maximalSearchDepth)
        logger.info("Searching Recursively with depth {}", maximalSearchDepth)

        val eligibleFiles = mutableListOf<File>()

        for (iteration in maximalSearchDepth downTo 1) {
            val nextDepth = searchCurrentDepth(eligibleFiles, matches)
            replaceCurrentDepth(nextDepth)
            progressUpdater.updateProgress((iteration + 1).toDouble() / maximalSearchDepth.toDouble(), "Looking through directories")
        }

        progressUpdater.updateProgress(1.0, "Finished looking through directories")
        return eligibleFiles
    }

    private fun searchCurrentDepth(addTo: MutableList<File>, matches: (File) -> Boolean): MutableList<File> {
        val nextDepth: MutableList<File> = mutableListOf()

        filesInCurrentDepth.forEach { file ->
            if (matches(file)) addTo.add(file)
            if (file.isDirectory) nextDepth.addAll(file.listFiles())
        }

        return nextDepth
    }

    private fun replaceCurrentDepth(nextDepth: MutableList<File>) {
        filesInCurrentDepth.clear()
        filesInCurrentDepth.addAll(nextDepth)
    }

    fun getFilesInCurrentDepth() = filesInCurrentDepth.toList()

    companion object RecursiveFileSearcherFactory {
        private val logger = JaKoLogger.createLogger(this::class.java)

        fun createRecursiveFileSearcher(entryPoint: File): RecursiveFileSearcher {
            if (!entryPoint.exists()) throw NoSuchFileException(entryPoint)
            return RecursiveFileSearcher(entryPoint.listFiles().toMutableList())
        }
    }
}