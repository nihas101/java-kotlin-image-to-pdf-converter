package de.nihas101.imageToPdfConverter.directoryIterators

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class ZipFileIterator(file: File) : DirectoryIterator {
    init {
        if (file.extension == "zip") { // TODO: Check which extensions are supported
            /* TODO: Create folder and unzip into it */
            val zipInputStream = createZipInputStream(file)
            zipInputStream.use { _ -> unzipFile(zipInputStream) }

            /* TODO: Create ImageFilesIterator and pass the newly unzipped files to it */
            /* TODO: Make it so ImageDirectoriesIterator and ImageFilesIterator can delete a folder after creating a PDF */
            /* TODO: Then add that as an option! for the user */
        }
    }

    private fun createZipInputStream(file: File): ZipInputStream {
        val fileInputStream = FileInputStream(file)
        return ZipInputStream(BufferedInputStream(fileInputStream))
    }

    private fun unzipFile(zipInputStream: ZipInputStream) {
        var zipEntry = zipInputStream.getNextEntry()

        while (zipEntry != null) {
            unzipEntry(zipEntry, zipInputStream)

            zipEntry = zipInputStream.getNextEntry()
        }
    }

    private fun unzipEntry(zipEntry: ZipEntry, zipInputStream: ZipInputStream) {
        val BUFFER = 2048
        val data = ByteArray(BUFFER)
        val fos = FileOutputStream(zipEntry.name) // TODO: Set path into folder
        val dest = BufferedOutputStream(fos, BUFFER)
        var count = zipInputStream.read(data, 0, BUFFER)

        dest.use {
            while (count != -1) {
                dest.write(data, 0, count)
                count = zipInputStream.read(data, 0, BUFFER)
            }
        }
    }

    override fun nextFile(): File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFile(index: Int): File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFiles(): MutableList<File> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(file: File): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun add(index: Int, file: File): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun add(file: File): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addAll(files: List<File>): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun nrOfFiles(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getParentDirectory(): File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetIndex() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object ZipFileIteratorFactory {
        fun createZipFileIterator(file: File): ZipFileIterator {
            return ZipFileIterator(file)
        }
    }
}