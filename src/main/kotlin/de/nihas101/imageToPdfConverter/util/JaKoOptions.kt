package de.nihas101.imageToPdfConverter.util

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Level.OFF


data class JaKoOptions(val isGUIEnabled: Boolean = true, val loggingLevel: Level = OFF)
