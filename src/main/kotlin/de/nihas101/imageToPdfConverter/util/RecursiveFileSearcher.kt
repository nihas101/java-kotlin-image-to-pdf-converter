package de.nihas101.imageToPdfConverter.util

import java.io.File

public class RecursiveFileSearcher private constructor(
        private val filesInCurrentDepth: MutableList<File> = mutableListOf()
) {
    fun searchRecursively(matches: (File) -> Boolean, maximalSearchDepth: Int): List<File> {
        if (maximalSearchDepth < 1) throw NonPositiveMaxSearchDepth(maximalSearchDepth)

        val eligibleFiles = mutableListOf<File>()

        for (iteration in maximalSearchDepth downTo 1) {
            val nextDepth = searchCurrentDepth(eligibleFiles, matches)
            replaceCurrentDepth(nextDepth)
        }

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
        fun createRecursiveFileSearcher(entryPoint: File): RecursiveFileSearcher {
            if (!entryPoint.exists()) throw NoSuchFileException(entryPoint)
            return RecursiveFileSearcher(entryPoint.listFiles().toMutableList())
        }
    }
}