package de.nihas101.imagesToPdfConverter.fileReader

import java.io.File

class ImageDirectoriesIterator(private val directory: File): DirectoryIterator{
    private var directories: List<File> = setupDirectories(directory)
    private var currentIndex = 0

    companion object ImageDirectoriesIteratorFactory {
        fun createImageDirectoriesIterator(directory: File): ImageDirectoriesIterator = ImageDirectoriesIterator(directory)
    }

    override fun nrOfFiles(): Int = directories.size

    override fun getParentDirectory(): File = directory

    private fun setupDirectories(directory: File): List<File> {
        val directoryList =
                if(directory.isDirectory) directory.listFiles().filter { file -> file.isDirectory }
                else List(1, { _ -> directory }).filter { file -> file.isDirectory }

        return directoryList.filter {
            file -> ImageFilesIterator.createImageFilesIterator(file).nrOfFiles() != 0
        }.map {
                    file -> ImageFilesIterator.createImageFilesIterator(file).getParentDirectory()
                }
    }

    override fun getFiles(): List<File> = directories

    override fun nextFile(): File {
        if(currentIndex < directories.size) return directories[currentIndex++]
        throw NoMoreDirectoriesException(directory)
    }

    override fun resetIndex() {
        currentIndex = 0
    }

    override fun getFile(index: Int): File = directories[index]
}