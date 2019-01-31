package de.nihas101.image_to_pdf_converter.util

import ch.qos.logback.classic.Level
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ArgumentParserTest {

    private val levels = Array<Level>(4) { index ->
        when (index) {
            0 -> Level.TRACE
            1 -> Level.DEBUG
            2 -> Level.WARN
            3 -> Level.INFO
            else -> Level.ERROR
        }
    }

    @Test
    fun noGUI() {
        testArgumentCases(Constants.NO_GUI, false, Level.ERROR)
    }

    @Test
    fun trace() {
        testArgumentCases(Constants.TRACE, true, Level.TRACE)
    }

    @Test
    fun debug() {
        testArgumentCases(Constants.DEBUG, true, Level.DEBUG)
    }

    @Test
    fun warn() {
        testArgumentCases(Constants.WARN, true, Level.WARN)
    }

    @Test
    fun info() {
        testArgumentCases(Constants.INFO, true, Level.INFO)
    }

    @Test
    fun error() {
        testArgumentCases(Constants.ERROR, true, Level.ERROR)
    }

    private fun testArgumentCases(levelArgument: String, expectedIsGuiEnabled: Boolean, expectedLevel: Level) {
        testArguments(Array(1) { levelArgument.toLowerCase() }, JaKoOptions(), expectedIsGuiEnabled, expectedLevel)
        testArguments(Array(1) { levelArgument.toUpperCase() }, JaKoOptions(), expectedIsGuiEnabled, expectedLevel)
    }

    @Test
    fun traceAssertPresetLevels() {
        assertPresetLevels(Constants.TRACE, Level.TRACE)
    }

    @Test
    fun debugAssertPresetLevels() {
        assertPresetLevels(Constants.DEBUG, Level.DEBUG)
    }

    @Test
    fun warnAssertPresetLevels() {
        assertPresetLevels(Constants.WARN, Level.WARN)
    }

    @Test
    fun infoAssertPresetLevels() {
        assertPresetLevels(Constants.INFO, Level.INFO)
    }

    @Test
    fun errorAssertPresetLevels() {
        assertPresetLevels(Constants.ERROR, Level.ERROR)
    }

    private fun assertPresetLevels(arg: String, level: Level) {
        levels.forEach { presetLevel ->
            if (!level.isGreaterOrEqual(presetLevel)) assertPresetLevel(Array(1) { arg }, presetLevel, level)
            else assertPresetLevel(Array(1) { arg }, presetLevel, presetLevel)
        }
    }

    private fun assertPresetLevel(args: Array<String>, setLevel: Level, expectedLevel: Level) {
        var options = JaKoOptions(loggingLevel = setLevel)

        options = parseArguments(args, options)

        assertOptions(true, expectedLevel, options)
    }

    @Test
    fun multipleLevels() {
        val args = Array(3) { index ->
            when (index) {
                0 -> Constants.INFO
                1 -> Constants.WARN
                else -> Constants.TRACE
            }
        }

        testArguments(args, JaKoOptions(), true, Level.TRACE)
    }

    @Test
    fun noGuiAndLevel() {
        val args = Array(3) { index ->
            when (index) {
                0 -> Constants.ERROR
                else -> Constants.NO_GUI
            }
        }

        testArguments(args, JaKoOptions(), false, Level.ERROR)
    }

    private fun testArguments(args: Array<String>, options: JaKoOptions, expectedIsGuiEnabled: Boolean, expectedLevel: Level) {
        val parsedOptions = parseArguments(args, options)

        assertOptions(expectedIsGuiEnabled, expectedLevel, parsedOptions)
    }

    private fun assertOptions(expectedIsGuiEnabled: Boolean, expectedLevel: Level, actualOptions: JaKoOptions) {
        assertEquals(expectedIsGuiEnabled, actualOptions.isGUIEnabled)
        assertEquals(expectedLevel, actualOptions.loggingLevel)
    }
}