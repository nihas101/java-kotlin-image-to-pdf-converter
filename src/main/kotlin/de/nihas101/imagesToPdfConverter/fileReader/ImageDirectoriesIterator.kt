package de.nihas101.imagesToPdfConverter.fileReader

import java.io.File

class ImageDirectoriesIterator(private val directory: File): DirectoryIterator{
    private var imageFilesIterators: List<ImageFilesIterator> = setupDirectories(directory)
    private var currentIndex = 0

    companion object ImageDirectoriesIteratorFactory {
        fun createImageDirectoriesIterator(directory: File): ImageDirectoriesIterator = ImageDirectoriesIterator(directory)
    }

    fun getImageFilesIterators(): List<ImageFilesIterator> = imageFilesIterators

    fun getImageFilesIterator(index: Int): ImageFilesIterator = imageFilesIterators[index]

    fun nextImageFilesIterator(): ImageFilesIterator{
        if(currentIndex < imageFilesIterators.size) return imageFilesIterators[currentIndex++]
        throw NoMoreDirectoriesException(directory)
    }

    override fun nrOfFiles(): Int{
        var nrOfFiles = 0

        imageFilesIterators.forEach({
            imageFilesIterator -> nrOfFiles += imageFilesIterator.getFiles().size

        })

        return nrOfFiles
    }

    override fun getParentDirectory(): File = directory

    private fun setupDirectories(directory: File): List<ImageFilesIterator> {
        val directoryList =
                if(directory.isDirectory) directory.listFiles().filter { file -> file.isDirectory }
                else List(1, { _ -> directory }).filter { file -> file.isDirectory }

        return directoryList.map { file -> ImageFilesIterator.createImagesDirLoader(file) }
    }

    override fun getFiles(): List<File> {
        val files = MutableList(0,{ _ -> File("")})

        imageFilesIterators.forEach({
            imageFilesIterator -> files.add(imageFilesIterator.getParentDirectory())
        })

        return files
    }

    override fun nextFile(): File {
        return imageFilesIterators[currentIndex].nextFile()
    }

    override fun getFile(index: Int): File = imageFilesIterators[index].getParentDirectory()
}