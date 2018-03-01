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

package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.ExtensionNotSupportedException
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.FileIsDirectoryException
import de.nihas101.imageToPdfConverter.tasks.Cancellable
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.imageio.ImageIO

class ImageUnZipper private constructor(private val zipInputStream: ZipInputStream) : Cancellable {
    var cancelled = false

    fun unzip(unzipInto: File, deleteOnExit: Boolean = false) {
        unzip({ zipEntry -> createFile("${unzipInto.absolutePath}/${zipEntry.name}", deleteOnExit) })
        System.gc()
    }

    private fun unzip(fileFactory: (ZipEntry) -> File) {
        zipInputStream.use { _ -> unzipImages(fileFactory) }
    }

    private fun unzipImages(fileFactory: (ZipEntry) -> File) {
        var zipEntry = zipInputStream.getNextEntry()

        while (zipEntry != null) {
            if (cancelled) throw InterruptedException()
            try {
                unzipImage(zipEntry, fileFactory)
            } catch (exception: FileIsDirectoryException) {
                /* SKIP ENTRY */
            }

            zipEntry = zipInputStream.getNextEntry()
        }
    }

    override fun cancelTask() {
        cancelled = true
    }

    private fun unzipImage(zipEntry: ZipEntry, fileFactory: (ZipEntry) -> File) {
        val file: File = fileFactory(zipEntry)
        val bufferedImage = ImageIO.read(zipInputStream)
        if (bufferedImage != null)
            ImageIO.write(bufferedImage, extractExtension(zipEntry), file)
    }

    private fun extractExtension(zipEntry: ZipEntry): String {
        return zipEntry.name.substring(zipEntry.name.lastIndexOf(".") + 1)
    }

    companion object ZipFileIteratorFactory {
        fun createImageUnZipper(file: File): ImageUnZipper {
            if (canUnzip(file))
                return ImageUnZipper(createZipInputStream(file))
            else throw ExtensionNotSupportedException(file.extension)
        }

        fun canUnzip(file: File): Boolean {
            return (!file.isDirectory && file.extension == "zip")
        }

        private fun createZipInputStream(file: File): ZipInputStream {
            val fileInputStream = FileInputStream(file)
            return ZipInputStream(BufferedInputStream(fileInputStream))
        }

        fun createFile(path: String, deleteOnExit: Boolean = false): File {
            val file = File(path)

            if (deleteOnExit) file.deleteOnExit()

            if (file.extension == "") {
                file.mkdir()
                throw FileIsDirectoryException(file)
            } else {
                file.createNewFile()
                return file
            }
        }
    }
}