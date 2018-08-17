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