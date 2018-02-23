package de.nihas101.imageToPdfConverter.directoryIterators

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import de.nihas101.imageToPdfConverter.pdf.ImagePdf
import de.nihas101.imageToPdfConverter.pdf.ImagePdf.ImagePdfFactory.createPdf
import de.nihas101.imageToPdfConverter.pdf.ImageToPdfOptions.OptionsFactory.createOptions
import junit.framework.TestCase.fail
import org.junit.Test
import java.io.File
import java.io.OutputStream

class ImagePdfTest {
    @Test
    fun imagePdfTest1() {
        val testOutputStream = TestOutputStream(StringBuilder())
        val imagePdf = createPdf(createOptions(false, saveLocation = File("src/test/resources/test1.pdf")), testOutputStream)

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/images/3.png")))
        imagePdf.close()

        if (testOutputStream.output.isEmpty()) fail()
    }

    @Test
    fun imagePdfTest2() {
        val testOutputStream = TestOutputStream(StringBuilder())
        val imagePdf = createPdf(createOptions(false, saveLocation = File("src/test/resources/test2.pdf")), testOutputStream)

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/images/2.png")))
        imagePdf.close()

        if (testOutputStream.output.isEmpty()) fail()
    }

    @Test
    fun imagePdfTest3() {
        val testOutputStream = TestOutputStream(StringBuilder())
        val imagePdf = createPdf(createOptions(false, saveLocation = File("src/test/resources/test3.pdf")), testOutputStream)

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/images/1.jpg")))
        imagePdf.close()

        if (testOutputStream.output.isEmpty()) fail()
    }

    @Test
    fun unicodeTest() {
        val testOutputStream = TestOutputStream(StringBuilder())
        val imagePdf: ImagePdf = createPdf(createOptions(false, saveLocation = File("src/test/resources/の.png")), testOutputStream)

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/images/1.jpg")))
        imagePdf.close()

        if (testOutputStream.output.isEmpty()) fail()
    }

    class TestOutputStream(var output: StringBuilder) : OutputStream() {
        override fun write(b: Int) {
            output.append(b.toChar())
        }
    }
}