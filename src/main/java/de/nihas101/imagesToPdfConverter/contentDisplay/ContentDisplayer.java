package de.nihas101.imagesToPdfConverter.contentDisplay;

import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator;
import de.nihas101.imagesToPdfConverter.fileReader.ImageFilesIterator;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

import static de.nihas101.imagesToPdfConverter.contentDisplay.DirectoryContentDisplay.createDirectoryContentDisplay;
import static de.nihas101.imagesToPdfConverter.contentDisplay.ImageDisplay.createImageDisplay;

public class ContentDisplayer {
    private final DirectoryIterator directoryIterator;

    private ContentDisplayer(DirectoryIterator directoryIterator){
        this.directoryIterator = directoryIterator;
    }

    public static ContentDisplayer createContentDisplayer(DirectoryIterator directoryIterator){
        return new ContentDisplayer(directoryIterator);
    }

    public void displayContent(int index){
        if(directoryIterator.getFile(index).isDirectory()) displayDirectory(index);
        else displayImage(index);
    }

    private void displayDirectory(int index) {
        /* TODO: Add a build button on this scene, so single pdfs can be changed and build
         * TODO: -> If this is done, remove it from the main scene list! */
        DirectoryContentDisplay directoryContentDisplay = createDirectoryContentDisplay(
                ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator(directoryIterator.getFile(index))
        );

        try { directoryContentDisplay.start(new Stage()); }
        catch (Exception e) { e.printStackTrace(); }
    }

    private void displayImage(int index) {
        ImageDisplay imageDisplay;
        File file = directoryIterator.getFile(index);

        try {
            imageDisplay = createImageDisplay(
                    new Image(String.valueOf(file.toURI().toURL())),
                    file.getAbsolutePath()
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        try { imageDisplay.start(new Stage()); }
        catch (Exception e) { e.printStackTrace(); }
    }
}
