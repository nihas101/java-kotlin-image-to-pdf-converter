package de.nihas101.imageToPdfConverter.directoryIterators

import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator.ImageDirectoriesIteratorFactory.createImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ZipFileIterator.ZipFileIteratorFactory.createZipFileIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ZipFilesIterator
import de.nihas101.imageToPdfConverter.pdf.IteratorOptions
import java.io.File

abstract class DirectoryIterator {
    abstract fun nextFile(): File
    abstract fun getFile(index: Int): File
    abstract fun getFiles(): MutableList<File>
    abstract fun remove(file: File): Boolean
    abstract fun add(index: Int, file: File): Boolean
    abstract fun add(file: File): Boolean
    abstract fun addAll(files: List<File>): Boolean
    abstract fun numberOfFiles(): Int
    abstract fun getParentDirectory(): File
    abstract fun resetIndex()

    companion object DirectoryIteratorFactory {
        fun createDirectoryIterator(directory: File, iteratorOptions: IteratorOptions): DirectoryIterator {
            return when (iteratorOptions.multipleDirectories) {
                false -> createSingleDirectoryIterator(directory, iteratorOptions)
                true -> createMultipleDirectoriesIterator(directory, iteratorOptions)
            }
        }

        private fun createSingleDirectoryIterator(directory: File, iteratorOptions: IteratorOptions): DirectoryIterator {
            return when (iteratorOptions.zipFiles) {
                false -> createImageFilesIterator(directory)
                true -> createZipFileIterator(directory, iteratorOptions.deleteOnExit)
            }
        }

        private fun createMultipleDirectoriesIterator(directory: File, iteratorOptions: IteratorOptions): DirectoryIterator {
            return when (iteratorOptions.zipFiles) {
                false -> createImageDirectoriesIterator(directory)
                true -> ZipFilesIterator.createZipFilesIterator(directory, iteratorOptions.deleteOnExit)
            }
        }
    }
}