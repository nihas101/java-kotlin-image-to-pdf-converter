package de.nihas101.imageToPdfConverter.gui.controller;

import de.nihas101.imageToPdfConverter.gui.subStages.ImageDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * The Controller for the {@link ImageDisplay}
 */
public class ImageDisplayController {
    public ImageView imageView;
    public BorderPane borderPane;

    /**
     * Sets up the {@link ImageDisplayController}
     * @param image The {@link Image} to display
     */
    public void setup(Image image) {
        imageView.setImage(image);
        imageView.fitHeightProperty();
        imageView.fitWidthProperty();

        imageView.fitWidthProperty().bind(borderPane.widthProperty());
        imageView.fitHeightProperty().bind(borderPane.heightProperty());
    }
}
