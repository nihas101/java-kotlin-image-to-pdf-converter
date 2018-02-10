package de.nihas101.imagesToPdfConverter.directoryIterators

import de.nihas101.imagesToPdfConverter.directoryIterators.exceptions.NoMoreDirectoriesException
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
        val directoryList =
                if(directory.isDirectory) directory.listFiles().filter { file -> file.isDirectory }
                else List(1, { _ -> directory }).filter { file -> file.isDirectory }

        return directoryList.filter {
            file -> ImageFilesIterator.createImageFilesIterator(file).nrOfFiles() > 0 }.toMutableList()
    }

    override fun getFiles(): MutableList<File> = directories

    override fun add(index: Int, file: File): Boolean {
        return if(file.isDirectory && ImageFilesIterator.createImageFilesIterator(file).nrOfFiles() > 0){
            directories.add(file)
        }
        else false
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
}