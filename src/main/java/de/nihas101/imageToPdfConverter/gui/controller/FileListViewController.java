package de.nihas101.imageToPdfConverter.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;

import java.io.File;

public abstract class FileListViewController {
    @FXML
    ListView<File> imageListView;

    public void setupKeyEvents(Scene scene){
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case DELETE:
                    if (imageListView.getSelectionModel().getSelectedIndex() > -1)
                        imageListView.getItems().remove(imageListView.getSelectionModel().getSelectedIndex());

                default: /* NOP */
            }
        });
    }
}
