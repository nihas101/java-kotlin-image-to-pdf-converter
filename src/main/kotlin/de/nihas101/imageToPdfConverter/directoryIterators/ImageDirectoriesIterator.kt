package de.nihas101.imageToPdfConverter.directoryIterators

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreDirectoriesException
import java.io.File

class ImageDirectoriesIterator private constructor(private val directory: File) : DirectoryIterator {
    private var directories: MutableList<File> = setupDirectories(directory)
    private var currentIndex = 0

    override fun nrOfFiles(): Int = directories.size

    override fun getParentDirectory(): File = directory

    private fun setupDirectories(directory: File) =
            directory.listFiles().filter { file -> isImageDirectory(file) }.toMutableList()

    override fun getFiles(): MutableList<File> = directories

    override fun add(index: Int, file: File): Boolean {
        return if (isImageDirectory(file)) {
            directories.add(index, file)
            true
        } else false
    }

    override fun add(file: File): Boolean {
        return if (isImageDirectory(file)) {
            directories.add(file)
            true
        } else false
    }

    override fun addAll(files: List<File>) =
            directories.addAll(files.filter { file -> isImageDirectory(file) })

    override fun remove(file: File) =
            directories.remove(file)

    override fun nextFile(): File {
        if (currentIndex < directories.size) return directories[currentIndex++]
        throw NoMoreDirectoriesException(directory)
    }

    override fun resetIndex() {
        currentIndex = 0
    }

    override fun getFile(index: Int): File = directories[index]

    private fun isImageDirectory(directory: File) = directory.isDirectory && containsImage(directory)

    private fun containsImage(directory: File): Boolean {
        directory.listFiles().forEach { file -> if (ImageFilesIterator.isImage(file)) return true }
        return false
    }

    companion object ImageDirectoriesIteratorFactory {
        fun createImageDirectoriesIterator(directory: File): ImageDirectoriesIterator = ImageDirectoriesIterator(directory)
    }
}