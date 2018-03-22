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

import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator.ImageDirectoriesIteratorFactory.createImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ZipFileIterator.ZipFileIteratorFactory.createZipFileIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ZipFilesIterator.ZipFilesIteratorFactory.createZipFilesIterator
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import de.nihas101.imageToPdfConverter.tasks.Cancellable
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import de.nihas101.imageToPdfConverter.util.TrivialProgressUpdater
import java.io.File

abstract class DirectoryIterator : Cancellable {
    protected var directory: File? = null
    protected var cancelled = false

    open fun setupDirectory(directory: File, progressUpdater: ProgressUpdater = TrivialProgressUpdater()) {
        this.directory = directory
    }

    protected fun createFileFilter(directory: File, progressUpdater: ProgressUpdater, filter: (File) -> Boolean): (Int, File) -> Boolean {
        if (directory.listFiles() == null) {
            return { _, file ->
                if (cancelled) throw InterruptedException()
                filter(file)
            }
        } else {
            val directorySize = directory.listFiles().size.toDouble()
            return { index, file ->
                if (cancelled) throw InterruptedException()
                progressUpdater.updateProgress(index.toDouble() / directorySize, file)
                filter(file)
            }
        }
    }

    protected open fun setupFiles(fileFilter: (Int, File) -> Boolean): MutableList<File> {
        if (directory!!.listFiles() == null) return emptyList<File>().toMutableList()

        val filteredFiles = directory!!.listFiles().filterIndexed({ index, file -> fileFilter(index, file) }).toMutableList()

        // TODO: Think of how to reset the progressUpdater afterwards

        return filteredFiles
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