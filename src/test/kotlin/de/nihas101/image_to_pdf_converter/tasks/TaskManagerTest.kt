package de.nihas101.image_to_pdf_converter.tasks

import de.nihas101.image_to_pdf_converter.tasks.TaskManager.TaskManagerFactory.createTaskManager
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

class SetTrueTask(private val booleanSetter: BooleanSetter, assertEquals: (Boolean) -> Unit) : CancellableTask(booleanSetter, CallClosure({}, assertEquals)) {
    override fun call() {
        booleanSetter.setBoolean(true)
        callClosure.after(true)
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