package de.nihas101.imageToPdfConverter.util

import ch.qos.logback.classic.Level
import de.nihas101.imageToPdfConverter.util.Constants.*

class ArgumentParser{
    companion object ArgumentParser {
        fun parse(args: Array<String>, options: JaKoOptions) : JaKoOptions {
            var parsedOptions = options
            for (arg in args) parsedOptions = parse(arg, parsedOptions)
            return parsedOptions
        }

        private fun parse(arg: String, options: JaKoOptions) : JaKoOptions{
            return when(arg.trim().toLowerCase()){
                TRACE -> setLoggingLevel(options, Level.TRACE)
                DEBUG -> setLoggingLevel(options, Level.DEBUG)
                INFO -> setLoggingLevel(options, Level.INFO)
                WARN -> setLoggingLevel(options, Level.WARN)
                ERROR -> setLoggingLevel(options, Level.ERROR)
                NO_GUI -> options.copy(isGUIEnabled = false)
                else -> options
            }
        }

        private fun setLoggingLevel(options: JaKoOptions, level: Level) : JaKoOptions {
            return when {
                options.loggingLevel == Level.OFF -> options.copy(loggingLevel = level)
                level.isGreaterOrEqual(options.loggingLevel) -> options.copy(loggingLevel = level)
                else -> options
            }
        }
    }
}