package de.nihas101.imagesToPdfConverter;

import de.nihas101.imagesToPdfConverter.fileReader.ImageFilesIterator;
import javafx.application.Application;
import javafx.stage.Stage;

public class DirectoryContentDisplay extends Application {
    ImageFilesIterator imageFilesIterator;

    private DirectoryContentDisplay(ImageFilesIterator imageFilesIterator) {
        this.imageFilesIterator = imageFilesIterator;
    }

    public static DirectoryContentDisplay createDirectoryContentDisplay(ImageFilesIterator imageFilesIterator){
        return new DirectoryContentDisplay(imageFilesIterator);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
            /* TODO: Setup stage */
    }
}
