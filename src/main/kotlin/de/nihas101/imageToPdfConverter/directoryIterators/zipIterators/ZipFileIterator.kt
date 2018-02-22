package de.nihas101.imageToPdfConverter.directoryIterators.zipIterators

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.Unzipper.ZipFileIteratorFactory.createFileOutputStream
import de.nihas101.imageToPdfConverter.directoryIterators.zipIterators.Unzipper.ZipFileIteratorFactory.createUnzipper
import java.io.File


class ZipFileIterator(private val file: File) : DirectoryIterator {
    private val imageFilesIterator: ImageFilesIterator

    init {
        createUnzipper(file).unzip { fileName -> createFileOutputStream("${file.parent}/$fileName") }
        imageFilesIterator = ImageFilesIterator.createImageFilesIterator(file)
    }

    /* TODO: Create ImageFilesIterator and pass the newly unzipped files to it */
    /* TODO: Make it so ImageDirectoriesIterator and ImageFilesIterator can delete a folder after creating a PDF */
    /* TODO: Then add that as an option! for the user */

    override fun nextFile() = imageFilesIterator.nextFile()

    override fun getFile(index: Int) = imageFilesIterator.getFile(index)

    override fun getFiles() = imageFilesIterator.getFiles()

    override fun remove(file: File) = imageFilesIterator.remove(file)

    override fun add(index: Int, file: File) = imageFilesIterator.add(index, file)

    override fun add(file: File) = imageFilesIterator.add(file)

    override fun addAll(files: List<File>) = imageFilesIterator.add(file)

    override fun numberOfFiles() = imageFilesIterator.numberOfFiles()

    override fun getParentDirectory() = imageFilesIterator.getParentDirectory()

    override fun resetIndex() = imageFilesIterator.resetIndex()

    companion object ZipFileIteratorFactory {
        fun createZipFileIterator(file: File): ZipFileIterator {
            return ZipFileIterator(file)
        }
    }
}