package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator.ImageDirectoriesIteratorFactory.createImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ImageUnzipper.ZipFileIteratorFactory.createImageUnzipper
import java.io.File

class ZipFilesIterator(directory: File, deleteOnExit: Boolean) : DirectoryIterator() {
    private var imageDirectoriesIterator: ImageDirectoriesIterator

    init {
        directory.listFiles().forEach { file ->
            val unzipInto = makeUnzipDirectory(file, deleteOnExit)
            if (ImageUnzipper.canUnzip(file)) createImageUnzipper(file).unzip(unzipInto, true)
        }
        imageDirectoriesIterator = createImageDirectoriesIterator(directory)
    }

    private fun makeUnzipDirectory(file: File, deleteOnExit: Boolean): File {
        val unzipDirectory = File("${file.parent}/${file.nameWithoutExtension}")

        if (deleteOnExit) unzipDirectory.deleteOnExit()
        unzipDirectory.mkdir()

        return unzipDirectory
    }

    override fun nextFile() = imageDirectoriesIterator.nextFile()

    override fun getFile(index: Int) = imageDirectoriesIterator.getFile(index)

    override fun getFiles() = imageDirectoriesIterator.getFiles()

    override fun remove(file: File) = imageDirectoriesIterator.remove(file)

    override fun add(index: Int, file: File) = imageDirectoriesIterator.add(index, file)

    override fun add(file: File) = imageDirectoriesIterator.add(file)

    override fun addAll(files: List<File>) = imageDirectoriesIterator.addAll(files)

    override fun numberOfFiles() = imageDirectoriesIterator.numberOfFiles()

    override fun getParentDirectory() = imageDirectoriesIterator.getParentDirectory()

    override fun resetIndex() = imageDirectoriesIterator.resetIndex()

    companion object ZipFilesIteratorFactory {
        fun createZipFilesIterator(directory: File, deleteOnExit: Boolean): ZipFilesIterator {
            return ZipFilesIterator(directory, deleteOnExit)
        }
    }
}