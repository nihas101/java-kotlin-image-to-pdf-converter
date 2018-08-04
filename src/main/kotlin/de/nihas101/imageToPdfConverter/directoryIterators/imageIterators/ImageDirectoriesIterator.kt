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

package de.nihas101.imageToPdfConverter.directoryIterators.imageIterators

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreDirectoriesException
import de.nihas101.imageToPdfConverter.util.JaKoLogger
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.io.File

class ImageDirectoriesIterator private constructor() : DirectoryIterator() {
    private var directories: MutableList<File> = mutableListOf()
    private var currentIndex = 0

    override fun setupDirectory(directory: File, progressUpdater: ProgressUpdater) {
        super.setupDirectory(directory, progressUpdater)
        if (directory.isDirectory)
            directories = super.setupFiles(createFileFilter(directory, progressUpdater) { file -> isImageDirectory(file) })
    }

    override fun numberOfFiles(): Int = directories.size

    override fun getParentDirectory(): File = directory!!

    override fun getFiles(): MutableList<File> = directories

    override fun add(index: Int, file: File): Boolean {
        val arguments = Array<Any>(2) {}
        arguments[0] = file.name
        arguments[1] = index

        return if (isImageDirectory(file)) {
            directories.add(index, file)
            logger.info("Added {} at index {}", arguments)
            true
        } else {
            logger.info("Ignored addition of {} at index {} as it is no image", arguments)
            false
        }
    }

    override fun add(file: File): Boolean {
        return if (isImageDirectory(file)) {
            directories.add(file)
            logger.info("Added {}", file.name)
            true
        } else {
            logger.info("Ignored addition of {} as it is no image", file.name)
            false
        }
    }

    override fun addAll(files: List<File>) =
            directories.addAll(files.filter { file -> isImageDirectory(file) })

    override fun remove(file: File): Boolean {
        logger.info("Removed {}", file.name)
        return directories.remove(file)
    }

    override fun nextFile(): File {
        if (currentIndex < directories.size) return directories[currentIndex++]
        throw NoMoreDirectoriesException(directory!!)
    }

    override fun resetIndex() {
        logger.info("{}", "Index reset")
        currentIndex = 0
    }

    override fun getFile(index: Int): File = directories[index]

    companion object ImageDirectoriesIteratorFactory {
        private val logger = JaKoLogger.createLogger(ImageDirectoriesIterator::class.java)

        fun createImageDirectoriesIterator(): ImageDirectoriesIterator = ImageDirectoriesIterator()
        fun isImageDirectory(directory: File) = directory.isDirectory && containsImage(directory)
        private fun containsImage(directory: File): Boolean {
            directory.listFiles().forEach { file -> if (ImageFilesIterator.isImage(file)) return true }
            return false
        }
    }
}