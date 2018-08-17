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
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

open class ImageFilesIterator protected constructor() : DirectoryIterator() {
    override fun getParentDirectory(): File = directory!!

    override fun canBeAdded(file: File): Boolean = isImage(file)

    companion object ImageFilesIteratorFactory {
        fun createImageFilesIterator(): ImageFilesIterator = ImageFilesIterator()

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