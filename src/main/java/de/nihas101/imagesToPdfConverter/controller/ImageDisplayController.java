package de.nihas101.imagesToPdfConverter.controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class ImageDisplayController {
    public ImageView imageView;
    public BorderPane borderPane;

    public void setup(Image image) {
        imageView.setImage(image);
        imageView.fitHeightProperty();
        imageView.fitWidthProperty();

        imageView.fitWidthProperty().bind(borderPane.widthProperty());
        imageView.fitHeightProperty().bind(borderPane.heightProperty());
    }
}
