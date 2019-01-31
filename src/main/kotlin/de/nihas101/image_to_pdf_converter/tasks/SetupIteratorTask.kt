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

package de.nihas101.image_to_pdf_converter.tasks

import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator
import de.nihas101.image_to_pdf_converter.util.JaKoLogger
import de.nihas101.image_to_pdf_converter.util.ProgressUpdater
import java.io.File

open class SetupIteratorTask protected constructor(
        protected val directoryIterator: DirectoryIterator,
        protected val directory: File,
        protected val progressUpdater: ProgressUpdater,
        callClosure: CallClosure
) : CancellableTask(directoryIterator, callClosure) {

    override fun call() {
        callClosure.before()
        try {
            directoryIterator.addDirectory(directory, progressUpdater)
        } catch (exception: InterruptedException) {
            /* The task was cancelled */
            logger.warn("{}", exception)
        }
        callClosure.after()
    }

    companion object SetupIteratorTaskFactory {
        private val logger = JaKoLogger.createLogger(SetupIteratorTask::class.java)

        fun createSetupIteratorTask(
                directoryIterator: DirectoryIterator,
                file: File,
                progressUpdater: ProgressUpdater,
                callClosure: CallClosure
        ): SetupIteratorTask {
            return SetupIteratorTask(
                    directoryIterator,
                    file,
                    progressUpdater,
                    callClosure
            )
        }
    }
}