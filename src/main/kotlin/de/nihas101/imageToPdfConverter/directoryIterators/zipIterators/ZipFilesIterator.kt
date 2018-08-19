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

package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ImageUnZipper.ZipFileIteratorFactory.createImageUnZipper
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import de.nihas101.imageToPdfConverter.util.RecursiveFileSearcher.RecursiveFileSearcherFactory.createRecursiveFileSearcher
import java.io.File

class ZipFilesIterator private constructor(private val deleteOnExit: Boolean, maximalSearchDepth: Int) : ImageDirectoriesIterator(maximalSearchDepth) {
    private var imageUnZipper: ImageUnZipper? = null

    override fun cancelTask() {
        super.cancelTask()
        if (imageUnZipper != null) imageUnZipper!!.cancelTask()
    }


    override fun addDirectory(file: File, progressUpdater: ProgressUpdater, maximalSearchDepth: Int): Boolean {
        super.setupDirectory(file, progressUpdater)

        val unzippedFiles = if (file.isDirectory) unzipFilesInDirectory(progressUpdater)
        else listOf()
        return super.addAll(unzippedFiles, progressUpdater)
    }

    private fun unzipFilesInDirectory(progressUpdater: ProgressUpdater): List<File> {
        val unzippedFiles = mutableListOf<File>()
        val recursiveFileSearcher = createRecursiveFileSearcher(directory!!)

        val zipFiles = recursiveFileSearcher.searchRecursively({ file -> file.extension == "zip" }, maximalSearchDepth)
        val numberOfFiles = zipFiles.size.toDouble()

        zipFiles.forEachIndexed { index, file ->
            if (cancelled) throw InterruptedException()

            progressUpdater.updateProgress((index + 1).toDouble() / numberOfFiles, file)

            val unzipIntoUntrimmed = File("${file.parent}/${file.nameWithoutExtension}")
            if (ImageUnZipper.canUnzip(file)) {
                imageUnZipper = createImageUnZipper(file)
                val unzipIntoTrimmed = imageUnZipper!!.unzip(unzipIntoUntrimmed, deleteOnExit = deleteOnExit)
                unzippedFiles.add(unzipIntoTrimmed)
            }
        }
        return unzippedFiles
    }

    companion object ZipFilesIteratorFactory {
        fun createZipFilesIterator(deleteOnExit: Boolean, maximalSearchDepth: Int = 1): ZipFilesIterator {
            return ZipFilesIterator(deleteOnExit, maximalSearchDepth)
        }
    }
}