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
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreImagesException
import de.nihas101.imageToPdfConverter.util.JaKoLogger
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

class ImageFilesIterator private constructor() : DirectoryIterator() {
    private var files: MutableList<File> = mutableListOf()
    private var currentIndex = 0

    override fun setupDirectory(directory: File, progressUpdater: ProgressUpdater) {
        super.setupDirectory(directory, progressUpdater)
        files = if (directory.isDirectory)
            setupFiles(createFileFilter(directory, progressUpdater) { file -> isImage(file) })
        else
            List(1) { _ -> directory }.filter { file -> isImage(file) }.toMutableList()
    }

    override fun getFile(index: Int): File = files[index]

    override fun getFiles(): MutableList<File> = files

    override fun add(index: Int, file: File): Boolean {
        val arguments = Array<Any>(2) {}
        arguments[0] = file.name
        arguments[1] = index

        return if (isImage(file)) {
            files.add(index, file)
            logger.info("Added {} at index {}", arguments)
            true
        } else {
            logger.info("Ignored addition of {} at index {} as it is no image", arguments)
            false
        }
    }

    override fun add(file: File): Boolean {
        return if (isImage(file)) {
            files.add(file)
            logger.info("Added {}", file.name)
            true
        } else {
            logger.info("Ignored addition of {} as it is no image", file.name)
            false
        }
    }

    override fun addAll(files: List<File>): Boolean {
        return this.files.addAll(files.filter { file -> isImage(file) })
    }

    override fun remove(file: File): Boolean {
        logger.info("Removed {}", file.name)
        return files.remove(file)
    }

    override fun nextFile(): File {
        if (currentIndex < files.size) return files[currentIndex++]
        else throw NoMoreImagesException(directory!!)
    }

    override fun resetIndex() {
        logger.info("{}", "Index reset")
        currentIndex = 0
    }

    override fun numberOfFiles(): Int = files.size

    override fun getParentDirectory(): File = directory!!

    companion object ImageFilesIteratorFactory {
        fun createImageFilesIterator(): ImageFilesIterator = ImageFilesIterator()
        private val logger = JaKoLogger.createLogger(ImageFilesIterator::class.java)

        fun isImage(file: File): Boolean {
            val image: BufferedImage?
            if (file.isDirectory || !file.exists()) return false

            try {
                image = ImageIO.read(file)
            } catch (exception: IOException) {
                return false
            }

            return (image != null) && !exclude(file)
        }

        private fun exclude(file: File): Boolean {
            return "gif" == file.extension
        }
    }
}