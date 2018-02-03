package de.nihas101.imagesToPdfConverter.contentDisplay;

import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator;
import de.nihas101.imagesToPdfConverter.fileReader.ImageFilesIterator;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

import static de.nihas101.imagesToPdfConverter.contentDisplay.DirectoryContentDisplay.createDirectoryContentDisplay;
import static de.nihas101.imagesToPdfConverter.contentDisplay.ImageDisplay.createImageDisplay;

/**
 * A class for displaying the content of a {@link DirectoryIterator}
 */
public class DirectoryIteratorDisplayer {
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
    public void displayContent(int index){
        if(directoryIterator.getFile(index).isDirectory()) displayDirectory(index);
        else displayImage(index);
    }

    /**
     * Displays the content of the directory found at the given index of the {@link DirectoryIterator}
     * @param index The index of the directory to display
     */
    private void displayDirectory(int index) {
        /* TODO: Add a build button on this scene, so single pdfs can be changed and build
         * TODO: -> If this is done, remove it from the main scene list! */
        DirectoryContentDisplay directoryContentDisplay = createDirectoryContentDisplay(
                ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator(directoryIterator.getFile(index))
        );

        try { directoryContentDisplay.start(new Stage()); }
        catch (Exception e) { e.printStackTrace(); }
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

        try { imageDisplay.start(new Stage()); }
        catch (Exception e) { e.printStackTrace(); }
    }
}
