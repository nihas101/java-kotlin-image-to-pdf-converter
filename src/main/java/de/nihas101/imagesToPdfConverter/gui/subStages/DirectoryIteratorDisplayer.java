package de.nihas101.imagesToPdfConverter.gui.subStages;

import de.nihas101.imagesToPdfConverter.gui.controller.MainController;
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator;
import de.nihas101.imagesToPdfConverter.fileReader.ImageFilesIterator;
import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;

import static de.nihas101.imagesToPdfConverter.gui.subStages.DirectoryContentDisplay.createDirectoryContentDisplay;
import static de.nihas101.imagesToPdfConverter.gui.subStages.ImageDisplay.createImageDisplay;

/**
 * A class for displaying the content of a {@link DirectoryIterator}
 */
public final class DirectoryIteratorDisplayer {
    /**
     * The {@link DirectoryIterator} of which to display the content
     */
    private final DirectoryIterator directoryIterator;

    private DirectoryIteratorDisplayer(DirectoryIterator directoryIterator){
        this.directoryIterator = directoryIterator;
    }

    /**
     * The factory method for creating {@link DirectoryIteratorDisplayer}s
     * @param directoryIterator The {@link DirectoryIterator} of which to display the content
     * @return The created {@link DirectoryIteratorDisplayer} instance
     */
    public static DirectoryIteratorDisplayer createContentDisplayer(DirectoryIterator directoryIterator){
        return new DirectoryIteratorDisplayer(directoryIterator);
    }

    /**
     * Displays the content found at the given index of the {@link DirectoryIterator} instance
     * @param index The index of the content to display
     */
    public void displayContent(int index, MainController mainController){
        if(directoryIterator.getFile(index).isDirectory()) displayDirectory(index, mainController);
        else displayImage(index);
    }

    /**
     * Displays the content of the directory found at the given index of the {@link DirectoryIterator}
     * @param index The index of the directory to display
     */
    private void displayDirectory(int index, MainController mainController) {
        DirectoryContentDisplay directoryContentDisplay = createDirectoryContentDisplay(
                ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator(directoryIterator.getFile(index)),
                index,
                mainController
        );

        directoryContentDisplay.displayContent();
    }

    /**
     * Displays the image found at the given index of the {@link DirectoryIterator}
     * @param index The index of the image to display
     */
    private void displayImage(int index) {
        ImageDisplay imageDisplay;
        File file = directoryIterator.getFile(index);

        try {
            imageDisplay = createImageDisplay(
                    new Image(String.valueOf(file.toURI().toURL())),
                    file.getName()
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        imageDisplay.displayImage();
    }
}
