/*
 JaKoImage2PDF is a program for converting images to PDFs with the use of iText 7
 Copyright (C) 2018  Nikita Hasert

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.nihas101.imageToPdfConverter.directoryIterators

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreFilesException
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator.ImageDirectoriesIteratorFactory.createImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ZipFileIterator.ZipFileIteratorFactory.createZipFileIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ZipFilesIterator.ZipFilesIteratorFactory.createZipFilesIterator
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import de.nihas101.imageToPdfConverter.tasks.Cancellable
import de.nihas101.imageToPdfConverter.util.JaKoLogger
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import de.nihas101.imageToPdfConverter.util.TrivialProgressUpdater
import java.io.File

abstract class DirectoryIterator : Cancellable {
    private var currentIndex = 0
    protected val files = mutableListOf<File>()
    protected var directory: File? = null
    protected var cancelled = false

    protected open fun setupDirectory(directory: File, progressUpdater: ProgressUpdater = TrivialProgressUpdater()) {
        this.directory = directory
    }

    open fun nextFile(): File {
        if (currentIndex < files.size) return files[currentIndex++]
        throw NoMoreFilesException(directory!!)
    }

    fun getFile(index: Int): File = files[index]
    fun getFileList(): MutableList<File> = files

    fun remove(file: File): Boolean {
        val absolutePath = file.absolutePath

        files.forEachIndexed { index, dirFile ->
            if (dirFile.absolutePath == absolutePath) {
                files.removeAt(index)
                logger.info("Removed {}", file.name)
                return true
            }
        }

        return false
    }

    abstract fun add(index: Int, file: File): Boolean

    protected open fun add(index: Int, file: File, canBeAdded: (File) -> Boolean): Boolean {
        val arguments = Array<Any>(2) {}
        arguments[0] = file.name
        arguments[1] = index

        return if (canBeAdded(file)) {
            files.add(index, file)
            logger.info("Added {} at index {}", arguments)
            true
        } else {
            logger.info("Ignored addition of {} at index {} as it is no image", arguments)
            false
        }
    }

    fun add(file: File): Boolean {
        if (files.isEmpty()) setupDirectory(file.parentFile, TrivialProgressUpdater())
        return add(files.size, file)
    }

    abstract fun addAll(filesToAdd: List<File>, progressUpdater: ProgressUpdater = TrivialProgressUpdater()): Boolean

    fun addAll(filesToAdd: List<File>, progressUpdater: ProgressUpdater = TrivialProgressUpdater(), canBeAdded: (File) -> Boolean): Boolean {
        if (filesToAdd.isEmpty()) return true
        if (files.isEmpty()) setupDirectory(filesToAdd[0].parentFile, progressUpdater)
        val outOf = filesToAdd.size

        return files.addAll(filesToAdd.filterIndexed { index, file ->
            progressUpdater.updateProgress((index + 1).toDouble() / outOf.toDouble(), file)
            canBeAdded(file)
        })
    }

    open fun addDirectory(file: File, progressUpdater: ProgressUpdater): Boolean {
        return if (file.isDirectory) {
            val files = file.listFiles().toList()
            if (files.isEmpty()) return true
            addAll(files, progressUpdater)
        } else {
            progressUpdater.updateProgress(1.0, file)
            add(file)
        }
    }


    fun clear() = files.clear()
    fun numberOfFiles(): Int = files.size
    abstract fun getParentDirectory(): File

    fun resetIndex() {
        logger.info("{}", "Index reset")
        currentIndex = 0
    }

    override fun cancelTask() {
        cancelled = true
    }

    companion object DirectoryIteratorFactory {
        private val logger = JaKoLogger.createLogger(DirectoryIterator::class.java)

        fun createDirectoryIterator(file: File, iteratorOptions: IteratorOptions): DirectoryIterator {
            val directoryIterator = createDirectoryIterator(iteratorOptions)
            directoryIterator.addDirectory(file, TrivialProgressUpdater())
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
                false -> {
                    logger.info("{}", "Created ImageFilesIterator")
                    createImageFilesIterator()
                }
                true -> {
                    logger.info("{}", "Created ZipFileIterator")
                    createZipFileIterator(iteratorOptions.deleteOnExit)
                }
            }
        }

        private fun createMultipleDirectoriesIterator(iteratorOptions: IteratorOptions): DirectoryIterator {
            return when (iteratorOptions.zipFiles) {
                false -> {
                    logger.info("{}", "Created ImageDirectoriesIterator")
                    createImageDirectoriesIterator()
                }
                true -> {
                    logger.info("{}", "Created ZipFilesIterator")
                    createZipFilesIterator(iteratorOptions.deleteOnExit)
                }
            }
        }
    }
}