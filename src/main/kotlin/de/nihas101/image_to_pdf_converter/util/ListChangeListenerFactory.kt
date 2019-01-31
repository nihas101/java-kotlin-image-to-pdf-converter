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

package de.nihas101.image_to_pdf_converter.util

import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.scene.control.ListView
import java.io.File
import java.net.MalformedURLException
import java.util.stream.IntStream


class ListChangeListenerFactory(private val imageListView: ListView<File>, private val imageMap: ImageMap) {
    /**
     * Sets up a [ListChangeListener] that forwards all changes on the [ObservableList] to the underlying [List]
     *
     * @param directoryIterator The [DirectoryIterator] for iterating over directories
     * @return The created [ListChangeListener]
     */
    fun setupListChangeListener(directoryIterator: DirectoryIterator, notification: () -> Unit): ListChangeListener<File> {
        return ListChangeListener { change ->
            while (change.next()) handleChange(directoryIterator, change)
            notification()
        }
    }

    private fun handleChange(directoryIterator: DirectoryIterator, change: ListChangeListener.Change<out File>) {
        if (change.wasRemoved()) removeChange(directoryIterator, change)

        if (change.wasAdded() && change.addedSize == 1)
            addAtChangePosition(directoryIterator, change)
        else if (change.wasAdded()) addChange(directoryIterator, change)
    }

    private fun addChange(directoryIterator: DirectoryIterator, change: ListChangeListener.Change<out File>) {
        IntStream.range(0, change.addedSubList.size).forEach { index ->
            if (!directoryIterator.add(change.addedSubList[index]))
                imageListView.items.remove(change.addedSubList[index])
        }
    }

    private fun removeChange(directoryIterator: DirectoryIterator, change: ListChangeListener.Change<out File>) {
        val removedFile = change.removed[0]
        directoryIterator.remove(removedFile)
        if (imageMap.contains(change.removed[0])) {
            try {
                imageMap.remove(removedFile.toURI().toURL().toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

        }
    }

    private fun addAtChangePosition(directoryIterator: DirectoryIterator, change: ListChangeListener.Change<out File>) {
        if (!directoryIterator.add(change.from, change.addedSubList[0]))
            imageListView.items.remove(change.addedSubList[0])
    }

    companion object ListChangeListenerFactoryFactory {
        fun createListChangeListenerFactory(imageListView: ListView<File>, imageMap: ImageMap) =
                ListChangeListenerFactory(imageListView, imageMap)
    }
}