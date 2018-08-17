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

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.ExtensionNotSupportedException
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.FileIsDirectoryException
import de.nihas101.imageToPdfConverter.tasks.Cancellable
import de.nihas101.imageToPdfConverter.util.JaKoLogger
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import de.nihas101.imageToPdfConverter.util.TrivialProgressUpdater
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.Thread.yield
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import javax.imageio.ImageIO

class ImageUnZipper private constructor(private val file: File) : Cancellable {
    var cancelled = false
    private val zipInputStream = createZipInputStream(file)
    private val numberOfEntries: Int = ZipFile(file).size()

    fun unzip(unzipInto: File, progressUpdater: ProgressUpdater = TrivialProgressUpdater(), deleteOnExit: Boolean = false): File {
        val unzipIntoTrimmed = createDirectory(unzipInto, deleteOnExit)

        unzip(progressUpdater) { zipEntry -> createFile("${unzipIntoTrimmed.absolutePath}/${zipEntry.name.trim()}", deleteOnExit) }
        System.gc()
        return unzipIntoTrimmed
    }

    private fun createDirectory(unzipInto: File, deleteOnExit: Boolean): File {
        val unzipIntoTrimmed = trimDirectory(unzipInto)
        unzipIntoTrimmed.mkdir()
        if (deleteOnExit) unzipIntoTrimmed.deleteOnExit()

        return unzipIntoTrimmed
    }

    private fun trimDirectory(file: File): File {
        return File(file.absolutePath.trim().trim { char -> char == '.' }.trim())
    }

    private fun unzip(progressUpdater: ProgressUpdater, fileFactory: (ZipEntry) -> File) {
        zipInputStream.use { _ -> unzipImages(progressUpdater, fileFactory) }
    }

    private fun unzipImages(progressUpdater: ProgressUpdater, fileFactory: (ZipEntry) -> File) {
        var zipEntry = zipInputStream.nextEntry

        for (index in 0 until numberOfEntries) {
            if (cancelled) throw InterruptedException()

            progressUpdater.updateProgress(index.toDouble() / numberOfEntries.toDouble(), file)

            try {
                unzipImage(zipEntry, fileFactory)
            } catch (exception: FileIsDirectoryException) {
                logger.info("Skipping {}, as it is a directory", file.name)
            }

            zipInputStream.closeEntry()
            zipEntry = zipInputStream.nextEntry
            yield() // Yield so FX-Thread gets a chance to update its contents
        }

    }

    override fun cancelTask() {
        cancelled = true
    }

    private fun unzipImage(zipEntry: ZipEntry, fileFactory: (ZipEntry) -> File) {
        val file: File = fileFactory(zipEntry)
        logger.info("Unzipping {}", file.name)
        try {
            val bufferedImage = ImageIO.read(zipInputStream)
            if (bufferedImage != null)
                ImageIO.write(bufferedImage, extractExtension(zipEntry), file)
        } catch (exception: IOException) {
            logger.error("{}", exception)
        } catch (exception: IllegalArgumentException) {
            logger.error("{}", exception)
        }
    }

    private fun extractExtension(zipEntry: ZipEntry): String {
        return zipEntry.name.substring(zipEntry.name.lastIndexOf(".") + 1)
    }

    companion object ZipFileIteratorFactory {
        private val logger = JaKoLogger.createLogger(ImageUnZipper::class.java)

        fun createImageUnZipper(file: File): ImageUnZipper {
            if (canUnzip(file)) {
                return ImageUnZipper(file)
            } else throw ExtensionNotSupportedException(file.extension)
        }

        fun canUnzip(file: File): Boolean {
            return (!file.isDirectory && file.extension == "zip")
        }

        fun createZipInputStream(file: File): ZipInputStream {
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