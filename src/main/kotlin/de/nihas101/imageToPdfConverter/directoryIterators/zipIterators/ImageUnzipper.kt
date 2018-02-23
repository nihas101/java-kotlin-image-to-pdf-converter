package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.ExtensionNotSupportedException
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.FileIsDirectoryException
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.imageio.ImageIO

class ImageUnzipper private constructor(private val zipInputStream: ZipInputStream) {
    fun unzip(unzipInto: File, deleteOnExit: Boolean = false) {
        unzip({ zipEntry -> createFile("${unzipInto.absolutePath}/${zipEntry.name}", deleteOnExit) })
    }

    private fun unzip(fileFactory: (ZipEntry) -> File) {
        zipInputStream.use { _ -> unzipImages(fileFactory) }
    }

    private fun unzipImages(fileFactory: (ZipEntry) -> File) {
        var zipEntry = zipInputStream.getNextEntry()

        while (zipEntry != null) {
            try {
                unzipImage(zipEntry, fileFactory)
            } catch (exception: FileIsDirectoryException) {
                /* SKIP ENTRY */
            }

            zipEntry = zipInputStream.getNextEntry()
        }
    }

    private fun unzipImage(zipEntry: ZipEntry, fileFactory: (ZipEntry) -> File) {
        val file: File = fileFactory(zipEntry)
        val bufferedImage = ImageIO.read(zipInputStream)
        ImageIO.write(bufferedImage, extractExtension(zipEntry), file)
    }

    private fun extractExtension(zipEntry: ZipEntry): String {
        return zipEntry.name.substring(zipEntry.name.lastIndexOf(".") + 1)
    }

    companion object ZipFileIteratorFactory {
        fun createImageUnzipper(file: File): ImageUnzipper {
            if (file.extension == "zip") // TODO: Check which extensions are supported
                return ImageUnzipper(createZipInputStream(file))
            else throw ExtensionNotSupportedException(file.extension)
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