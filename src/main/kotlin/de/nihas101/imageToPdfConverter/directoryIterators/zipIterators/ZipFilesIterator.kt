package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import java.io.File

class ZipFilesIterator : DirectoryIterator {

    /* TODO: Create ImageDirectoryIterator and pass the newly unzipped files to it */
    /* TODO: Add this as possibility for user */

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

    override fun numberOfFiles(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getParentDirectory(): File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetIndex() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object ZipFilesIteratorFactory {
        fun createZipFilesIterator(file: File): ZipFilesIterator {
            return ZipFilesIterator()
        }
    }
}