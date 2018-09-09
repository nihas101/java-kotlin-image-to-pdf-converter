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
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator.ImageFilesIteratorFactory.isImage
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ImageUnZipper.ZipFileIteratorFactory.canUnzip
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import de.nihas101.imageToPdfConverter.util.RecursiveFileSearcher
import java.io.File

open class ImageDirectoriesIterator protected constructor(iteratorOptions: IteratorOptions) : DirectoryIterator(iteratorOptions) {
    override fun getParentDirectory(): File = directory!!

    override fun canBeAdded(file: File) = isImageDirectory(file) || canUnzip(file)

    override fun addDirectory(file: File, progressUpdater: ProgressUpdater, maximalSearchDepth: Int): Boolean {
        super.setupDirectory(file, progressUpdater)

        val recursiveFileSearcher = RecursiveFileSearcher.createRecursiveFileSearcher(file)
        val files = recursiveFileSearcher.searchRecursively({ fileToAdd -> canBeAdded(fileToAdd) }, maximalSearchDepth)

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
            if (canUnzip(file)) processedFiles.add(unzip(file, progressUpdater))
            else processedFiles.add(file)
            progressUpdater.updateProgress((index + 1).toDouble() / outOf.toDouble(), file)
        }

        return files.addAll(processedFiles)
    }

    private fun isImageDirectory(directory: File): Boolean {
        if (!directory.isDirectory) return false

        directory.listFiles().forEach { file -> if (isImage(file)) return true }
        return false
    }

    companion object ImageDirectoriesIteratorFactory {
        fun createImageDirectoriesIterator(iteratorOptions: IteratorOptions): ImageDirectoriesIterator = ImageDirectoriesIterator(iteratorOptions)
    }
}