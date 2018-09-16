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
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator.ImageFilesIteratorFactory.isImage
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ImageUnZipper.ZipFileIteratorFactory.canUnzip
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import de.nihas101.imageToPdfConverter.util.JaKoLogger
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import de.nihas101.imageToPdfConverter.util.RecursiveFileSearcher.RecursiveFileSearcherFactory.createRecursiveFileSearcher
import java.io.File

open class ImageDirectoriesIterator protected constructor(iteratorOptions: IteratorOptions) : DirectoryIterator(iteratorOptions) {
    override fun getParentDirectory(): File = directory!!

    override fun canBeAdded(file: File) = isImageDirectory(file) || canUnzip(file)

    override fun add(index: Int, file: File): Boolean {
        val arguments = Array<Any>(2) {}
        arguments[0] = file.name
        arguments[1] = index

        return when {
            isImageDirectory(file) -> {
                files.add(index, file)
                logger.info("Added {} at index {}", arguments)
                true
            }
            canUnzip(file) -> add(unzip(file))
            else -> {
                logger.info("Ignored addition of {} at index {} as it cannot be added", arguments)
                false
            }
        }
    }

    override fun addDirectory(file: File, progressUpdater: ProgressUpdater): Boolean {
        super.setupDirectory(file, progressUpdater)

        val recursiveFileSearcher = createRecursiveFileSearcher(file)
        val files = recursiveFileSearcher.searchRecursively(iteratorOptions.maximalSearchDepth, progressUpdater)
        { fileToAdd -> canBeAdded(fileToAdd) }

        return if (files.isEmpty()) {
            setupDirectory(file)
            true
        } else {
            addAll(files.filter(this@ImageDirectoriesIterator::canBeAdded), progressUpdater)
        }
    }

    override fun addAll(filesToAdd: List<File>, progressUpdater: ProgressUpdater): Boolean {
        val processedFiles = mutableListOf<File>()

        if (filesToAdd.isEmpty()) return true
        if (files.isEmpty()) setupDirectory(filesToAdd[0].parentFile, progressUpdater)
        val outOf = filesToAdd.size

        filesToAdd.forEachIndexed { index, file ->
            processedFiles.addAll(processFile(file))
            progressUpdater.updateProgress((index + 1).toDouble() / outOf.toDouble(), file)
        }

        return files.addAll(processedFiles)
    }

    private fun processFile(file: File): MutableList<File> {
        val processedFiles = mutableListOf<File>()

        if (canUnzip(file)) processedFiles.addAll(tryToUnzip(file))
        else if (isImageDirectory(file)) processedFiles.add(file)

        return processedFiles
    }

    private fun tryToUnzip(file: File): List<File> {
        val unzippedFiles = mutableListOf<File>()

        try {
            unzippedFiles.add(unzip(file))
        } catch (exception: ExtensionNotSupportedException) {
            logger.info("Skipping {} as it cannot be unzipped.", file)
        }

        return unzippedFiles
    }

    private fun isImageDirectory(directory: File): Boolean {
        if (!directory.isDirectory) return false

        directory.listFiles().forEach { file -> if (isImage(file)) return true }
        return false
    }

    companion object ImageDirectoriesIteratorFactory {
        private val logger = JaKoLogger.createLogger(this::class.java)!!

        fun createImageDirectoriesIterator(iteratorOptions: IteratorOptions): ImageDirectoriesIterator = ImageDirectoriesIterator(iteratorOptions)
    }
}