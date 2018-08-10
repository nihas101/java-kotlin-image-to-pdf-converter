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

import ch.qos.logback.classic.Level
import de.nihas101.imageToPdfConverter.util.Constants.*

class ArgumentParser {
    companion object ArgumentParser {
        fun parseArguments(arguments: Array<String>, options: JaKoOptions): JaKoOptions {
            var parsedOptions = options
            for (arg in arguments) parsedOptions = parseArgument(arg, parsedOptions)
            return parsedOptions
        }

        private fun parseArgument(argument: String, options: JaKoOptions): JaKoOptions {
            return when (argument.trim().toLowerCase()) {
                TRACE -> setLoggingLevel(options, Level.TRACE)
                DEBUG -> setLoggingLevel(options, Level.DEBUG)
                INFO -> setLoggingLevel(options, Level.INFO)
                WARN -> setLoggingLevel(options, Level.WARN)
                ERROR -> setLoggingLevel(options, Level.ERROR)
                NO_GUI -> options.copy(isGUIEnabled = false)
                else -> options
            }
        }

        private fun setLoggingLevel(options: JaKoOptions, level: Level): JaKoOptions {
            return when {
                options.loggingLevel == Level.OFF -> options.copy(loggingLevel = level)
                level.isGreaterOrEqual(options.loggingLevel) -> options.copy(loggingLevel = level)
                else -> options
            }
        }
    }
}