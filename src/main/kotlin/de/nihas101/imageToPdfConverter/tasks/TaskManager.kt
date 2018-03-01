/*
 JaKoImage2PDF is a program for converting images to PDFs with the use of iText 7
 Copyright (C) 2018  Nikita Hasert

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.nihas101.imageToPdfConverter.tasks

class TaskManager private constructor(private val tasks: MutableList<CancellableTask>) {
    fun start(task: CancellableTask, isDaemon: Boolean) {
        add(task)
        startThread(task, isDaemon)
    }

    fun cancelAllTasks() {
        tasks.forEach { task -> task.cancelTask() }
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