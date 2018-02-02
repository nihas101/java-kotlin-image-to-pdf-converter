package de.nihas101.imagesToPdfConverter.fileReader

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import de.nihas101.imagesToPdfConverter.pdf.ImagePdf
import org.junit.Test

class ImagePdfTest{
    @Test
    fun imagePdfTest1(){
        val imagePdf: ImagePdf = ImagePdf.createPdf("src/test/resources/test1.pdf")

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/3.png")))
        imagePdf.close()
    }

    @Test
    fun imagePdfTest2(){
        val imagePdf: ImagePdf = ImagePdf.createPdf("src/test/resources/test2.pdf")

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/2.png")))
        imagePdf.close()
    }

    @Test
    fun imagePdfTest3(){
        val imagePdf: ImagePdf = ImagePdf.createPdf("src/test/resources/test3.pdf")

        imagePdf.add(Image(ImageDataFactory.create("src/test/resources/1.jpg")))
        imagePdf.close()
    }
}