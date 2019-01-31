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

package de.nihas101.image_to_pdf_converter.directory_iterators

import de.nihas101.image_to_pdf_converter.directory_iterators.exceptions.ExtensionNotSupportedException
import de.nihas101.image_to_pdf_converter.directory_iterators.exceptions.NoMoreFilesException
import de.nihas101.image_to_pdf_converter.directory_iterators.image_iterators.ImageDirectoriesIterator.ImageDirectoriesIteratorFactory.createImageDirectoriesIterator
import de.nihas101.image_to_pdf_converter.directory_iterators.image_iterators.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import de.nihas101.image_to_pdf_converter.directory_iterators.zip_iterators.ImageUnZipper
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.IteratorOptions
import de.nihas101.image_to_pdf_converter.tasks.Cancellable
import de.nihas101.image_to_pdf_converter.util.JaKoLogger
import de.nihas101.image_to_pdf_converter.util.ProgressUpdater
import de.nihas101.image_to_pdf_converter.util.TrivialProgressUpdater
import java.io.File

abstract class DirectoryIterator(protected val iteratorOptions: IteratorOptions) : Cancellable {
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

    fun add(file: File): Boolean {
        if (files.isEmpty()) setupDirectory(file.parentFile, TrivialProgressUpdater())
        return add(files.size, file)
    }

    abstract fun add(index: Int, file: File): Boolean

    abstract fun addAll(filesToAdd: List<File>, progressUpdater: ProgressUpdater = TrivialProgressUpdater()): Boolean

    abstract fun canBeAdded(file: File): Boolean

    abstract fun addDirectory(file: File, progressUpdater: ProgressUpdater): Boolean

    fun unzip(file: File, progressUpdater: ProgressUpdater = TrivialProgressUpdater()): File {
        val unzipInto = File("${file.parent.trim()}/${file.nameWithoutExtension.trim()}")
        logger.info("Unzipping {}", file.name)

        return try {
            val imageUnZipper = ImageUnZipper.createImageUnZipper(file)
            imageUnZipper.unzip(unzipInto, progressUpdater, iteratorOptions.deleteOnExit)
        } catch (exception: ExtensionNotSupportedException) {
            logger.error("{}. Skipping directory.", exception)
            /* Proceed with empty unzip directory */
            throw exception
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
        private val logger = JaKoLogger.createLogger(this::class.java)!!

        fun createDirectoryIterator(file: File, iteratorOptions: IteratorOptions): DirectoryIterator {
            val directoryIterator = createDirectoryIterator(iteratorOptions)
            directoryIterator.addDirectory(file, TrivialProgressUpdater())
            return directoryIterator
        }

        fun createDirectoryIterator(iteratorOptions: IteratorOptions): DirectoryIterator {
            return when (iteratorOptions.multipleDirectories) {
                false -> createImageFilesIterator(iteratorOptions)
                true -> createImageDirectoriesIterator(iteratorOptions)
            }
        }
    }
}