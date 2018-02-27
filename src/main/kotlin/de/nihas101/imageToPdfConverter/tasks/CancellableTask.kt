package de.nihas101.imageToPdfConverter.tasks

import javafx.concurrent.Task

abstract class CancellableTask(
        protected val before: () -> Unit,
        protected val cancellable: Cancellable,
        protected val after: () -> Unit
) : Task<Unit>(), Cancellable, Executable {
    override fun executeTask() {
        call()
    }

    override fun cancelTask() {
        cancellable.cancelTask()
        this.cancel()
    }
}