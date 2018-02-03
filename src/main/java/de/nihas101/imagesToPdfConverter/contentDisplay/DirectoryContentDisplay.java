package de.nihas101.imagesToPdfConverter.contentDisplay;

import de.nihas101.imagesToPdfConverter.controller.DirectoryContentDisplayController;
import de.nihas101.imagesToPdfConverter.fileReader.ImageFilesIterator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DirectoryContentDisplay extends Application {
    private ImageFilesIterator imageFilesIterator;

    private DirectoryContentDisplay(ImageFilesIterator imageFilesIterator) {
        this.imageFilesIterator = imageFilesIterator;
    }

    static DirectoryContentDisplay createDirectoryContentDisplay(ImageFilesIterator imageFilesIterator){
        return new DirectoryContentDisplay(imageFilesIterator);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        /* Load root-node */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/directoryContentDisplay.fxml"));
        Pane root = loader.load();
        DirectoryContentDisplayController directoryContentDisplayController = loader.getController();
        directoryContentDisplayController.setup(imageFilesIterator);

        /* Create Scene */
        Scene scene = new Scene(root);

        primaryStage.setTitle("ContentDisplay - " + imageFilesIterator.getParentDirectory().getAbsolutePath());
        primaryStage.setScene(scene);
        //primaryStage.setHeight();
        //primaryStage.setWidth();
        primaryStage.show();
        primaryStage.setResizable(false);
    }
}
