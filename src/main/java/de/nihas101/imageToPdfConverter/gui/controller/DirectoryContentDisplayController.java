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

package de.nihas101.imageToPdfConverter.gui.controller;

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator;
import de.nihas101.imageToPdfConverter.gui.subStages.DirectoryContentDisplay;
import de.nihas101.imageToPdfConverter.listCell.ImageListCell;
import de.nihas101.imageToPdfConverter.pdf.builders.PdfBuilder;
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions;
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions;
import de.nihas101.imageToPdfConverter.tasks.BuildPdfTask;
import de.nihas101.imageToPdfConverter.tasks.CallClosure;
import de.nihas101.imageToPdfConverter.tasks.LoadImagesTask;
import de.nihas101.imageToPdfConverter.util.BuildProgressUpdater;
import de.nihas101.imageToPdfConverter.util.ImageMap;
import de.nihas101.imageToPdfConverter.util.JaKoLogger;
import de.nihas101.imageToPdfConverter.util.TrivialProgressUpdater;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import kotlin.Unit;

import java.io.File;
import java.util.Map;

import static de.nihas101.imageToPdfConverter.gui.subStages.DirectoryIteratorDisplayer.createContentDisplayer;
import static de.nihas101.imageToPdfConverter.util.ImageMap.createImageMap;
import static de.nihas101.imageToPdfConverter.util.JaKoLogger.createLogger;
import static javafx.application.Platform.runLater;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.paint.Color.GREEN;

/**
 * The Controller of the {@link DirectoryContentDisplay} {@link Application}
 */
public class DirectoryContentDisplayController extends FileListViewController {
    @FXML
    public Button buildButton;
    /**
     * The {@link DirectoryIterator} for iterating over directories
     */
    private DirectoryIterator directoryIterator;
    private Stage directoryContentDisplayStage;
    private MainWindowController mainWindowController;
    private ImageToPdfOptions imageToPdfOptions;
    private int directoryIteratorIndex;

    private static JaKoLogger logger = createLogger(DirectoryContentDisplayController.class);

    /**
     * Sets up the {@link DirectoryContentDisplayController}
     *
     * @param directoryIterator The {@link DirectoryIterator} for iterating over directories
     */
    public void setup(DirectoryIterator directoryIterator, int directoryIteratorIndex, Stage directoryContentDisplayStage, MainWindowController mainWindowController) {
        this.directoryIterator = directoryIterator;
        this.directoryIteratorIndex = directoryIteratorIndex;
        this.directoryContentDisplayStage = directoryContentDisplayStage;
        this.mainWindowController = mainWindowController;
        this.imageToPdfOptions = mainWindowController.mainWindow.imageToPdfOptions.copyForJava();

        startLoadImagesThread(createImageMap());
    }

    private void startLoadImagesThread(ImageMap imageMap) {
        LoadImagesTask loadImagesTask = LoadImagesTask.LoadImagesTaskFactory.createLoadImagesTask(
                imageMap,
                directoryIterator,
                new TrivialProgressUpdater(),
                () -> {
                    setupObservableList(directoryIterator, imageMap);
                    return Unit.INSTANCE;
                });

        mainWindowController.mainWindow.taskManager.start(loadImagesTask, true);
    }

    /**
     * Sets up the {@link ObservableList<File>} and adds it to the {@link ListView<File>}
     *
     * @param directoryIterator The {@link DirectoryIterator} for iterating over directories
     * @param imageMap          The {@link Map} holding the images to be drawn in the list
     */
    private void setupObservableList(DirectoryIterator directoryIterator, ImageMap imageMap) {
        ObservableList<File> observableFiles = observableArrayList(directoryIterator.getFileList());
        observableFiles.addListener(setupListChangeListener(directoryIterator));
        imageListView.setItems(observableFiles);
        imageListView.setCellFactory(param -> new ImageListCell(imageMap, directoryIterator.getFileList(), observableFiles));
        logger.info("{}", "Set up observable list.");
    }

    /**
     * Sets up a {@link ListChangeListener} that forwards all changes on the {@link ObservableList} to the underlying {@link java.util.List}
     *
     * @param directoryIterator The {@link DirectoryIterator} for iterating over directories
     * @return The created {@link ListChangeListener}
     */
    private ListChangeListener<File> setupListChangeListener(DirectoryIterator directoryIterator) {
        return change -> {
            while (change.next()) handleChange(directoryIterator, change);
        };
    }

    private void handleChange(DirectoryIterator directoryIterator, ListChangeListener.Change<? extends File> change) {
        if (change.wasRemoved()) directoryIterator.getFileList().remove(change.getRemoved().get(0));
        if (change.wasAdded()) directoryIterator.getFileList().add(change.getFrom(), change.getAddedSubList().get(0));
    }

    /**
     * Displays the content of the {@link DirectoryIterator}
     *
     * @param mouseEvent The delivered {@link Event}
     */
    public void displayListCell(MouseEvent mouseEvent) {
        if (imageListView.getItems().size() == 0) return;

        if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
            int index = imageListView.getSelectionModel().getSelectedIndex();
            logger.info("Displaying image with index {}", index);
            createContentDisplayer(directoryIterator).displayContent(index, mainWindowController);
        }

        mouseEvent.consume();
    }

    public void buildPDF(ActionEvent actionEvent) {
        File saveFile = mainWindowController.mainWindow.openSaveFileChooser(
                directoryIterator.getParentDirectory(),
                directoryIterator.getParentDirectory().getName() + ".pdf"
        );

        if (saveFile != null) {
            imageToPdfOptions.setSaveLocation(saveFile);

            CallClosure callClosure = new CallClosure(
                    () -> Unit.INSTANCE,
                    () -> {
                        runLater(() -> {
                            mainWindowController.buildProgressBar.setProgress(0);
                            mainWindowController.notifyUser("Finished building: " + saveFile.getAbsolutePath(), GREEN);
                            mainWindowController.imageListView.getItems().remove(directoryIteratorIndex);
                            directoryContentDisplayStage.close();
                        });
                        return Unit.INSTANCE;
                    });

            BuildPdfTask buildPdfTask = BuildPdfTask.BuildPdfTaskFactory.createBuildPdfTask(
                    PdfBuilder.PdfBuilderFactory.createPdfBBuilder(new IteratorOptions()),
                    directoryIterator,
                    imageToPdfOptions,
                    new BuildProgressUpdater(mainWindowController),
                    callClosure
            );

            mainWindowController.mainWindow.taskManager.start(buildPdfTask, true);
        }

        actionEvent.consume();
    }
}
