package de.nihas101.imagesToPdfConverter.gui;

import de.nihas101.imagesToPdfConverter.gui.controller.MainWindowController;
import de.nihas101.imagesToPdfConverter.directoryIterators.DirectoryIterator;
import de.nihas101.imagesToPdfConverter.directoryIterators.ImageDirectoriesIterator;
import de.nihas101.imagesToPdfConverter.directoryIterators.ImageFilesIterator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static javafx.scene.input.KeyEvent.KEY_PRESSED;


public final class MainWindow extends Application{
    private DirectoryIterator directoryIterator;
    private MainWindowController mainWindowController;

    /**
     * {@inheritDoc}
     */
    public void start(Stage primaryStage) throws IOException {
        /* Load root-node */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main.fxml"));
        GridPane root = loader.load();
        mainWindowController = loader.getController();
        mainWindowController.setup(this);

        /* Create Scene */
        Scene scene = new Scene(root);
        setupKeyEvents(scene);

        primaryStage.setTitle("Images 2 PDF Converter");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setupIterator(File file, boolean isImageDirectoryIterator){
        if(isImageDirectoryIterator){
            directoryIterator = ImageDirectoriesIterator.ImageDirectoriesIteratorFactory.createImageDirectoriesIterator(file);
        }else{
            directoryIterator = ImageFilesIterator.ImageFilesIteratorFactory.createImageFilesIterator(file);
        }
    }

    private void setupKeyEvents(Scene scene){
        scene.addEventHandler(KEY_PRESSED, (event) -> {
            switch(event.getCode()){
                case DELETE: {
                    if(mainWindowController.imageListView.getSelectionModel().getSelectedIndex() > -1)
                        mainWindowController.imageListView.getItems().remove(mainWindowController.imageListView.getSelectionModel().getSelectedIndex());
                } break;
                default: /* NOP */
            }
        });
    }

    public DirectoryIterator getDirectoryIterator() {
        return directoryIterator;
    }

    public void show() {
        launch();
    }
}