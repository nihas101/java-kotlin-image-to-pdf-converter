package de.nihas101.imageToPdfConverter.tasks

class TaskManager private constructor(private val tasks: MutableList<CancellableTask>) {
    fun start(task: CancellableTask, isDaemon: Boolean) {
        add(task)
        startThread(task, isDaemon)
    }

    private fun add(task: CancellableTask) {
        tasks.removeAll(tasks.filter { task.isDone })
        tasks.add(task)
    }

    private fun startThread(task: CancellableTask, isDaemon: Boolean) {
        val thread = Thread(task)
        thread.isDaemon = isDaemon
        thread.start()
    }

    companion object TaskManagerFactory {
        fun createTaskManager() = TaskManager(mutableListOf())
    }
}