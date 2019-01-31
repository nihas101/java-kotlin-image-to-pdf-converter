package de.nihas101.image_to_pdf_converter.pdf.pdf_options

import de.nihas101.image_to_pdf_converter.pdf.pdf_options.ImageToPdfOptions.OptionsFactory.createOptions
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.File

class PdfBuildInformationTest {

    @Test
    fun setupDirectoryIterator() {
        val pdfBuilderInformation = PdfBuildInformation()
        pdfBuilderInformation.sourceFile = File("src/test/resources/images")
        pdfBuilderInformation.setupDirectoryIterator()

        val fileList = pdfBuilderInformation.getDirectoryIterator().getFileList()
        fileList.sort()

        assertEquals("[1.jpg, 2.png, 3.png, の.png]", fileList.map { file -> file.name }.toString())
    }

    @Test
    fun setMultipleDirectories() {
        val pdfBuilderInformation = PdfBuildInformation()
        assertEquals(false, pdfBuilderInformation.getMultipleDirectories())
        pdfBuilderInformation.setMultipleDirectories(true)
        assertEquals(true, pdfBuilderInformation.getMultipleDirectories())
    }

    @Test
    fun setTargetFile() {
        val pdfBuildInformation = PdfBuildInformation()
        pdfBuildInformation.setTargetFile(File("src/test/resources/images"))

        assertEquals("images", pdfBuildInformation.getTargetFile().name)
    }

    @Test
    fun getPdfWriterOptions() {
        val pdfBuildInformation = PdfBuildInformation()
        val imageToPdfOptions = createOptions()
        assertEquals(imageToPdfOptions, pdfBuildInformation.getImageToPdfOptions())
    }

    @Test
    fun setCustomTargetFile() {
        val pdfBuildInformation = PdfBuildInformation()
        assertEquals(false, pdfBuildInformation.customTargetFile)
        pdfBuildInformation.customTargetFile = true
        assertEquals(true, pdfBuildInformation.customTargetFile)
    }
}