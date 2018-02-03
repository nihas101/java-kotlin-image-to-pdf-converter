package de.nihas101.imagesToPdfConverter.contentDisplay;

import de.nihas101.imagesToPdfConverter.controller.DirectoryContentDisplayController;
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator;
import de.nihas101.imagesToPdfConverter.fileReader.ImageFilesIterator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * An {@link Application} for displaying the content of a directory
 */
public class DirectoryContentDisplay extends Application {
    /**
     * The {@link DirectoryIterator} of which to display the content
     */
    private DirectoryIterator directoryIterator;

    private DirectoryContentDisplay(DirectoryIterator directoryIterator) {
        this.directoryIterator = directoryIterator;
    }

    /**
     * The factory method for creating {@link DirectoryContentDisplay}s
     * @param imageFilesIterator The {@link DirectoryIterator} of which to display the content
     * @return The created {@link ImageFilesIterator} instance
     */
    static DirectoryContentDisplay createDirectoryContentDisplay(DirectoryIterator imageFilesIterator){
        return new DirectoryContentDisplay(imageFilesIterator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        /* Load root-node */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/directoryContentDisplay.fxml"));
        Pane root = loader.load();
        DirectoryContentDisplayController directoryContentDisplayController = loader.getController();
        directoryContentDisplayController.setup(directoryIterator);

        /* Create Scene */
        Scene scene = new Scene(root);

        primaryStage.setTitle("ContentDisplay - " + directoryIterator.getParentDirectory().getAbsolutePath());
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }
}
