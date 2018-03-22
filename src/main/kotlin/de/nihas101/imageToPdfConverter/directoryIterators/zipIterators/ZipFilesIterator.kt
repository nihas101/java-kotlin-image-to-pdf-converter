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

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator.ImageDirectoriesIteratorFactory.createImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ImageUnZipper.ZipFileIteratorFactory.createImageUnZipper
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.io.File

class ZipFilesIterator private constructor(private val deleteOnExit: Boolean) : DirectoryIterator() {
    private var imageDirectoriesIterator: ImageDirectoriesIterator = createImageDirectoriesIterator()
    private var imageUnZipper: ImageUnZipper? = null

    override fun setupDirectory(directory: File, progressUpdater: ProgressUpdater) {
        super.setupDirectory(directory, progressUpdater)

        // TODO: Track progress on unzipping!
        if (directory.isDirectory) unzipFilesInDirectory(progressUpdater)

        imageDirectoriesIterator = createImageDirectoriesIterator()
        imageDirectoriesIterator.setupDirectory(directory, progressUpdater)
    }

    private fun unzipFilesInDirectory(progressUpdater: ProgressUpdater) {
        val numberOfFiles = directory!!.listFiles().size.toDouble()

        directory!!.listFiles().forEachIndexed { index, file ->
            if (cancelled) throw InterruptedException()
            val unzipInto = makeUnzipDirectory(file, deleteOnExit)
            if (ImageUnZipper.canUnzip(file)) {
                imageUnZipper = createImageUnZipper(file)
                imageUnZipper!!.unzip(unzipInto, deleteOnExit = true)
            }
            progressUpdater.updateProgress(index.toDouble() / numberOfFiles, file)
        }
    }

    private fun makeUnzipDirectory(file: File, deleteOnExit: Boolean): File {
        val unzipDirectory = File("${file.parent}/${file.nameWithoutExtension}")

        if (deleteOnExit) unzipDirectory.deleteOnExit()
        unzipDirectory.mkdir()

        return unzipDirectory
    }

    override fun cancelTask() {
        super.cancelTask()
        if (imageUnZipper != null) imageUnZipper!!.cancelTask()
    }

    override fun nextFile() = imageDirectoriesIterator.nextFile()

    override fun getFile(index: Int) = imageDirectoriesIterator.getFile(index)

    override fun getFiles() = imageDirectoriesIterator.getFiles()

    override fun remove(file: File) = imageDirectoriesIterator.remove(file)

    override fun add(index: Int, file: File) = imageDirectoriesIterator.add(index, file)

    override fun add(file: File) = imageDirectoriesIterator.add(file)

    override fun addAll(files: List<File>) = imageDirectoriesIterator.addAll(files)

    override fun numberOfFiles() = imageDirectoriesIterator.numberOfFiles()

    override fun getParentDirectory() = imageDirectoriesIterator.getParentDirectory()

    override fun resetIndex() = imageDirectoriesIterator.resetIndex()

    companion object ZipFilesIteratorFactory {
        fun createZipFilesIterator(deleteOnExit: Boolean): ZipFilesIterator {
            return ZipFilesIterator(deleteOnExit)
        }
    }
}