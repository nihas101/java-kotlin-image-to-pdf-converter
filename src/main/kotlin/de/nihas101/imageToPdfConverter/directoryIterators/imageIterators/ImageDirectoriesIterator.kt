/*
 Image2PDF is a program for converting images to PDFs with the use of iText 7
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
import java.io.File

class ImageDirectoriesIterator private constructor() : DirectoryIterator() {
    private var directories: MutableList<File> = mutableListOf()
    private var currentIndex = 0

    override fun setupDirectory(directory: File) {
        super.setupDirectory(directory)
        if (directory.isDirectory)
            directories = setupDirectories(directory)
    }

    private fun setupDirectories(directory: File): MutableList<File> {
        return directory.listFiles().filter { file ->
            if (cancelled) throw InterruptedException()
            isImageDirectory(file)
        }.toMutableList()
    }

    override fun numberOfFiles(): Int = directories.size

    override fun getParentDirectory(): File = directory!!

    override fun getFiles(): MutableList<File> = directories

    override fun add(index: Int, file: File): Boolean {
        return if (isImageDirectory(file)) {
            directories.add(index, file)
            true
        } else false
    }

    override fun add(file: File): Boolean {
        return if (isImageDirectory(file)) {
            directories.add(file)
            true
        } else false
    }

    override fun addAll(files: List<File>) =
            directories.addAll(files.filter { file -> isImageDirectory(file) })

    override fun remove(file: File) =
            directories.remove(file)

    override fun nextFile(): File {
        if (currentIndex < directories.size) return directories[currentIndex++]
        throw NoMoreDirectoriesException(directory!!)
    }

    override fun resetIndex() {
        currentIndex = 0
    }

    override fun getFile(index: Int): File = directories[index]

    private fun isImageDirectory(directory: File) = directory.isDirectory && containsImage(directory)

    private fun containsImage(directory: File): Boolean {
        directory.listFiles().forEach { file -> if (ImageFilesIterator.isImage(file)) return true }
        return false
    }

    companion object ImageDirectoriesIteratorFactory {
        fun createImageDirectoriesIterator(): ImageDirectoriesIterator = ImageDirectoriesIterator()
    }
}