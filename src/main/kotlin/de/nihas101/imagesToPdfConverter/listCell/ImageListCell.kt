package de.nihas101.imagesToPdfConverter.listCell

import de.nihas101.imagesToPdfConverter.Constants.*
import de.nihas101.imagesToPdfConverter.ImageMap
import javafx.collections.ObservableList
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


class ImageListCell(private val imageMap: ImageMap, private val files: MutableList<File>, private val observableFiles: ObservableList<File>) : ListCell<File>(){
    private val imageView: ImageView = ImageView()
    private val directoryImageString = File(DIRECTORY_IMAGE_PATH).toURI().toURL().toString()

    override fun updateItem(file: File?, empty: Boolean) {
        super.updateItem(file, empty)
        if (empty || file == null) {
            text = null
            graphic = null
        } else {
            loadImage(file)
            setupImageListCellContextMenu(file, files, observableFiles)
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
        onDragDetected = EventHandler {
            event -> run {
                if (item == null) return@EventHandler

                val dragBoard = startDragAndDrop(TransferMode.MOVE)
                val content = ClipboardContent()
                content.putString(index.toString())
                dragBoard.dragView = imageMap[item.toURI().toURL().toString()]
                dragBoard.setContent(content)

                event.consume()
            }
        }
    }

    private fun setOnDragOver() {
        setOnDragOver { event ->
            if (event.gestureSource !== this && event.dragboard.hasString()) event.acceptTransferModes(TransferMode.MOVE)
            event.consume()
        }
    }

    private fun setOnDragEntered() {
        setOnDragEntered { event ->
            if (event.gestureSource !== this && event.dragboard.hasString()) opacity = 0.3
        }
    }

    private fun setOnDragExited(){
        setOnDragExited { event ->
            if (event.gestureSource !== this && event.dragboard.hasString()) opacity = 1.0
        }
    }

    private fun setOnDragDone() {
        setOnDragDropped { event ->
            if (item == null) return@setOnDragDropped

            val dragBoard = event.dragboard
            var success = false

            if (dragBoard.hasString()) {
                reorder(dragBoard.string.toInt(), index)
                success = true
            }

            event.isDropCompleted = success
            event.consume()
        }
    }

    private fun reorder(from: Int, to: Int){
        val file = files.removeAt(from)
        observableFiles.removeAt(from)
        files.add(to,file)
        observableFiles.add(to,file)
    }

    private fun loadImage(file: File?) {
        if(file!!.isDirectory)
            imageView.image = imageMap[directoryImageString]
        else
            imageView.image = imageMap[file.toURI().toURL().toString()]

        if(imageView.image != null) scaleImageView(imageView)

        graphic = createVBox(imageView, cropText(file.name))
    }

    private fun scaleImageView(imageView: ImageView){
        val scale = (CELL_SIZE /imageView.image.height)
        imageView.fitHeight = imageView.image.height * scale
        imageView.fitWidth  = imageView.image.width * scale
    }

    private fun createVBox(imageView: ImageView, text: Text): VBox{
        val vBox = VBox()
        vBox.children.addAll(imageView, text)
        vBox.alignment = Pos.CENTER
        return vBox
    }

    private fun cropText(text: String): Text{
        return if(text.length > LIST_CELL_MAX_STRING_LENGTH)
            Text(text.substring(0,LIST_CELL_MAX_STRING_LENGTH) + "...")
        else
            Text(text)
    }

    private fun setupImageListCellContextMenu(file: File, files: MutableList<File>, observableFiles: ObservableList<File>){
        contextMenu = ContextMenu()

        val deleteItem = setupMenuItem(file, files, observableFiles)
        contextMenu.items.add(deleteItem)
    }

    private fun setupMenuItem(file: File, files: MutableList<File>, observableFiles: ObservableList<File>): MenuItem{
        val menuItem = MenuItem()
        menuItem.textProperty().set("Remove ${file.name}")
        menuItem.setOnAction({ _ ->
            files.removeAt(this.index)
            observableFiles.removeAt(this.index)
            imageMap.remove(file.toURI().toURL().toString())
        })

        return menuItem
    }
}