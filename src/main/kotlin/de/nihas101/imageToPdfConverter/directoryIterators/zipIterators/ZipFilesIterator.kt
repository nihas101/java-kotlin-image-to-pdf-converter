package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageDirectoriesIterator.ImageDirectoriesIteratorFactory.createImageDirectoriesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ImageUnZipper.ZipFileIteratorFactory.createImageUnZipper
import java.io.File

class ZipFilesIterator(private val deleteOnExit: Boolean) : DirectoryIterator() {
    private var imageDirectoriesIterator: ImageDirectoriesIterator = createImageDirectoriesIterator()
    private var imageUnZipper: ImageUnZipper? = null

    override fun setupDirectory(directory: File) {
        super.setupDirectory(directory)
        directory.listFiles().forEach { file ->
            if (cancelled) throw InterruptedException()
            val unzipInto = makeUnzipDirectory(file, deleteOnExit)
            if (ImageUnZipper.canUnzip(file)) {
                imageUnZipper = createImageUnZipper(file)
                imageUnZipper!!.unzip(unzipInto, true)
            }
        }

        imageDirectoriesIterator = createImageDirectoriesIterator()
        imageDirectoriesIterator.setupDirectory(directory)
    }

    private fun makeUnzipDirectory(file: File, deleteOnExit: Boolean): File {
        val unzipDirectory = File("${file.parent}/${file.nameWithoutExtension}")

        if (deleteOnExit) unzipDirectory.deleteOnExit()
        unzipDirectory.mkdir()

        return unzipDirectory
    }

    override fun cancelTask() {
        super.cancelTask()
        if (imageUnZipper != null) imageUnZipper!!.cancelTask()
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
        fun createZipFilesIterator(deleteOnExit: Boolean): ZipFilesIterator {
            return ZipFilesIterator(deleteOnExit)
        }
    }
}