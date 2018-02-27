package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.ExtensionNotSupportedException
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.ImageUnZipper.ZipFileIteratorFactory.createImageUnZipper
import java.io.File


class ZipFileIterator(private val deleteOnExit: Boolean) : DirectoryIterator() {
    private var imageFilesIterator: ImageFilesIterator = ImageFilesIterator.createImageFilesIterator()
    private var imageUnZipper: ImageUnZipper? = null

    override fun setupDirectory(directory: File) {
        super.setupDirectory(directory)
        val unzipInto = makeUnzipDirectory(deleteOnExit)
        try {
            imageUnZipper = createImageUnZipper(directory)
            imageUnZipper!!.unzip(unzipInto, deleteOnExit)
        } catch (exception: ExtensionNotSupportedException) {
            /* Proceed with empty unzip directory */
        }
        imageFilesIterator = ImageFilesIterator.createImageFilesIterator()
        imageFilesIterator.setupDirectory(unzipInto)
    }

    private fun makeUnzipDirectory(deleteOnExit: Boolean): File {
        val unzipDirectory = File("${directory!!.parent}/${directory!!.nameWithoutExtension}")

        if (deleteOnExit) unzipDirectory.deleteOnExit()
        unzipDirectory.mkdir()

        return unzipDirectory
    }

    override fun cancelTask() {
        super.cancelTask()
        if (imageUnZipper != null) imageUnZipper!!.cancelTask()
    }

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
        fun createZipFileIterator(deleteOnExit: Boolean): ZipFileIterator {
            return ZipFileIterator(deleteOnExit)
        }
    }
}