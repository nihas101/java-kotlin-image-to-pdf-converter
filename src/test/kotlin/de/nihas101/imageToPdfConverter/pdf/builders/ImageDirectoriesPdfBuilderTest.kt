package de.nihas101.imageToPdfConverter.pdf.builders

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator
import de.nihas101.imageToPdfConverter.pdf.builders.PdfBuilder.PdfBuilderFactory.createPdfBBuilder
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions.OptionsFactory.createOptions
import de.nihas101.imageToPdfConverter.util.ProgressUpdater
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

class ImageDirectoriesPdfBuilderTest {
    @Test
    fun build() {
        val atomicBoolean = AtomicBoolean(false)
        val imageToOptions = createOptions()
        imageToOptions.setMultipleDirectories(true)
        val saveLocation = File("src/test/resources/")

        imageToOptions.setSaveLocation(saveLocation)
        val directoryIterator = createDirectoryIterator(saveLocation, imageToOptions.getIteratorOptions())
        val pdfBuilder = createPdfBBuilder(imageToOptions.getIteratorOptions())

        pdfBuilder.build(directoryIterator, imageToOptions, TestProgressUpdater(atomicBoolean))

        val file = File("src/test/resources/images.pdf")
        file.deleteOnExit()

        assertEquals(true, file.exists())
        assertEquals(true, atomicBoolean.get())
        assertEquals(true, saveLocation.exists())
    }

    @Test
    fun buildTrailingSpace() {
        val atomicBoolean = AtomicBoolean(false)
        val imageToOptions = createOptions()
        imageToOptions.setZipFiles(true)

        val zipFile = File("src/test/resources/zip/imagesTrailing .zip")
        val saveFile = File("src/test/resources/zip/imagesTrailing.pdf")

        imageToOptions.setSaveLocation(saveFile)
        val directoryIterator = createDirectoryIterator(zipFile, imageToOptions.getIteratorOptions())
        val pdfBuilder = createPdfBBuilder(imageToOptions.getIteratorOptions())

        assertEquals(4, directoryIterator.numberOfFiles())

        pdfBuilder.build(directoryIterator, imageToOptions, TestProgressUpdater(atomicBoolean))


        saveFile.deleteOnExit()

        assertEquals(true, saveFile.exists())
        assertEquals(true, atomicBoolean.get())
        assertEquals(true, zipFile.exists())
    }
}

class TestProgressUpdater(private val buildSuccess: AtomicBoolean) : ProgressUpdater {
    override fun updateProgress(progress: Double, file: File) {
        if (progress == 1.0) buildSuccess.set(true)
    }

}