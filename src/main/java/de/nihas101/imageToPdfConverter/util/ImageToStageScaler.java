package de.nihas101.imageToPdfConverter.util;

import javafx.scene.image.Image;

import static de.nihas101.imageToPdfConverter.util.Constants.IMAGE_DISPLAY_MAX_HEIGHT;
import static de.nihas101.imageToPdfConverter.util.Constants.IMAGE_DISPLAY_MAX_WIDTH;

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
