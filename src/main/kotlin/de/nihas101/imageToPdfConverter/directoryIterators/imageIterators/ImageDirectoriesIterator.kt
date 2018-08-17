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
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.io.File

open class ImageDirectoriesIterator protected constructor() : DirectoryIterator() {
    override fun getParentDirectory(): File = directory!!

    override fun add(index: Int, file: File): Boolean =
            super.add(index, file) { fileToAdd -> isImageDirectory(fileToAdd) }

    override fun addAll(filesToAdd: List<File>, progressUpdater: ProgressUpdater): Boolean {
        return super.addAll(filesToAdd, progressUpdater) { directory -> isImageDirectory(directory) }
    }

    companion object ImageDirectoriesIteratorFactory {
        fun createImageDirectoriesIterator(): ImageDirectoriesIterator = ImageDirectoriesIterator()
        fun isImageDirectory(directory: File) = directory.isDirectory && containsImage(directory)
        private fun containsImage(directory: File): Boolean {
            directory.listFiles().forEach { file -> if (ImageFilesIterator.isImage(file)) return true }
            return false
        }
    }
}