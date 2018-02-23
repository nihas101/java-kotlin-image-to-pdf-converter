package de.nihas101.imageToPdfConverter.pdf

data class ImageToPdfOptions(
        val iteratorOptions: IteratorOptions = IteratorOptions(),
        val pdfOptions: PdfOptions = PdfOptions()
) {

    companion object OptionsFactory {
        fun createOptions(
                iteratorOptions: IteratorOptions = IteratorOptions(),
                pdfOptions: PdfOptions = PdfOptions()
        ): ImageToPdfOptions {
            return ImageToPdfOptions(iteratorOptions, pdfOptions)
        }
    }

}