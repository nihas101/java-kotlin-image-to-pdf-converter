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

package de.nihas101.imageToPdfConverter.listCell

import de.nihas101.imageToPdfConverter.util.Constants.*
import de.nihas101.imageToPdfConverter.util.ImageMap
import javafx.application.Platform.runLater
import javafx.collections.ObservableList
import javafx.event.Event
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.ContextMenu
import javafx.scene.control.ListCell
import javafx.scene.control.MenuItem
import javafx.scene.image.ImageView
import javafx.scene.input.ClipboardContent
import javafx.scene.input.TransferMode
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import java.io.File


class ImageListCell(private val imageMap: ImageMap, private val files: MutableList<File>, private val observableFiles: ObservableList<File>) : ListCell<File>() {
    private val imageView: ImageView = ImageView()
    private val directoryImageFile = File(DIRECTORY_IMAGE_PATH)

    override fun updateItem(file: File?, empty: Boolean) {
        super.updateItem(file, empty)
        if (empty || file == null) {
            text = null
            graphic = null
        } else {
            setGraphic(file)
            setupImageListCellContextMenu(file)
            setupDragAndDrop()
        }
    }

    private fun setupDragAndDrop() {
        /* Source: stackoverflow.com/questions/20412445/how-to-create-a-reorder-able-tableview-in-javafx */
        setupOnDrag()
        setOnDragOver()
        setOnDragEntered()
        setOnDragExited()
        setOnDragDone()
    }

    private fun setupOnDrag() {
        onDragDetected = EventHandler { event ->
            run {
                if (item == null) return@EventHandler

                val dragBoard = startDragAndDrop(TransferMode.MOVE)
                val content = ClipboardContent()
                content.putString(index.toString())
                dragBoard.dragView = if (!item.isDirectory) imageMap[item]
                else imageMap[directoryImageFile]
                dragBoard.setContent(content)

                event.consume()
            }
        }
    }

    private fun setOnDragOver() = setOnDragOver { event ->
        if (event.gestureSource !== this && (event.dragboard.hasString() || event.dragboard.hasFiles()))
            event.acceptTransferModes(TransferMode.MOVE)
        event.consume()
    }

    private fun setOnDragEntered() = setOnDragEntered { event ->
        if (event.gestureSource !== this && event.dragboard.hasString()) opacity = 0.3
    }

    private fun setOnDragExited() = setOnDragExited { event ->
        if (event.gestureSource !== this && event.dragboard.hasString()) opacity = 1.0
    }

    private fun setOnDragDone() {
        setOnDragDropped { event ->
            if (item == null) return@setOnDragDropped

            val dragBoard = event.dragboard

            if (dragBoard.hasFiles()) {
                observableFiles.addAll(dragBoard.files)
                imageMap.loadImages(dragBoard.files)
            } else if (dragBoard.hasString()) {
                reorder(dragBoard.string.toInt(), index)
            }

            event.isDropCompleted = true
            event.consume()
        }
    }

    private fun setGraphic(file: File?) {
        runLater({
            if (file!!.isDirectory) imageView.image = imageMap[directoryImageFile]
            else imageView.image = imageMap[file]

            if (imageView.image != null) scaleImageView(imageView)

            graphic = createVBox(imageView, cropText(file.name))
        })
    }

    private fun scaleImageView(imageView: ImageView) {
        val scale = (CELL_SIZE / imageView.image.height)
        imageView.fitHeight = imageView.image.height * scale
        imageView.fitWidth = imageView.image.width * scale
    }

    private fun createVBox(imageView: ImageView, text: Text): VBox {
        val vBox = VBox()
        vBox.children.addAll(imageView, text)
        vBox.alignment = Pos.CENTER
        return vBox
    }

    private fun cropText(text: String): Text {
        return if (text.length > LIST_CELL_MAX_STRING_LENGTH)
            Text(text.substring(0, LIST_CELL_MAX_STRING_LENGTH) + "...")
        else
            Text(text)
    }

    private fun setupImageListCellContextMenu(file: File) {
        contextMenu = ContextMenu()

        val deleteItem = setupMenuItem(
                "Remove ${file.name}",
                { event ->
                    removeFromLists()
                    imageMap.remove(file.toURI().toURL().toString())
                    event.consume()
                })

        val moveUpItem = setupMenuItem(
                "Move ${file.name} up",
                { _ -> if (index > 0) moveTo(index - 1) }
        )

        val moveDownItem = setupMenuItem("Move ${file.name} down",
                { _ -> if (index < files.size) moveTo(index + 1) }
        )

        val moveToFrontItem = setupMenuItem(
                "Move ${file.name} to the front",
                { _ -> if (index > 0) moveTo(0) }
        )

        val moveToBackItem = setupMenuItem("Move ${file.name} to the back",
                { _ -> if (index < files.size) moveTo(files.size - 1) }
        )

        if (index == 0) moveUpItem.disableProperty().set(false)
        if (index == files.size - 1) moveDownItem.disableProperty().set(false)
        if (index == 0) moveToFrontItem.disableProperty().set(false)
        if (index == files.size - 1) moveToBackItem.disableProperty().set(false)

        contextMenu.items.addAll(moveUpItem, moveToFrontItem, moveDownItem, moveToBackItem, deleteItem)
    }

    private fun reorder(from: Int, to: Int) = observableFiles.add(to, observableFiles.removeAt(from))

    private fun moveTo(index: Int) = addToLists(index, removeFromLists())

    private fun addToLists(index: Int, file: File) = observableFiles.add(index, file)

    private fun removeFromLists(): File = observableFiles.removeAt(index)

    private fun setupMenuItem(text: String, onAction: (Event) -> Unit): MenuItem {
        val menuItem = MenuItem()
        menuItem.textProperty().set(text)
        menuItem.setOnAction(onAction)

        return menuItem
    }
}