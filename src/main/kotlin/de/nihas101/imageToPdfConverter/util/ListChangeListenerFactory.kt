package de.nihas101.imageToPdfConverter.util

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator
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