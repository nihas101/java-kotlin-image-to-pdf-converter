package de.nihas101.imagesToPdfConverter.listCell

import de.nihas101.imagesToPdfConverter.Constants.*
import de.nihas101.imagesToPdfConverter.ImageMap
import javafx.geometry.Pos
import javafx.scene.control.ListCell
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import java.io.File

class ImageListCell(private val imageMap: ImageMap) : ListCell<File>(){
    private val imageView: ImageView = ImageView()
    private val directoryImageString = File(DIRECTORY_IMAGE_PATH).toURI().toURL().toString()

    override fun updateItem(file: File?, empty: Boolean) {
        super.updateItem(file, empty)
        if (empty || file == null) {
            text = null
            graphic = null
        } else {
            loadImage(file)
        }
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
}