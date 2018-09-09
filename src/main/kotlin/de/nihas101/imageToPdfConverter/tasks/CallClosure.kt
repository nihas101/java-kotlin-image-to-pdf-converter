package de.nihas101.imageToPdfConverter.tasks

data class CallClosure(val before: () -> Unit = {}, val after: () -> Unit = {})