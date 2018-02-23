package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.Unzipper.ZipFileIteratorFactory.createUnzipper
import java.io.File


class ZipFileIterator(private val file: File, deleteOnExit: Boolean) : DirectoryIterator {
    private val imageFilesIterator: ImageFilesIterator

    init {
        val unzipInto = makeUnzipDirectory(deleteOnExit)
        createUnzipper(file).unzip(unzipInto, deleteOnExit)
        imageFilesIterator = ImageFilesIterator.createImageFilesIterator(unzipInto)
    }

    private fun makeUnzipDirectory(deleteOnExit: Boolean): File {
        val unzipDirectory = File("${file.parent}/${file.nameWithoutExtension}")

        if (deleteOnExit) unzipDirectory.deleteOnExit()
        unzipDirectory.mkdir()

        return unzipDirectory
    }

    /* TODO: Add deleteOnExit as possibility for user */

    override fun nextFile() = imageFilesIterator.nextFile()

    override fun getFile(index: Int) = imageFilesIterator.getFile(index)

    override fun getFiles() = imageFilesIterator.getFiles()

    override fun remove(file: File) = imageFilesIterator.remove(file)

    override fun add(index: Int, file: File) = imageFilesIterator.add(index, file)

    override fun add(file: File) = imageFilesIterator.add(file)

    override fun addAll(files: List<File>) = imageFilesIterator.addAll(files)

    override fun numberOfFiles() = imageFilesIterator.numberOfFiles()

    override fun getParentDirectory() = imageFilesIterator.getParentDirectory()

    override fun resetIndex() = imageFilesIterator.resetIndex()

    companion object ZipFileIteratorFactory {
        fun createZipFileIterator(file: File, deleteOnExit: Boolean): ZipFileIterator {
            return ZipFileIterator(file, deleteOnExit)
        }
    }
}