package de.nihas101.imageToPdfConverter.pdf

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreImagesException
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator
import junit.framework.TestCase.fail
import org.junit.Test
import java.io.File

class ImageFilesIteratorTest {
    @Test
    fun imagesLoaderDirTest() {
        val file = File("src/test/resources")
        val imageFilesIterator: ImageFilesIterator = ImageFilesIterator.createImageFilesIterator(file)

        for (i in 1..imageFilesIterator.nrOfFiles()) imageFilesIterator.nextFile()

        try {
            imageFilesIterator.nextFile()
        } catch (exception: NoMoreImagesException) {
            return
        }

        fail()
    }

    @Test
    fun imagesLoaderSingleImageTest() {
        val file = File("src/test/resources/3.png")
        val imageFilesIterator: ImageFilesIterator = ImageFilesIterator.createImageFilesIterator(file)

        imageFilesIterator.nextFile()

        try {
            imageFilesIterator.nextFile()
        } catch (exception: NoMoreImagesException) {
            return
        }

        fail()
    }
}