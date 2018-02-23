package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.ExtensionNotSupportedException
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.FileIsDirectoryException
import de.nihas101.imageToPdfConverter.util.Constants.BUFFER
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class Unzipper private constructor(private val zipInputStream: ZipInputStream) {
    fun unzip(unzipInto: File, deleteOnExit: Boolean) {
        unzip({ fileName -> createFileOutputStream("${unzipInto.absolutePath}/$fileName", deleteOnExit) })
    }

    fun unzip(outputStreamFactory: (String) -> OutputStream) {
        zipInputStream.use { _ -> unzipFile(outputStreamFactory) }
    }

    private fun unzipFile(outputStreamFactory: (String) -> OutputStream) {
        var zipEntry = zipInputStream.getNextEntry()

        while (zipEntry != null) {
            try {
                unzipEntry(zipEntry, outputStreamFactory)
            } catch (exception: FileIsDirectoryException) {
                /* SKIP ENTRY */
            }

            zipEntry = zipInputStream.getNextEntry()
        }
    }

    private fun unzipEntry(zipEntry: ZipEntry, outputStreamFactory: (String) -> OutputStream) {
        val data = ByteArray(BUFFER)
        val outputStream: OutputStream = outputStreamFactory(zipEntry.name)

        var count = zipInputStream.read(data)

        outputStream.use {
            while (count != -1) {
                outputStream.write(data)
                count = zipInputStream.read(data)
            }
        }
    }

    companion object ZipFileIteratorFactory {
        fun createUnzipper(file: File): Unzipper {
            println(file)
            if (file.extension == "zip") // TODO: Check which extensions are supported
                return Unzipper(createZipInputStream(file))
            else throw ExtensionNotSupportedException(file.extension)
        }

        private fun createZipInputStream(file: File): ZipInputStream {
            val fileInputStream = FileInputStream(file)
            return ZipInputStream(BufferedInputStream(fileInputStream))
        }

        fun createFileOutputStream(path: String, deleteOnExit: Boolean = false): FileOutputStream {
            val file = File(path)

            if (deleteOnExit) file.deleteOnExit()

            if (file.extension == "") {
                file.mkdir()
                throw FileIsDirectoryException(file)
            } else {
                file.createNewFile()
                return FileOutputStream(file)
            }
        }
    }
}