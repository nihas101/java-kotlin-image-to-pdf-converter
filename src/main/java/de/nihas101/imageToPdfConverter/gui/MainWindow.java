package de.nihas101.imageToPdfConverter.gui;

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator;
import de.nihas101.imageToPdfConverter.gui.controller.MainWindowController;
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public final class MainWindow extends Application {
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

    public void setupIterator(File directory, ImageToPdfOptions imageToPdfOptions) {
        directoryIterator = DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator(
                directory, imageToPdfOptions.getIteratorOptions()
        );
    }

    public DirectoryIterator getDirectoryIterator() {
        return directoryIterator;
    }

    public void show() {
        launch();
    }
}