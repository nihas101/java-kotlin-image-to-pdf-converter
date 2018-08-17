package de.nihas101.imageToPdfConverter.util

import de.nihas101.imageToPdfConverter.util.Constants.FREE_HEAP_MEMORY_THRESHOLD
import java.lang.Runtime.getRuntime

class HeapAnalytic private constructor(private val freeHeapMemoryThreshold: Double = FREE_HEAP_MEMORY_THRESHOLD) {

    fun isFreeHeapMemoryThresholdCrossed(): Boolean {
        return getFreeMemoryPercentage() < freeHeapMemoryThreshold
    }

    fun getFreeMemoryPercentage(): Double {
        val freeMemoryPercentage = getRuntime().freeMemory().toDouble() / getRuntime().totalMemory().toDouble()
        logger.info("Free memory: {}", freeMemoryPercentage)
        return freeMemoryPercentage
    }

    companion object {
        private val logger = JaKoLogger.createLogger(HeapAnalytic::class.java)

        fun createHeapAnalytic(freeHeapMemoryThreshold: Double = FREE_HEAP_MEMORY_THRESHOLD): HeapAnalytic {
            return HeapAnalytic(freeHeapMemoryThreshold)
        }
    }
}