package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.gui.controller.MainWindowController

class SetupIteratorFromDragAndDropTask(private val mainWindowController: MainWindowController, private val after: () -> Unit) : SetupIteratorTask(mainWindowController) {
    override fun call() {
        super.call()
        after()
    }

    companion object SetupIteratorFromDragAndDropTaskFactory {
        fun createSetupIteratorThread(mainWindowController: MainWindowController, after: () -> Unit): Thread {
            val thread = Thread(SetupIteratorFromDragAndDropTask(mainWindowController, after))
            thread.isDaemon = true
            return thread
        }
    }
}