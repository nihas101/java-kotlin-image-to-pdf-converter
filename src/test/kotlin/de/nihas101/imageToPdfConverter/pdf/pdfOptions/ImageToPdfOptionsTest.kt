package de.nihas101.imageToPdfConverter.pdf.pdfOptions

import com.itextpdf.kernel.pdf.CompressionConstants.BEST_COMPRESSION
import com.itextpdf.kernel.pdf.CompressionConstants.DEFAULT_COMPRESSION
import com.itextpdf.kernel.pdf.PdfVersion.PDF_1_7
import com.itextpdf.kernel.pdf.PdfVersion.PDF_2_0
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class ImageToPdfOptionsTest {

    @Test
    fun setMultipleDirectories() {
        val imageToPdfOptions = ImageToPdfOptions()
        assertEquals(false, imageToPdfOptions.getIteratorOptions().multipleDirectories)
        imageToPdfOptions.setMultipleDirectories(true)
        assertEquals(true, imageToPdfOptions.getIteratorOptions().multipleDirectories)
    }

    @Test
    fun setZipFiles() {
        val imageToPdfOptions = ImageToPdfOptions()
        assertEquals(false, imageToPdfOptions.getIteratorOptions().zipFiles)
        imageToPdfOptions.setZipFiles(true)
        assertEquals(true, imageToPdfOptions.getIteratorOptions().zipFiles)
    }

    @Test
    fun setDeleteOnExit() {
        val imageToPdfOptions = ImageToPdfOptions()
        assertEquals(true, imageToPdfOptions.getIteratorOptions().deleteOnExit)
        imageToPdfOptions.setDeleteOnExit(false)
        assertEquals(false, imageToPdfOptions.getIteratorOptions().deleteOnExit)
    }

    @Test
    fun setSaveLocation() {
        val imageToPdfOptions = ImageToPdfOptions()
        imageToPdfOptions.setSaveLocation(File("src/test/resources/images"))
        assertEquals("images", imageToPdfOptions.getPdfOptions().saveLocation!!.name)
    }

    @Test
    fun setPdfVersion() {
        val imageToPdfOptions = ImageToPdfOptions()
        assertEquals(PDF_1_7, imageToPdfOptions.getPdfOptions().pdfVersion)
        imageToPdfOptions.setPdfVersion(PDF_2_0)
        assertEquals(PDF_2_0, imageToPdfOptions.getPdfOptions().pdfVersion)
    }

    @Test
    fun setCompressionLevel() {
        val imageToPdfOptions = ImageToPdfOptions()
        assertEquals(DEFAULT_COMPRESSION, imageToPdfOptions.getPdfOptions().compressionLevel)
        imageToPdfOptions.setCompressionLevel(BEST_COMPRESSION)
        assertEquals(BEST_COMPRESSION, imageToPdfOptions.getPdfOptions().compressionLevel)
    }
}