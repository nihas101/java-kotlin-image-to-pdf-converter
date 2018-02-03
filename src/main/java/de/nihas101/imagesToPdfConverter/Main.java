package de.nihas101.imagesToPdfConverter;

import de.nihas101.imagesToPdfConverter.controller.MainController;
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator;
import de.nihas101.imagesToPdfConverter.fileReader.ImageDirectoriesIterator;
import de.nihas101.imagesToPdfConverter.fileReader.ImageFilesIterator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class Main extends Application{
    private DirectoryIterator directoryIterator;

    /**
     * {@inheritDoc}
     */
    public void start(Stage primaryStage) throws IOException {
        /* Load root-node */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main.fxml"));
        GridPane root = loader.load();
        MainController mainController = loader.getController();
        mainController.setup(this);

        /* Create Scene */
        Scene scene = new Scene(root);

        primaryStage.setTitle("Images 2 PDF Converter");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setupIterator(File file) {
        directoryIterator = ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator(file);
    }

    public void setupDirectoriesIterator(File file){
        directoryIterator = ImageDirectoriesIterator.ImageDirectoriesIteratorFactory.createImageDirectoriesIterator(file);
    }

    public DirectoryIterator getDirectoryIterator() {
        return directoryIterator;
    }
}