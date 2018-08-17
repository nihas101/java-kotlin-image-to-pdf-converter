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
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ImageUnZipper.ZipFileIteratorFactory.createImageUnZipper
import de.nihas101.imageToPdfConverter.util.JaKoLogger
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import java.io.File


class ZipFileIterator private constructor(private val deleteOnExit: Boolean) : ImageFilesIterator() {
    private var imageUnZipper: ImageUnZipper? = null

    override fun cancelTask() {
        super.cancelTask()
        if (imageUnZipper != null) imageUnZipper!!.cancelTask()
    }

    override fun addDirectory(file: File, progressUpdater: ProgressUpdater): Boolean {
        super.setupDirectory(file, progressUpdater)
        val unzipInto = File("${file.parent.trim()}/${file.nameWithoutExtension.trim()}")
        logger.info("Unzipping {}", file.name)
        try {
            imageUnZipper = createImageUnZipper(file)
            imageUnZipper!!.unzip(unzipInto, progressUpdater, deleteOnExit)
        } catch (exception: ExtensionNotSupportedException) {
            logger.error("{}. Skipping directory.", exception)
            /* Proceed with empty unzip directory */
        }

        return super.addDirectory(unzipInto, progressUpdater)
    }

    companion object ZipFileIteratorFactory {
        private val logger = JaKoLogger.createLogger(ZipFileIterator::class.java)

        fun createZipFileIterator(deleteOnExit: Boolean): ZipFileIterator {
            return ZipFileIterator(deleteOnExit)
        }
    }
}