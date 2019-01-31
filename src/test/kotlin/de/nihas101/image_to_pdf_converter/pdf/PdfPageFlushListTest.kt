package de.nihas101.image_to_pdf_converter.pdf

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfPage
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import de.nihas101.image_to_pdf_converter.directory_iterators.ImagePdfTest
import de.nihas101.image_to_pdf_converter.pdf.PdfPageFlushList.PdfPageFlushListFactory.createPdfPageFlushList
import de.nihas101.image_to_pdf_converter.util.HeapAnalytic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import org.junit.Test
import java.io.File

class PdfPageFlushListTest {
    private val testOutputStream = ImagePdfTest.TestOutputStream(StringBuilder())
    private val flushList: MutableList<PdfPage> = mutableListOf()
    private val pdfWriter = PdfWriter(testOutputStream)
    private val pdf = PdfDocument(pdfWriter)
    private val document = Document(pdf, PageSize.A4, false)

    @Test
    fun add() {
        val heapAnalytic: HeapAnalytic = HeapAnalytic.createHeapAnalytic()
        val pdfPageFlushList = createPdfPageFlushList(flushList, document, pdfWriter, heapAnalytic)

        addPage(pdfPageFlushList)

        assertEquals(1, flushList.size)
    }

    @Test
    fun flush() {
        var heapAnalytic: HeapAnalytic = HeapAnalytic.createHeapAnalytic(0.1)
        heapAnalytic = HeapAnalytic.createHeapAnalytic(heapAnalytic.getFreeMemoryPercentage() - 0.1)
        val pdfPageFlushList = createPdfPageFlushList(flushList, document, pdfWriter, heapAnalytic)

        var lastPercentage = heapAnalytic.getFreeMemoryPercentage()
        var maxIterations = 1000

        while (maxIterations > 0) {
            maxIterations--
            addPage(pdfPageFlushList)

            if (lastPercentage < heapAnalytic.getFreeMemoryPercentage()) break
            lastPercentage = heapAnalytic.getFreeMemoryPercentage()
        }

        if (maxIterations < 1) fail()

        assertEquals(0, flushList.size)
        assertEquals(true, testOutputStream.output.isEmpty())
    }

    private fun addPage(pdfPageFlushList: PdfPageFlushList) {
        val image = Image(ImageDataFactory.create(File("src/test/resources/images/1.jpg").toURI().toURL()))
        pdf.addNewPage(PageSize(Rectangle(0F, 0F, image.imageWidth, image.imageHeight)))
        document.add(image)
        pdfPageFlushList.add(pdf.lastPage)
    }
}