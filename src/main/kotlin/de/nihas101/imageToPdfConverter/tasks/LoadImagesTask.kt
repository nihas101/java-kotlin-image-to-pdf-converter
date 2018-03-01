/*
 Image2PDF is a program for converting images to PDFs with the use of iText 7
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

package de.nihas101.imageToPdfConverter.tasks

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
import de.nihas101.imageToPdfConverter.util.ImageMap
import de.nihas101.imageToPdfConverter.util.ProgressUpdater

class LoadImagesTask private constructor(
        private val imageMap: ImageMap,
        private val directoryIterator: DirectoryIterator,
        private val updater: ProgressUpdater,
        before: () -> Unit,
        after: () -> Unit
) : CancellableTask(before, imageMap, after) {

    override fun call() {
        before()
        try {
            imageMap.loadImages(directoryIterator.getFiles(), updater)
        } catch (exception: InterruptedException) {
            /* The task was cancelled */
        }

        after()
    }

    companion object LoadImagesTaskFactory {
        fun createLoadImagesTask(
                imageMap: ImageMap,
                directoryIterator: DirectoryIterator,
                progressUpdater: ProgressUpdater,
                after: () -> Unit
        ): LoadImagesTask {
            return LoadImagesTask(imageMap, directoryIterator, progressUpdater, {}, after)
        }
    }
}