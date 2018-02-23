package de.nihas101.imageToPdfConverter.pdf

import com.itextpdf.kernel.pdf.PdfVersion
import java.io.File

data class PdfOptions(val compressionLevel: Int, val pdfVersion: PdfVersion, val saveLocation: File?)