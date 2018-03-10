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

package de.nihas101.imageToPdfConverter.gui.controller;

import de.nihas101.imageToPdfConverter.gui.subStages.ImageDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * The Controller for the {@link ImageDisplay}
 */
public class ImageDisplayController {
    public ImageView imageDisplayView;
    public BorderPane borderPane;

    /**
     * Sets up the {@link ImageDisplayController}
     *
     * @param image The {@link Image} to display
     */
    public void setup(Image image) {
        imageDisplayView.setImage(image);
        imageDisplayView.fitHeightProperty();
        imageDisplayView.fitWidthProperty();

        imageDisplayView.fitWidthProperty().bind(borderPane.widthProperty());
        imageDisplayView.fitHeightProperty().bind(borderPane.heightProperty());
    }
}
