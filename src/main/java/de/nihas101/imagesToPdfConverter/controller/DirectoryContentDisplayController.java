package de.nihas101.imagesToPdfConverter.controller;

import de.nihas101.imagesToPdfConverter.ImageMap;
import de.nihas101.imagesToPdfConverter.fileReader.ImageFilesIterator;
import de.nihas101.imagesToPdfConverter.listCell.ImageListCell;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.HashMap;

import static de.nihas101.imagesToPdfConverter.ContentDisplayer.createContentDisplayer;
import static javafx.collections.FXCollections.observableArrayList;

public class DirectoryContentDisplayController {
    public ListView<File> imageListView;
    private ImageFilesIterator imageFilesIterator;


    public void setup(ImageFilesIterator imageFilesIterator){
        this.imageFilesIterator = imageFilesIterator;
        new Thread(() -> {
            ImageMap imageMap = ImageMap.createImageMap(new HashMap<>());
            imageMap.setupImageMap(imageFilesIterator.getFiles());

            Platform.runLater(() -> {
                imageListView.setItems(observableArrayList(imageFilesIterator.getFiles()));
                imageListView.setCellFactory(param -> new ImageListCell(imageMap));
            });
        }).start();
    }


    public void displayListCell(MouseEvent mouseEvent) {
        if(imageListView.getItems().size() == 0) return;

        if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2){
            int index = imageListView.getSelectionModel().getSelectedIndex();
            createContentDisplayer(imageFilesIterator).displayContent(index);
        }
    }
}
