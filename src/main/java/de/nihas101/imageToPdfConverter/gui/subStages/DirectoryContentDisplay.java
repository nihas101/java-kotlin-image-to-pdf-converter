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

package de.nihas101.imageToPdfConverter.gui.subStages;

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator;
import de.nihas101.imageToPdfConverter.directoryIterators.imageIterators.ImageFilesIterator;
import de.nihas101.imageToPdfConverter.gui.controller.DirectoryContentDisplayController;
import de.nihas101.imageToPdfConverter.gui.controller.MainWindowController;
import de.nihas101.imageToPdfConverter.util.FXMLObjects;
import de.nihas101.imageToPdfConverter.util.JaKoLogger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static de.nihas101.imageToPdfConverter.util.JaKoLogger.createLogger;

/**
 * An {@link Application} for displaying the content of a directory
 */
public final class DirectoryContentDisplay extends Application {
    /**
     * The {@link DirectoryIterator} of which to display the content
     */
    private final int directoryIteratorIndex;
    private final MainWindowController mainWindowController;
    private DirectoryIterator directoryIterator;
    public DirectoryContentDisplayController directoryContentDisplayController;

    private static JaKoLogger logger = createLogger(DirectoryContentDisplay.class);


    private DirectoryContentDisplay(DirectoryIterator directoryIterator, int directoryIteratorIndex, MainWindowController mainWindowController) {
        this.directoryIterator = directoryIterator;
        this.directoryIteratorIndex = directoryIteratorIndex;
        this.mainWindowController = mainWindowController;
    }

    /**
     * The factory method for creating {@link DirectoryContentDisplay}s
     *
     * @param imageFilesIterator The {@link DirectoryIterator} of which to display the content
     * @return The created {@link ImageFilesIterator} instance
     */
    public static DirectoryContentDisplay createDirectoryContentDisplay(DirectoryIterator imageFilesIterator, int directoryIteratorIndex, MainWindowController mainWindowController) {
        return new DirectoryContentDisplay(imageFilesIterator, directoryIteratorIndex, mainWindowController);
    }

    public void displayContent() {
        try {
            start(new Stage());
        } catch (Exception exception) {
            logger.error("{}", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) {
        FXMLObjects fxmlObjects = FXMLObjects.loadFXMLObjects("fxml/directoryContentDisplay.fxml");
        Pane root = fxmlObjects.getRoot();
        directoryContentDisplayController = (DirectoryContentDisplayController) fxmlObjects.getController();

        directoryContentDisplayController.setup(directoryIterator, directoryIteratorIndex, primaryStage, mainWindowController);

        Scene scene = new Scene(root);
        directoryContentDisplayController.setupKeyEvents(scene);

        setupPrimaryStage(primaryStage, scene);
        primaryStage.showAndWait();
    }

    private void setupPrimaryStage(Stage primaryStage, Scene scene) {
        primaryStage.setTitle("ContentDisplay - " + directoryIterator.getParentDirectory().getAbsolutePath());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
    }
}
