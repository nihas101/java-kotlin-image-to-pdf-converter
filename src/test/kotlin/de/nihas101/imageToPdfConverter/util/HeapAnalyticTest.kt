package de.nihas101.imageToPdfConverter.util

import org.junit.Assert.assertEquals
import org.junit.Test

class HeapAnalyticTest {

    @Test
    fun isFreeHeapMemoryThresholdCrossed() {
        val heapAnalyticFalse = HeapAnalytic.createHeapAnalytic(.1)
        val heapAnalyticTrue = HeapAnalytic.createHeapAnalytic(1.0)

        assertEquals(false, heapAnalyticFalse.isFreeHeapMemoryThresholdCrossed())
        assertEquals(true, heapAnalyticTrue.isFreeHeapMemoryThresholdCrossed())
    }

    @Test
    fun getFreeMemoryPercentage() {
        val heapAnalytic = HeapAnalytic.createHeapAnalytic()
        assert(heapAnalytic.getFreeMemoryPercentage() < 1)
    }
}