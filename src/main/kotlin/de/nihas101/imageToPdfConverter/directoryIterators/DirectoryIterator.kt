package de.nihas101.imageToPdfConverter.directoryIterators

import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator.ImageDirectoriesIteratorFactory.createImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ZipFileIterator.ZipFileIteratorFactory.createZipFileIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ZipFilesIterator.ZipFilesIteratorFactory.createZipFilesIterator
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import de.nihas101.imageToPdfConverter.tasks.Cancellable
import java.io.File

abstract class DirectoryIterator : Cancellable {
    protected var directory: File? = null
    protected var cancelled = false

    open fun setupDirectory(directory: File) {
        this.directory = directory
    }

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

    override fun cancelTask() {
        cancelled = true
    }

    companion object DirectoryIteratorFactory {
        fun createDirectoryIterator(file: File, iteratorOptions: IteratorOptions): DirectoryIterator {
            val directoryIterator = createDirectoryIterator(iteratorOptions)
            directoryIterator.setupDirectory(file)
            return directoryIterator
        }

        fun createDirectoryIterator(iteratorOptions: IteratorOptions): DirectoryIterator {
            return when (iteratorOptions.multipleDirectories) {
                false -> createSingleDirectoryIterator(iteratorOptions)
                true -> createMultipleDirectoriesIterator(iteratorOptions)
            }
        }

        private fun createSingleDirectoryIterator(iteratorOptions: IteratorOptions): DirectoryIterator {
            return when (iteratorOptions.zipFiles) {
                false -> createImageFilesIterator()
                true -> createZipFileIterator(iteratorOptions.deleteOnExit)
            }
        }

        private fun createMultipleDirectoriesIterator(iteratorOptions: IteratorOptions): DirectoryIterator {
            return when (iteratorOptions.zipFiles) {
                false -> createImageDirectoriesIterator()
                true -> createZipFilesIterator(iteratorOptions.deleteOnExit)
            }
        }
    }
}