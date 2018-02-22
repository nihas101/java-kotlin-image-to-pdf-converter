package de.nihas101.imageToPdfConverter.directoryIterators.imageIterators

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreImagesException
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImageFilesIterator private constructor(private val directory: File) : DirectoryIterator {
    private var files: MutableList<File> =
            if (directory.isDirectory) directory.listFiles().filter { file -> isImage(file) }.toMutableList()
            else List(1, { _ -> directory }).filter { file -> isImage(file) }.toMutableList()
    private var currentIndex = 0

    override fun getFile(index: Int): File = files[index]

    override fun getFiles(): MutableList<File> = files

    override fun add(index: Int, file: File): Boolean {
        return if (isImage(file)) {
            files.add(index, file)
            true
        } else false
    }

    override fun add(file: File): Boolean {
        return if (isImage(file)) {
            files.add(file)
            true
        } else false
    }

    override fun addAll(files: List<File>): Boolean {
        return this.files.addAll(files.filter { file -> isImage(file) })
    }

    override fun remove(file: File) =
            files.remove(file)

    override fun nextFile(): File {
        if (currentIndex < files.size) return files[currentIndex++]
        else throw NoMoreImagesException(directory)
    }

    override fun resetIndex() {
        currentIndex = 0
    }

    override fun nrOfFiles(): Int = files.size

    override fun getParentDirectory(): File = directory

    companion object ImageFilesIteratorFactory {
        fun createImageFilesIterator(directory: File): ImageFilesIterator = ImageFilesIterator(directory)

        fun isImage(file: File): Boolean {
            val image: BufferedImage?
            if (file.isDirectory || !file.exists()) return false

            try {
                image = ImageIO.read(file)
            } catch (exception: Exception) {
                return false
            }

            return (image != null) && !exclude(file)
        }

        private fun exclude(file: File): Boolean {
            return "gif" == file.extension
        }
    }
}