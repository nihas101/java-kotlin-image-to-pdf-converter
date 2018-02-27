package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.tasks.TaskManager.TaskManagerFactory.createTaskManager
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class TaskManagerTest {
    @Test
    fun start() {
        val taskManager = createTaskManager()
        val atomicBoolean = AtomicBoolean(false)

        val setTrueTask = SetTrueTask(BooleanSetter(atomicBoolean)) {
            assertEquals(true, atomicBoolean)
        }

        taskManager.start(setTrueTask, false)
    }
}

class SetTrueTask(private val booleanSetter: BooleanSetter, assertEquals: () -> Unit) : CancellableTask({}, booleanSetter, assertEquals) {
    override fun call() {
        booleanSetter.setBoolean(true)
        after()
    }
}

class BooleanSetter(private val atomicBoolean: AtomicBoolean) : Cancellable {
    override fun cancelTask() {
        /* NOTHING TO DO */
    }

    fun setBoolean(boolean: Boolean) {
        atomicBoolean.set(boolean)
    }
}