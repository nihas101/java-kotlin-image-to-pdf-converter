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
import de.nihas101.imageToPdfConverter.gui.MainWindow;
import de.nihas101.imageToPdfConverter.listCell.ImageListCell;
import de.nihas101.imageToPdfConverter.pdf.builders.PdfBuilder;
import de.nihas101.imageToPdfConverter.tasks.BuildPdfTask;
import de.nihas101.imageToPdfConverter.tasks.LoadImagesTask;
import de.nihas101.imageToPdfConverter.tasks.SetupIteratorFromDragAndDropTask;
import de.nihas101.imageToPdfConverter.tasks.SetupIteratorTask;
import de.nihas101.imageToPdfConverter.util.BuildProgressUpdater;
import de.nihas101.imageToPdfConverter.util.ListChangeListenerFactory;
import de.nihas101.imageToPdfConverter.util.LoadProgressUpdater;
import de.nihas101.imageToPdfConverter.util.ProgressUpdater;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import kotlin.Unit;

import java.io.File;
import java.util.List;

import static de.nihas101.imageToPdfConverter.util.Constants.NOTIFICATION_MAX_STRING_LENGTH;
import static de.nihas101.imageToPdfConverter.util.FileChooserFactoryKt.createDirectoryChooser;
import static de.nihas101.imageToPdfConverter.util.FileChooserFactoryKt.createZipFileChooser;
import static javafx.application.Platform.runLater;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.paint.Color.*;

public class MainWindowController extends FileListViewController {
    @FXML
    public Button directoryButton;
    @FXML
    public Button buildButton;
    @FXML
    public ProgressBar buildProgressBar;
    @FXML
    public Text notificationText;
    @FXML
    public TextFlow notificationTextFlow;
    @FXML
    public Button optionsButton;
    public ProgressIndicator progressIndicator;
    /**
     * The {@link MainWindow} belonging to this Controller
     */
    public MainWindow mainWindow;

    private ListChangeListenerFactory listChangeListenerFactory;

    /**
     * Sets up the {@link MainWindowController}
     *
     * @param mainWindow The {@link MainWindow} belonging to this Controller
     */
    public void setup(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        listChangeListenerFactory = ListChangeListenerFactory.ListChangeListenerFactoryFactory
                .createListChangeListenerFactory(imageListView, mainWindow.imageMap);

        setOnDrag();
    }

