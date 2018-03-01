package de.nihas101.imageToPdfConverter.pdf

import de.nihas101.imageToPdfConverter.directoryIterators.exceptions.NoMoreImagesException
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator
import junit.framework.TestCase.fail
import org.junit.Test
import java.io.File

class ImageFilesIteratorTest {
    @Test
    fun imagesLoaderDirTest() {
        val file = File("src/test/RESOURCES")
        val imageFilesIterator: ImageFilesIterator = createImageFilesIterator()
        imageFilesIterator.setupDirectory(file)

        for (i in 1..imageFilesIterator.numberOfFiles()) imageFilesIterator.nextFile()

        try {
            imageFilesIterator.nextFile()
        } catch (exception: NoMoreImagesException) {
            return
        }

        fail()
    }

    @Test
    fun imagesLoaderSingleImageTest() {
        val file = File("src/test/RESOURCES/images/3.png")
        val imageFilesIterator: ImageFilesIterator = createImageFilesIterator()
        imageFilesIterator.setupDirectory(file)

        imageFilesIterator.nextFile()

        try {
            imageFilesIterator.nextFile()
        } catch (exception: NoMoreImagesException) {
            return
        }

        fail()
    }
}