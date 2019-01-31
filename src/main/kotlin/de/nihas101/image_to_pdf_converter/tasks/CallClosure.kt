package de.nihas101.image_to_pdf_converter.tasks

data class CallClosure(val before: () -> Unit = {}, val after: () -> Unit = {})