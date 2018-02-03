package de.nihas101.imagesToPdfConverter.fileReader

import java.io.File
import javax.imageio.ImageIO

class ImageFilesIterator private constructor(private val directory: File): DirectoryIterator {
    private var files: List<File> =
                        if(directory.isDirectory) directory.listFiles().filter { file -> isImage(file) }
                        else List(1, { _ -> directory }).filter { file -> isImage(file) }
    private var currentIndex = 0

    companion object ImageFilesIteratorFactory {
         fun createImageFilesIterator(directory: File): ImageFilesIterator = ImageFilesIterator(directory)
    }

    override fun getFile(index: Int): File = files[index]

    override fun getFiles(): List<File> = files

    override fun nextFile(): File{
        if(currentIndex < files.size) return files[currentIndex++]
        throw NoMoreImagesException(directory)
    }

    override fun resetIndex() {
        currentIndex = 0
    }

    override fun nrOfFiles(): Int = files.size

    override fun getParentDirectory(): File = directory

    private fun isImage(file: File): Boolean = file.isFile && (ImageIO.read(file) != null)
}