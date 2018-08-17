package de.nihas101.imageToPdfConverter.directoryIterators

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import de.nihas101.imageToPdfConverter.pdf.ImagePdf
import de.nihas101.imageToPdfConverter.pdf.ImagePdf.ImagePdfFactory.createPdf
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions.OptionsFactory.createOptions
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.PdfOptions
import junit.framework.TestCase.fail
import org.junit.Test
import java.io.File
import java.io.OutputStream

class ImagePdfTest {
    @Test
    fun imagePdfTest1() {
        val testOutputStream = TestOutputStream(StringBuilder(), false)
        val options = createOptions(
                IteratorOptions(false),
                PdfOptions(saveLocation = File("src/test/resources/test1.pdf"))
        )
        val imagePdf = createPdf(options, testOutputStream)

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/images/3.png")))
        imagePdf.close()

        if (testOutputStream.output.isEmpty()) fail()
    }

    @Test
    fun imagePdfTest2() {
        val testOutputStream = TestOutputStream(StringBuilder(), false)
        val options = createOptions(
                IteratorOptions(false),
                PdfOptions(saveLocation = File("src/test/resources/test2.pdf"))
        )
        val imagePdf = createPdf(options, testOutputStream)

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/images/2.png")))
        imagePdf.close()

        if (testOutputStream.output.isEmpty()) fail()
    }

    @Test
    fun imagePdfTest3() {
        val testOutputStream = TestOutputStream(StringBuilder(), false)
        val options = createOptions(
                IteratorOptions(false),
                PdfOptions(saveLocation = File("src/test/resources/test3.pdf"))
        )
        val imagePdf = createPdf(options, testOutputStream)

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/images/1.jpg")))
        imagePdf.close()

        if (testOutputStream.output.isEmpty()) fail()
    }

    @Test
    fun unicodeTest() {
        val testOutputStream = TestOutputStream(StringBuilder(), false)
        val options = createOptions(
                IteratorOptions(false),
                PdfOptions(saveLocation = File("src/test/resources/の.png"))
        )
        val imagePdf: ImagePdf = createPdf(options, testOutputStream)

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/images/1.jpg")))
        imagePdf.close()

        if (testOutputStream.output.isEmpty()) fail()
    }

    class TestOutputStream(var output: StringBuilder, private val flushable: Boolean = true) : OutputStream() {
        override fun write(b: Int) {
            output.append(b.toChar())
        }

        override fun flush() {
            if (flushable) output = StringBuilder()
        }
    }
}