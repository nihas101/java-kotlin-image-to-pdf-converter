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

import de.nihas101.imageToPdfConverter.gui.controller.OptionsMenuController;
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static javafx.stage.StageStyle.*;

public final class OptionsMenu extends Application {
    private final ImageToPdfOptions imageToPdfOptions;
    private OptionsMenuController optionsMenuController;
    private Stage primaryStage;

    private OptionsMenu(ImageToPdfOptions imageToPdfOptions) {
        this.imageToPdfOptions = imageToPdfOptions;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.initStyle(UNDECORATED);
        primaryStage.focusedProperty().addListener((ov, onHidden, onShown) -> {
            if(onHidden) stop();
        });

        GridPane root = loadFXML();
        optionsMenuController.setup(imageToPdfOptions);

        Scene scene = new Scene(root);

        setupPrimaryStage(scene);
        primaryStage.showAndWait();
    }

    private GridPane loadFXML() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/options.fxml"));
        GridPane root = loader.load();
        optionsMenuController = loader.getController();
        return root;
    }

    private void setupPrimaryStage(Scene scene) {
        primaryStage.setTitle("JaKoImageToPdf Options");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
    }

    @Override
    public void stop() {
        primaryStage.close();
    }

    public static OptionsMenu createOptionsMenu(ImageToPdfOptions imageToPdfOptions) {
        return new OptionsMenu(imageToPdfOptions);
    }

    /**
     * Returns the set imageToPdfOptions by the user
     * The method doesn't return until the displayed dialog is dismissed.
     *
     * @return A new {@link ImageToPdfOptions} instance holding the imageToPdfOptions the user
     * has set
     */
    public ImageToPdfOptions setOptions() {
        try {
            start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optionsMenuController.getImageToPdfOptions();
    }
}
