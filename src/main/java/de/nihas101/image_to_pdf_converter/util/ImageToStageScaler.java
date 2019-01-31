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

package de.nihas101.image_to_pdf_converter.util;

import javafx.scene.image.Image;

import static de.nihas101.image_to_pdf_converter.util.Constants.IMAGE_DISPLAY_MAX_HEIGHT;
import static de.nihas101.image_to_pdf_converter.util.Constants.IMAGE_DISPLAY_MAX_WIDTH;

public class ImageToStageScaler {
    public static double calculateScaleOfStage(Image image) {
        if (needToScaleAccordingToStage(image)) return scaleAccordingToStage(image);
        else if (needToScaleAccordingToImage(image)) return scaleAccordingToImage(image);
        else return 1;
    }

    private static boolean needToScaleAccordingToStage(Image image) {
        return (image.getHeight() > IMAGE_DISPLAY_MAX_HEIGHT && image.getWidth() > IMAGE_DISPLAY_MAX_WIDTH) ||
                (image.getHeight() < IMAGE_DISPLAY_MAX_HEIGHT && image.getWidth() < IMAGE_DISPLAY_MAX_WIDTH);

    }

    private static boolean needToScaleAccordingToImage(Image image) {
        return image.getHeight() < IMAGE_DISPLAY_MAX_HEIGHT || image.getWidth() < IMAGE_DISPLAY_MAX_WIDTH;
    }

    private static double scaleAccordingToStage(Image image) {
        if (image.getWidth() > image.getHeight()) return IMAGE_DISPLAY_MAX_WIDTH / image.getWidth();
        else return IMAGE_DISPLAY_MAX_HEIGHT / image.getHeight();
    }

    private static double scaleAccordingToImage(Image image) {
        if (image.getWidth() <= image.getHeight()) return image.getWidth() / IMAGE_DISPLAY_MAX_WIDTH;
        else return image.getHeight() / IMAGE_DISPLAY_MAX_HEIGHT;
    }
}
