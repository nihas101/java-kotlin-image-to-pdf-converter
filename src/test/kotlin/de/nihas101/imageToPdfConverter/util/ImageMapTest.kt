package de.nihas101.imageToPdfConverter.util

import javafx.scene.image.Image
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest
import java.io.File

internal class ImageMapTest : ApplicationTest() {
    @Test
    fun loadImages() {
        val imageMap = ImageMap.createImageMap()
        val files = File("src/test/resources/images").listFiles().toMutableList()

        imageMap.loadImages(files)

        assertEquals(true, imageMap.contains(files[0]))
        assertEquals(true, imageMap.contains(files[1]))
        assertEquals(true, imageMap.contains(files[2]))
        assertEquals(true, imageMap.contains(files[3]))
    }

    @Test
    fun loadImages1() {
        val imageMap = ImageMap.createImageMap()
        val files = File("src/test/resources/images").listFiles().toMutableList()

        imageMap.loadImages(files, TrivialProgressUpdater())

        assertEquals(true, imageMap.contains(files[0]))
        assertEquals(true, imageMap.contains(files[1]))
        assertEquals(true, imageMap.contains(files[2]))
        assertEquals(true, imageMap.contains(files[3]))
    }

    @Test
    fun clearImages() {
        val imageMap = ImageMap.createImageMap()
        val files = File("src/test/resources/images").listFiles().toMutableList()

        imageMap.loadImages(files)
        imageMap.clearImages()

        assertEquals(false, imageMap.contains(files[0]))
        assertEquals(false, imageMap.contains(files[1]))
        assertEquals(false, imageMap.contains(files[2]))
        assertEquals(false, imageMap.contains(files[3]))
    }

    @Test
    fun get() {
        val imageMap = ImageMap.createImageMap()
        val files = File("src/test/resources/images").listFiles().toMutableList()
        val image = Image(files[0].toURI().toURL().toString(), 100.0, 100.0, true, false)

        imageMap.loadImages(files)

        assertSameImage(image, imageMap.get(files[0])!!)
    }

    private fun assertSameImage(expected: Image, actual: Image) {
        val expectedPixelReader = expected.pixelReader
        val actualPixelReader = actual.pixelReader

        println("${expected.height.toInt()} , ${expected.width.toInt()}")

        for (x in 0 until expected.width.toInt()) {
            for (y in 0 until expected.height.toInt()) {
                assertEquals(expectedPixelReader.getColor(x, y), actualPixelReader.getColor(x, y))
            }
        }
    }

    @Test
    fun remove() {
    }

    @Test
    fun contains() {
    }
}