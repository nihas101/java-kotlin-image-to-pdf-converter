package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.gui.controller.MainWindowController
import javafx.concurrent.Task

open class SetupIteratorTask(private val mainWindowController: MainWindowController) : Task<Unit>() {
    override fun call() {
        mainWindowController.setupIterator()
    }

    companion object SetupIteratorThreadFactory {
        fun createSetupIteratorThread(mainWindowController: MainWindowController): Thread {
            return Thread(SetupIteratorTask(mainWindowController))
        }
    }
}