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

package de.nihas101.imageToPdfConverter.gui.subStages;

import de.nihas101.imageToPdfConverter.gui.controller.ImageDisplayController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static de.nihas101.imageToPdfConverter.util.Constants.IMAGE_DISPLAY_MAX_HEIGHT;
import static de.nihas101.imageToPdfConverter.util.Constants.IMAGE_DISPLAY_MAX_WIDTH;

/**
 * An Application for displaying {@link Image}s
 */
public final class ImageDisplay extends Application {
    /**
     * The {@link Image} to display
     */
    private Image image;
    /**
     * The name of the {@link Image}
     */
    private String imageName;

    private ImageDisplay(Image image, String imageName) {
        this.image = image;
        this.imageName = imageName;
    }

    /**
     * The factory method for creating {@link ImageDisplay}s
     *
     * @param image     The {@link Image} to display
     * @param imageName The name of the {@link Image}
     */
    public static ImageDisplay createImageDisplay(Image image, String imageName) {
        return new ImageDisplay(image, imageName);
    }

    public void displayImage() {
        try {
            start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        /* Load root-node */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/imageDisplay.fxml"));
        Pane root = loader.load();
        ImageDisplayController imageDisplayController = loader.getController();
        imageDisplayController.setup(image);

        /* Create Scene */
        Scene scene = new Scene(root);

        primaryStage.setTitle("ImageDisplay - " + imageName);
        primaryStage.setScene(scene);
        if (image.getHeight() > IMAGE_DISPLAY_MAX_HEIGHT || image.getWidth() > IMAGE_DISPLAY_MAX_WIDTH) {
            double scale = calculateScale(image);
            primaryStage.setHeight(image.getHeight() * scale);
            primaryStage.setWidth(image.getWidth() * scale);
        } else {
            primaryStage.setHeight(image.getHeight());
            primaryStage.setWidth(image.getWidth());
        }
        primaryStage.showAndWait();
    }

    private double calculateScale(Image image) {
        if (image.getWidth() > image.getHeight()) return IMAGE_DISPLAY_MAX_WIDTH / image.getWidth();
        else return IMAGE_DISPLAY_MAX_HEIGHT / image.getHeight();
    }
}
