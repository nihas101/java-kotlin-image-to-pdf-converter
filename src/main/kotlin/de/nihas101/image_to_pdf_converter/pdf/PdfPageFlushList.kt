package de.nihas101.image_to_pdf_converter.pdf

import com.itextpdf.kernel.pdf.PdfPage
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import de.nihas101.image_to_pdf_converter.util.HeapAnalytic
import de.nihas101.image_to_pdf_converter.util.JaKoLogger

class PdfPageFlushList private constructor(
        private val flushList: MutableList<PdfPage> = mutableListOf(),
        private val document: Document,
        private val pdfWriter: PdfWriter,
        private val heapAnalytic: HeapAnalytic = HeapAnalytic.createHeapAnalytic()
) {
    fun add(pdfPage: PdfPage) {
        flushList.add(pdfPage)
        if (heapAnalytic.isFreeHeapMemoryThresholdCrossed()) flush()
    }

    private fun flush() {
        try {
            document.flush()
            flushPdfPageList()
            pdfWriter.flush()
        } catch (exception: NullPointerException) {
            logger.error("{}", exception)
        }
        System.gc()
    }

    private fun flushPdfPageList() {
        flushList.forEach { page: PdfPage -> page.flush(true) }
        flushList.clear()
    }

    companion object PdfPageFlushListFactory {
        private val logger = JaKoLogger.createLogger(PdfPageFlushList::class.java)

        fun createPdfPageFlushList(flushList: MutableList<PdfPage> = mutableListOf(),
                                   document: Document,
                                   pdfWriter: PdfWriter,
                                   heapAnalytic: HeapAnalytic = HeapAnalytic.createHeapAnalytic()): PdfPageFlushList {
            return PdfPageFlushList(flushList, document, pdfWriter, heapAnalytic)
        }
    }
}