package de.nihas101.imageToPdfConverter.directoryIterators

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import de.nihas101.imageToPdfConverter.pdf.ImagePdf
import junit.framework.TestCase.fail
import org.junit.Test
import java.io.OutputStream

class ImagePdfTest{
    @Test
    fun imagePdfTest1(){
        val testOutputStream = TestOutputStream("")
        val imagePdf: ImagePdf = ImagePdf.createPdf("src/test/resources/test1.pdf", outputStream = testOutputStream)

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/3.png")))
        imagePdf.close()

        if(testOutputStream.output.isEmpty()) fail()
    }

    @Test
    fun imagePdfTest2(){
        val testOutputStream = TestOutputStream("")
        val imagePdf: ImagePdf = ImagePdf.createPdf("src/test/resources/test2.pdf", outputStream = testOutputStream)

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/2.png")))
        imagePdf.close()

        if(testOutputStream.output.isEmpty()) fail()
    }

    @Test
    fun imagePdfTest3(){
        val testOutputStream = TestOutputStream("")
        val imagePdf: ImagePdf = ImagePdf.createPdf("src/test/resources/test3.pdf", outputStream = testOutputStream)

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/1.jpg")))
        imagePdf.close()

        if(testOutputStream.output.isEmpty()) fail()
    }

    @Test
    fun unicodeTest(){
        val testOutputStream = TestOutputStream("")
        val imagePdf: ImagePdf = ImagePdf.createPdf("src/test/resources/の.png", outputStream = testOutputStream)

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/1.jpg")))
        imagePdf.close()

        if(testOutputStream.output.isEmpty()) fail()
    }

    class TestOutputStream(var output: String): OutputStream(){
        override fun write(b: Int) {
            output += b.toChar()
        }
    }
}