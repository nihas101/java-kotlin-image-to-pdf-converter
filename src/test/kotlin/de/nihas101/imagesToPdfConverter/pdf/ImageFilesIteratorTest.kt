package de.nihas101.imagesToPdfConverter.pdf

import de.nihas101.imagesToPdfConverter.fileReader.ImageFilesIterator
import de.nihas101.imagesToPdfConverter.fileReader.NoMoreImagesException
import junit.framework.TestCase.fail
import org.junit.Test
import java.io.File

class ImageFilesIteratorTest {
    @Test
    fun imagesLoaderDirTest(){
        val file = File("src/test/resources")
        val imageFilesIterator: ImageFilesIterator = ImageFilesIterator.createImagesDirLoader(file)

        for (i in 1..imageFilesIterator.nrOfFiles()) imageFilesIterator.nextFile()

        try {
            imageFilesIterator.nextFile()
        }catch (exception: NoMoreImagesException){
            return
        }

        fail()
    }

    @Test
    fun imagesLoaderSingleImageTest(){
        val file = File("src/test/resources/3.png")
        val imageFilesIterator: ImageFilesIterator = ImageFilesIterator.createImagesDirLoader(file)

        imageFilesIterator.nextFile()

        try {
            imageFilesIterator.nextFile()
        }catch (exception: NoMoreImagesException){
            return
        }

        fail()
    }
}