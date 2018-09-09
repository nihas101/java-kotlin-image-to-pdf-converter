package de.nihas101.imageToPdfConverter.pdf

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreFilesException
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import de.nihas101.imageToPdfConverter.util.TrivialProgressUpdater
import junit.framework.TestCase.fail
import org.junit.Test
import java.io.File

class ImageFilesIteratorTest {
    @Test
    fun imagesLoaderDirTest() {
        val file = File("src/test/resources")
        val imageFilesIterator: ImageFilesIterator = createImageFilesIterator(IteratorOptions())
        imageFilesIterator.addDirectory(file, TrivialProgressUpdater())

        for (i in 1..imageFilesIterator.numberOfFiles()) imageFilesIterator.nextFile()

        try {
            imageFilesIterator.nextFile()
        } catch (exception: NoMoreFilesException) {
            return
        }

        fail()
    }

    @Test
    fun imagesLoaderSingleImageTest() {
        val file = File("src/test/resources/images/3.png")
        val imageFilesIterator: ImageFilesIterator = createImageFilesIterator(IteratorOptions())
        imageFilesIterator.addDirectory(file, TrivialProgressUpdater())

        imageFilesIterator.nextFile()

        try {
            imageFilesIterator.nextFile()
        } catch (exception: NoMoreFilesException) {
            return
        }

        fail()
    }
}