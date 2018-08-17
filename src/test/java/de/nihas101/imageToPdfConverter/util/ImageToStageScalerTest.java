package de.nihas101.imageToPdfConverter.util;

import javafx.scene.image.WritableImage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static de.nihas101.imageToPdfConverter.util.Constants.IMAGE_DISPLAY_MAX_HEIGHT;
import static de.nihas101.imageToPdfConverter.util.Constants.IMAGE_DISPLAY_MAX_WIDTH;
import static de.nihas101.imageToPdfConverter.util.ImageToStageScaler.calculateScaleOfStage;
import static org.junit.Assert.assertEquals;

public class ImageToStageScalerTest extends ApplicationTest {

    @Test
    public void calculateScaleOfStageNoScaling() {
        double scale = calculateScaleOfStage(new WritableImage(IMAGE_DISPLAY_MAX_WIDTH, IMAGE_DISPLAY_MAX_HEIGHT));

        assertEquals(1, scale, 0.00001);
    }

    @Test
    public void calculateScaleOfStageTooLarge() {
        double scale = calculateScaleOfStage(new WritableImage(IMAGE_DISPLAY_MAX_WIDTH * 2, IMAGE_DISPLAY_MAX_HEIGHT * 2));

        assertEquals(.5, scale, 0.00001);
    }

    @Test
    public void calculateScaleOfStageTooLargeHeightLargerThanWidth() {
        double scale = calculateScaleOfStage(new WritableImage(IMAGE_DISPLAY_MAX_WIDTH * 2, IMAGE_DISPLAY_MAX_WIDTH * 4));

        assertEquals(.140625, scale, 0.00001);
    }

    @Test
    public void calculateScaleOfStageWidthTooSmallHeightTooLarge() {
        double scale = calculateScaleOfStage(new WritableImage((int) (IMAGE_DISPLAY_MAX_WIDTH * .5), IMAGE_DISPLAY_MAX_WIDTH * 2));

        assertEquals(.5, scale, 0.00001);
    }

    @Test
    public void calculateScaleOfStageWidthTooLargeWidthTooSmall() {
        double scale = calculateScaleOfStage(new WritableImage(IMAGE_DISPLAY_MAX_WIDTH * 2, (int) (IMAGE_DISPLAY_MAX_WIDTH * .5)));

        assertEquals(0.888888888888888888888, scale, 0.00001);
    }

    @Test
    public void calculateScaleOfStageTooSmall() {
        double scale = calculateScaleOfStage(new WritableImage((int) (IMAGE_DISPLAY_MAX_WIDTH * .5), (int) (IMAGE_DISPLAY_MAX_HEIGHT * .5)));

        assertEquals(2.0, scale, 0.00001);
    }

    @Test
    public void calculateScaleOfStageHeightWidthSwitched() {
        double scale = calculateScaleOfStage(new WritableImage(IMAGE_DISPLAY_MAX_HEIGHT, IMAGE_DISPLAY_MAX_WIDTH));

        assertEquals(0.5625, scale, 0.00001);
    }

    @Test
    public void calculateScaleOfStageHeightEqualsWidth() {
        double scale = calculateScaleOfStage(new WritableImage(IMAGE_DISPLAY_MAX_HEIGHT, IMAGE_DISPLAY_MAX_HEIGHT));

        assertEquals(0.5625, scale, 0.00001);
    }

    @Test
    public void calculateScaleOfStageWidthEqualsHeight() {
        double scale = calculateScaleOfStage(new WritableImage(IMAGE_DISPLAY_MAX_WIDTH, IMAGE_DISPLAY_MAX_WIDTH));

        assertEquals(1.0, scale, 0.00001);
    }
}