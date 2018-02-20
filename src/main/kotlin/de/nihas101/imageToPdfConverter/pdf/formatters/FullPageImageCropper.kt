package de.nihas101.imageToPdfConverter.pdf.formatters

import com.itextpdf.kernel.pdf.PdfArray
import com.itextpdf.kernel.pdf.PdfName
import com.itextpdf.kernel.pdf.PdfPage
import com.itextpdf.layout.element.Image

class FullPageImageCropper : ImagePdfPageFormatter(){
    private val lowerLeftX: Float = 0F

    companion object FullPageCropperFactory{
        fun createFullPageCropper(): FullPageImageCropper = FullPageImageCropper()
    }

    override fun format(pdfPage: PdfPage, image: Image){
        val pdfRatio = (pdfPage.mediaBox.width / pdfPage.mediaBox.height)
        val imageRatio = (image.imageWidth / image.imageHeight)
        val scaleBetweenPdfAndImage = pdfRatio / imageRatio
        val cropBoxArray = createCropBoxArray(
                lowerLeftX,
                calculateBottomCrop(pdfPage.mediaBox.height, scaleBetweenPdfAndImage),
                pdfPage.mediaBox.width,
                pdfPage.mediaBox.height
        )

        pdfPage.pdfObject.put(PdfName.CropBox, cropBoxArray)
    }

    private fun createCropBoxArray(lowerLeftX: Float, lowerLeftY: Float, upperRightX: Float, upperRightY: Float): PdfArray
        = PdfArray(floatArrayOf(lowerLeftX,lowerLeftY,upperRightX,upperRightY))


    private fun calculateBottomCrop(height: Float, scale: Float): Float = Math.abs(height - (height * scale))

}