    private void setOnDrag() {
        imageListView.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != imageListView && dragEvent.getDragboard().hasFiles()) {
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            dragEvent.consume();
        });

        imageListView.setOnDragDropped(dragEvent -> {
            if (dragEvent.getDragboard().hasFiles()) {
                List<File> files = dragEvent.getDragboard().getFiles();
                setupIteratorFromDragAndDrop(files);
                dragEvent.setDropCompleted(true);
            }

            dragEvent.consume();
        });
    }

    private void setupIteratorFromDragAndDrop(List<File> files) {
        mainWindow.chosenDirectory = files.get(0);
        startSetupIteratorFromDragAndDropThread(files);
    }

    private void startSetupIteratorFromDragAndDropThread(List<File> files) {
        DirectoryIterator directoryIterator = DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator(
                mainWindow.imageToPdfOptions.getIteratorOptions()
        );

        SetupIteratorFromDragAndDropTask setupIteratorFromDragAndDropTask =
                SetupIteratorFromDragAndDropTask.SetupIteratorFromDragAndDropTaskFactory.createSetupIteratorTask(
                        directoryIterator,
                        files.get(0),
                        () -> {
                            disableInput(true);
                            notifyUser("Preparing files...", BLACK);
                            return Unit.INSTANCE;
                        },
                        () -> {
                            mainWindow.setupIterator(directoryIterator);
                            runLater(() -> setupListView(mainWindow.getDirectoryIterator()));
                            if (files.size() > 1) {
                                runLater(() -> {
                                    buildProgressBar.setProgress(0.0);
                                    notifyUser("Preparing files...", BLACK);
                                    mainWindow.getDirectoryIterator().addAll(files.subList(1, files.size()));
                                    imageListView.getItems().addAll(files.subList(1, files.size()));
                                });
                            }
                            disableInput(false);
                            return Unit.INSTANCE;
                        }
                );

        mainWindow.taskManager.start(setupIteratorFromDragAndDropTask, true);
    }

    /**
     * Opens a {@link DirectoryChooser} and loads the files contained within it
     *
     * @param actionEvent The delivered {@link Event}
     */
    public void chooseDirectory(ActionEvent actionEvent) {
        File givenDirectory;

        if (userWantsAZipFile())
            givenDirectory = createZipFileChooser().showOpenDialog(directoryButton.getScene().getWindow());
        else
            givenDirectory = createDirectoryChooser(mainWindow.imageToPdfOptions.getIteratorOptions())
                    .showDialog(directoryButton.getScene().getWindow());

        if (givenDirectory != null) {
            buildProgressBar.setProgress(0);
            mainWindow.chosenDirectory = givenDirectory;
            startSetupIteratorThread();
        }

        actionEvent.consume();
    }

    private boolean userWantsAZipFile() {
        return mainWindow.imageToPdfOptions.getIteratorOptions().getZipFiles() &&
                !mainWindow.imageToPdfOptions.getIteratorOptions().getMultipleDirectories();
    }

    private void startSetupIteratorThread() {
        DirectoryIterator directoryIterator = DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator(
                mainWindow.imageToPdfOptions.getIteratorOptions()
        );

        SetupIteratorTask setupIteratorTask = SetupIteratorTask.SetupIteratorTaskFactory.createSetupIteratorTask(
                directoryIterator,
                mainWindow.chosenDirectory,
                () -> {
                    disableInput(true);
                    notifyUser("Preparing files...", BLACK);
                    return Unit.INSTANCE;
                },
                () -> {
                    try {
                        mainWindow.setupIterator(directoryIterator);
                        runLater(() -> setupListView(mainWindow.getDirectoryIterator()));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        notifyUser("An error occurred while trying to prepare the files", RED);
                    } finally {
                        disableInput(false);
                    }
                    return Unit.INSTANCE;
                }
        );

        mainWindow.taskManager.start(setupIteratorTask, true);
    }

    /**
     * Sets up the {@link ListView}
     *
     * @param directoryIterator The {@link DirectoryIterator} for iterating over files
     */
    public void setupListView(DirectoryIterator directoryIterator) {
        startLoadImagesThread(directoryIterator);
    }

    private void startLoadImagesThread(DirectoryIterator directoryIterator) {
        LoadImagesTask loadImagesTask = LoadImagesTask.LoadImagesTaskFactory.createLoadImagesTask(
                mainWindow.imageMap,
                directoryIterator,
                createLoadProgressUpdater(directoryIterator),
                () -> {
                    setupObservableList(directoryIterator);
                    return Unit.INSTANCE;
                }
        );

        mainWindow.taskManager.start(loadImagesTask, true);
    }

    private ProgressUpdater createLoadProgressUpdater(DirectoryIterator directoryIterator) {
        return new LoadProgressUpdater(
                (message, color) -> {
                    notifyUser(message, color);
                    return Unit.INSTANCE;
                },
                directoryIterator.numberOfFiles()
        );
    }

    /**
     * Sets up the {@link ObservableList<File>} and adds it to the {@link ListView<File>}
     *
     * @param directoryIterator The {@link DirectoryIterator} for iterating over directories
     */
    private void setupObservableList(DirectoryIterator directoryIterator) {
        runLater(() -> {
            ObservableList<File> observableFiles = observableArrayList(directoryIterator.getFiles());
            observableFiles.addListener(listChangeListenerFactory.setupListChangeListener(directoryIterator, () -> {
                notifyUser("Files: " + observableFiles.size(), BLACK);
                return Unit.INSTANCE;
            }));

            imageListView.setItems(observableFiles);
            imageListView.setCellFactory(param -> new ImageListCell(
                    mainWindow.imageMap, directoryIterator.getFiles(), observableFiles)
            );

            notifyUser("Files: " + directoryIterator.numberOfFiles(), BLACK);
        });
    }

    /**
     * Builds single or multiple {@link de.nihas101.imageToPdfConverter.pdf.ImagePdf}s
     *
     * @param actionEvent The delivered {@link Event}
     */
    public void buildPdf(ActionEvent actionEvent) {
        if (!valuesSetForBuilding()) return;

        if (userWantsMultiplePDFs()) buildMultiplePdf();
        else buildSinglePdf();

        actionEvent.consume();
    }

    private boolean userWantsMultiplePDFs() {
        return mainWindow.imageToPdfOptions.getIteratorOptions().getMultipleDirectories();
    }

    /**
     * Disables and enables input
     *
     * @param isDisabled True to disable, false to enable input
     */
    public void disableInput(boolean isDisabled) {
        progressIndicator.setVisible(isDisabled);
        imageListView.setDisable(isDisabled);
        buildButton.setDisable(isDisabled);
        directoryButton.setDisable(isDisabled);
        optionsButton.setDisable(isDisabled);
    }

    /**
     * Builds a single {@link de.nihas101.imageToPdfConverter.pdf.ImagePdf}s
     */
    private void buildSinglePdf() {
        File saveFile = mainWindow.openSaveFileChooser(
                mainWindow.chosenDirectory.getParentFile(),
                mainWindow.getDirectoryIterator().getParentDirectory().getName() + ".pdf"
        );

        buildPdf(saveFile);
    }

    /**
     * Builds multiple {@link de.nihas101.imageToPdfConverter.pdf.ImagePdf}s
     */
    private void buildMultiplePdf() {
        File saveFile = mainWindow.openDirectoryChooser(mainWindow.getDirectoryIterator().getParentDirectory());
        buildPdf(saveFile);
    }

    private void buildPdf(File saveFile) {
        if (saveFile != null) {
            mainWindow.setSaveLocation(saveFile);
            startPdfBuilderThread(PdfBuilder.PdfBuilderFactory.createPdfBBuilder(mainWindow.imageToPdfOptions.getIteratorOptions()));
        } else notifyUser("Build cancelled by user", BLACK);
    }

    private void startPdfBuilderThread(PdfBuilder pdfBuilder) {
        BuildPdfTask buildPdfTask = BuildPdfTask.BuildPdfTaskFactory.createBuildPdfTask(
                pdfBuilder,
                mainWindow.getDirectoryIterator(),
                mainWindow.imageToPdfOptions,
                new BuildProgressUpdater(this),
                () -> {
                    disableInput(true);
                    return Unit.INSTANCE;
                },
                () -> {
                    disableInput(false);
                    notifyUser(
                            "Finished building: "
                                    + mainWindow.imageToPdfOptions.getPdfOptions().getSaveLocation().getAbsolutePath(),
                            GREEN
                    );
                    return Unit.INSTANCE;
                }
        );

        mainWindow.taskManager.start(buildPdfTask, true);
    }

    /**
     * @return True if a all necessary values for building the PDF are set, false otherwise
     */
    private boolean valuesSetForBuilding() {
        if (mainWindow.getDirectoryIterator() == null) {
            notifyUser("Please choose a directory", RED);
            return false;
        } else if (mainWindow.getDirectoryIterator().numberOfFiles() == 0) {
            notifyUser("There are no files to turn into a PDF", RED);
            return false;
        }

        return true;
    }

    /**
     * Notifies the user by setting the {@link MainWindowController#notificationText} to the given message
     *
     * @param message The message of the  notification
     * @param color   The color with which the message should be displayed
     */
    public void notifyUser(String message, Paint color) {
        if (message.length() > NOTIFICATION_MAX_STRING_LENGTH)
            message = message.substring(0, NOTIFICATION_MAX_STRING_LENGTH) + "...";

        notificationText.setText(message);
        notificationText.setFill(color);
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
            mainWindow.openContentDisplay(index);
        }
    }

    public void openOptionsMenu(ActionEvent actionEvent) {
        disableInput(true);
        mainWindow.openOptionsMenu();
        disableInput(false);
        actionEvent.consume();
    }
}
