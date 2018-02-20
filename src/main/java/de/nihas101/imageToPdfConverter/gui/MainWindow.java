package de.nihas101.imageToPdfConverter.gui;

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator;
import de.nihas101.imageToPdfConverter.directoryIterators.ImageDirectoriesIterator;
import de.nihas101.imageToPdfConverter.directoryIterators.ImageFilesIterator;
import de.nihas101.imageToPdfConverter.gui.controller.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public final class MainWindow extends Application{
    private DirectoryIterator directoryIterator;

    /**
     * {@inheritDoc}
     */
    public void start(Stage primaryStage) throws IOException {
        /* Load root-node */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main.fxml"));
        GridPane root = loader.load();
        MainWindowController mainWindowController = loader.getController();
        mainWindowController.setup(this);

        /* Create Scene */
        Scene scene = new Scene(root);
        mainWindowController.setupKeyEvents(scene);

        primaryStage.setTitle("Image2PDF Converter");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setupIterator(File file, boolean isImageDirectoryIterator){
        if(isImageDirectoryIterator)
            directoryIterator = ImageDirectoriesIterator.ImageDirectoriesIteratorFactory.createImageDirectoriesIterator(file);
        else
            directoryIterator = ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator(file);
    }

    public DirectoryIterator getDirectoryIterator() {
        return directoryIterator;
    }

    public void show() {
        launch();
    }
}