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
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.ExtensionNotSupportedException
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ImageUnZipper.ZipFileIteratorFactory.canUnzip
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import de.nihas101.imageToPdfConverter.util.JaKoLogger
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

open class ImageFilesIterator protected constructor(iteratorOptions: IteratorOptions) : DirectoryIterator(iteratorOptions) {
    override fun getParentDirectory(): File = directory!!

    override fun canBeAdded(file: File): Boolean = isImage(file) || canUnzip(file)

    override fun addDirectory(file: File, progressUpdater: ProgressUpdater): Boolean {
        super.setupDirectory(file, progressUpdater)

        return when {
            file.isDirectory -> addAll(file.listFiles().filter(this@ImageFilesIterator::canBeAdded), progressUpdater)
            canUnzip(file) -> addAll(unzip(file, progressUpdater).listFiles().toList(), progressUpdater)
            isImage(file) -> addAll(listOf(file), progressUpdater)
            else -> false
        }
    }

    override fun addAll(filesToAdd: List<File>, progressUpdater: ProgressUpdater): Boolean {
        val processedFiles = mutableListOf<File>()

        if (filesToAdd.isEmpty()) return true
        if (files.isEmpty()) setupDirectory(filesToAdd[0].parentFile, progressUpdater)
        val outOf = filesToAdd.size

        filesToAdd.forEachIndexed { index, file ->
            if (canUnzip(file)) {
                try {
                    processedFiles.add(unzip(file, progressUpdater))
                } catch (exception: ExtensionNotSupportedException) {
                    logger.info("Skipping {}, as it cannot be unzipped", file)
                }
            } else processedFiles.add(file)
            progressUpdater.updateProgress((index + 1).toDouble() / outOf.toDouble(), file)
        }

        return files.addAll(processedFiles)
    }

    companion object ImageFilesIteratorFactory {
        private val logger = JaKoLogger.createLogger(this::class.java)

        fun createImageFilesIterator(iteratorOptions: IteratorOptions): ImageFilesIterator = ImageFilesIterator(iteratorOptions)

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