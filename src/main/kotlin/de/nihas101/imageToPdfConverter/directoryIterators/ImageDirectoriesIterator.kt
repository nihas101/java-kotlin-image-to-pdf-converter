package de.nihas101.imageToPdfConverter.directoryIterators

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreDirectoriesException
import java.io.File

class ImageDirectoriesIterator private constructor(private val directory: File): DirectoryIterator{
    private var directories: MutableList<File> = setupDirectories(directory)
    private var currentIndex = 0

    companion object ImageDirectoriesIteratorFactory {
        fun createImageDirectoriesIterator(directory: File): ImageDirectoriesIterator = ImageDirectoriesIterator(directory)
    }

    override fun nrOfFiles(): Int = directories.size

    override fun getParentDirectory(): File = directory

    private fun setupDirectories(directory: File): MutableList<File> {
        return directory.listFiles().filter { file -> isImageDirectory(file) }.toMutableList()
    }

    override fun getFiles(): MutableList<File> = directories

    override fun add(index: Int, file: File): Boolean {
        return if(isImageDirectory(file)){
            directories.add(index, file)
            true
        } else false
    }

    override fun add(file: File): Boolean {
        return if(isImageDirectory(file)){
            directories.add(file)
            true
        }else false
    }

    override fun addAll(files: List<File>): Boolean {
        return directories.addAll(files.filter { file -> isImageDirectory(file) })
    }

    override fun remove(file: File): Boolean {
        return directories.remove(file)
    }

    override fun nextFile(): File {
        if(currentIndex < directories.size) return directories[currentIndex++]
        throw NoMoreDirectoriesException(directory)
    }

    override fun resetIndex() {
        currentIndex = 0
    }

    override fun getFile(index: Int): File = directories[index]

    private fun isImageDirectory(file: File): Boolean{
        return file.isDirectory && ImageFilesIterator.createImageFilesIterator(file).nrOfFiles() > 0
    }
}