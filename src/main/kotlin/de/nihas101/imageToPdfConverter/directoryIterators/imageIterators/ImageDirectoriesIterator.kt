package de.nihas101.imageToPdfConverter.directoryIterators.imageIterators

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreDirectoriesException
import java.io.File

class ImageDirectoriesIterator private constructor() : DirectoryIterator() {
    private var directories: MutableList<File> = mutableListOf()
    private var currentIndex = 0

    override fun setupDirectory(directory: File) {
        super.setupDirectory(directory)
        directories = setupDirectories(directory)
    }

    private fun setupDirectories(directory: File) =
            directory.listFiles().filter { file ->
                if (cancelled) throw InterruptedException()
                isImageDirectory(file)
            }.toMutableList()

    override fun numberOfFiles(): Int = directories.size

    override fun getParentDirectory(): File = directory!!

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
        throw NoMoreDirectoriesException(directory!!)
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
        fun createImageDirectoriesIterator(): ImageDirectoriesIterator = ImageDirectoriesIterator()
    }
}