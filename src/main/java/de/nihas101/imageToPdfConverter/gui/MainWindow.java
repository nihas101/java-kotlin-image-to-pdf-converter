/*
 JaKoImage2PDF is a program for converting images to PDFs with the use of iText 7
 Copyright (C) 2018  Nikita Hasert

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.nihas101.imageToPdfConverter.gui;

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator;
import de.nihas101.imageToPdfConverter.gui.controller.MainWindowController;
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions;
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions;
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.PdfOptions;
import de.nihas101.imageToPdfConverter.tasks.TaskManager;
import de.nihas101.imageToPdfConverter.util.ImageMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static de.nihas101.imageToPdfConverter.gui.subStages.DirectoryIteratorDisplayer.createContentDisplayer;
import static de.nihas101.imageToPdfConverter.gui.subStages.OptionsMenu.createOptionsMenu;
import static de.nihas101.imageToPdfConverter.util.FileChooserFactoryKt.createDirectoryChooser;
import static de.nihas101.imageToPdfConverter.util.FileChooserFactoryKt.createSaveFileChooser;
import static de.nihas101.imageToPdfConverter.util.ImageMap.createImageMap;


public final class MainWindow extends Application {
    private MainWindowController mainWindowController;
    private Scene scene;

    private DirectoryIterator directoryIterator;

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

    private FileChooser saveFileChooser;

    public TaskManager taskManager = TaskManager.TaskManagerFactory.createTaskManager();

    /**
     * {@inheritDoc}
     */
    public void start(Stage primaryStage) throws IOException {
        /* Load root-node */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main.fxml"));
        GridPane root = loader.load();
        mainWindowController = loader.getController();
        setupMainWindow();
        setupOnExit(primaryStage);
        mainWindowController.setup(this);

        /* Create Scene */
        scene = new Scene(root);
        mainWindowController.setupKeyEvents(scene);

        primaryStage.setTitle("JaKoImage2PDF Converter");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private void setupMainWindow() {
        saveFileChooser = createSaveFileChooser();
        imageMap = createImageMap();

        imageToPdfOptions = ImageToPdfOptions.OptionsFactory.createOptions(
                new IteratorOptions(),
                new PdfOptions()
        );
    }

    private void setupOnExit(Stage stage) {
        stage.setOnCloseRequest(event -> taskManager.cancelAllTasks());
    }

    public File openSaveFileChooser(File initialDirectory, String initialFileName) {
        saveFileChooser.setInitialFileName(initialFileName);
        saveFileChooser.setInitialDirectory(initialDirectory);
        return saveFileChooser.showSaveDialog(scene.getWindow());
    }

    public File openDirectoryChooser(File initialDirectory) {
        DirectoryChooser directoryChooser = createDirectoryChooser(imageToPdfOptions.getIteratorOptions());
        directoryChooser.setInitialDirectory(initialDirectory);
        directoryChooser.setTitle("Choose a folder to save the PDFs in");
        return directoryChooser.showDialog(scene.getWindow());
    }

    public void openContentDisplay(int index) {
        try {
            createContentDisplayer(directoryIterator).displayContent(index, mainWindowController);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void openOptionsMenu() {
        try {
            imageToPdfOptions = createOptionsMenu(imageToPdfOptions).setOptions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSaveLocation(File saveLocation) {
        imageToPdfOptions.setSaveLocation(saveLocation);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setupIterator(DirectoryIterator directoryIterator) {
        this.directoryIterator = directoryIterator;
    }

    public DirectoryIterator getDirectoryIterator() {
        return directoryIterator;
    }

    public void show() {
        launch();
    }
}