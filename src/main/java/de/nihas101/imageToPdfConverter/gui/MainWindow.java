package de.nihas101.imageToPdfConverter.gui;

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator;
import de.nihas101.imageToPdfConverter.gui.controller.MainWindowController;
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions;
import de.nihas101.imageToPdfConverter.tasks.TaskManager;
import de.nihas101.imageToPdfConverter.util.ImageMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public final class MainWindow extends Application {
    private DirectoryIterator directoryIterator;
    /* TODO: Move these to MainWindow */

    /**
     * The directory from which to load more {@link File}s
     */
    public File chosenDirectory = new File("");
    /**
     * The {@link ImageMap} holding the loaded {@link Image}s
     */
    public ImageMap imageMap;

    /**
     * The selected {@link ImageToPdfOptions} for building the PDF(s)
     */
    public ImageToPdfOptions imageToPdfOptions;

    public FileChooser saveFileChooser;

    public TaskManager taskManager = TaskManager.TaskManagerFactory.createTaskManager();

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

    public void setupIterator(DirectoryIterator directoryIterator) {
        this.directoryIterator = directoryIterator;
    }

    /* TODO: Add Shortcut for building: Like pressing b */

    public DirectoryIterator getDirectoryIterator() {
        return directoryIterator;
    }

    public void show() {
        launch();
    }
}