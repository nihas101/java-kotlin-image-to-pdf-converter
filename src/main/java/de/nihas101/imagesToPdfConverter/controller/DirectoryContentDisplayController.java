package de.nihas101.imagesToPdfConverter.controller;

import de.nihas101.imagesToPdfConverter.ImageMap;
import de.nihas101.imagesToPdfConverter.subStages.DirectoryContentDisplay;
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator;
import de.nihas101.imagesToPdfConverter.listCell.ImageListCell;
import de.nihas101.imagesToPdfConverter.pdf.ImagePdfBuilder;
import javafx.application.Application;
import javafx.application.Platform;
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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static de.nihas101.imagesToPdfConverter.ImageMap.createImageMap;
import static de.nihas101.imagesToPdfConverter.subStages.DirectoryIteratorDisplayer.createContentDisplayer;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.paint.Color.GREEN;

/**
 * The Controller of the {@link DirectoryContentDisplay} {@link Application}
 */
public class DirectoryContentDisplayController {
    /**
     * The {@link ListView} for displaying the content of the directory
     */
    @FXML
    public ListView<File> imageListView;
    @FXML
    public Button buildButton;
    /**
     * The {@link DirectoryIterator} for iterating over directories
     */
    private DirectoryIterator directoryIterator;
    private Stage directoryContentDisplayStage;
    private MainController mainController;
    private int directoryIteratorIndex;

    /**
     * Sets up the {@link DirectoryContentDisplayController}
     * @param directoryIterator The {@link DirectoryIterator} for iterating over directories
     */
    public void setup(DirectoryIterator directoryIterator, int directoryIteratorIndex, Stage directoryContentDisplayStage, MainController mainController){
        this.directoryIterator = directoryIterator;
        this.directoryIteratorIndex = directoryIteratorIndex;
        this.directoryContentDisplayStage = directoryContentDisplayStage;
        this.mainController = mainController;

        new Thread(() -> {
            ImageMap imageMap = createImageMap(new HashMap<>());
            imageMap.loadImages(directoryIterator.getFiles());

            Platform.runLater(() -> setupObservableList(directoryIterator, imageMap));
        }).start();
    }

    /**
     * Sets up the {@link ObservableList<File>} and adds it to the {@link ListView<File>}
     * @param directoryIterator The {@link DirectoryIterator} for iterating over directories
     * @param imageMap The {@link Map} holding the images to be drawn in the list
     */
    private void setupObservableList(DirectoryIterator directoryIterator, ImageMap imageMap) {
        ObservableList<File> observableFiles = observableArrayList(directoryIterator.getFiles());
        observableFiles.addListener(setupListChangeListener(directoryIterator));
        imageListView.setItems(observableFiles);
        imageListView.setCellFactory(param -> new ImageListCell(imageMap, directoryIterator.getFiles(), observableFiles));
    }

    /**
     * Sets up a {@link ListChangeListener} that forwards all changes on the {@link ObservableList} to the underlying {@link java.util.List}
     * @param directoryIterator The {@link DirectoryIterator} for iterating over directories
     * @return The created {@link ListChangeListener}
     */
    private ListChangeListener<File> setupListChangeListener(DirectoryIterator directoryIterator) {
        return change -> {
            while (change.next()) {
                if (change.wasRemoved()) directoryIterator.getFiles().remove(change.getRemoved().get(0));
                if (change.wasAdded())
                    directoryIterator.getFiles().add(change.getFrom(), change.getAddedSubList().get(0));
            }
        };
    }

    /**
     * Displays the content of the {@link DirectoryIterator}
     * @param mouseEvent The delivered {@link Event}
     */
    public void displayListCell(MouseEvent mouseEvent) {
        if(imageListView.getItems().size() == 0) return;

        if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2){
            int index = imageListView.getSelectionModel().getSelectedIndex();
            createContentDisplayer(directoryIterator).displayContent(index, mainController);
        }

        mouseEvent.consume();
    }

    public void buildPDF(ActionEvent actionEvent) {
        mainController.saveFileChooser.setInitialFileName(directoryIterator.getParentDirectory().getName() + ".pdf");
        mainController.saveFileChooser.setInitialDirectory(directoryIterator.getParentDirectory());
        File saveFile = mainController.saveFileChooser.showSaveDialog(buildButton.getScene().getWindow());

        if(saveFile != null) {
            new Thread(() -> {
                ImagePdfBuilder.PdfBuilderFactory.createPdfImageBuilder().build(
                        directoryIterator,
                        saveFile,
                        progress -> mainController.buildProgressBar.setProgress(progress)
                );
                mainController.notifyUser("Finished building: " + saveFile.getAbsolutePath(), GREEN);
            }).start();
        }

        actionEvent.consume();

        mainController.imageListView.getItems().remove(directoryIteratorIndex);
        directoryContentDisplayStage.close();
    }
}
