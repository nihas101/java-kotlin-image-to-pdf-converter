package de.nihas101.imageToPdfConverter.gui.subStages;

import de.nihas101.imageToPdfConverter.gui.controller.OptionsMenuController;
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public final class OptionsMenu extends Application {
    private final ImageToPdfOptions imageToPdfOptions;
    private OptionsMenuController optionsMenuController;

    private OptionsMenu(ImageToPdfOptions imageToPdfOptions) {
        this.imageToPdfOptions = imageToPdfOptions;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        /* Load root-node */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/options.fxml"));
        GridPane root = loader.load();
        optionsMenuController = loader.getController();
        optionsMenuController.setup(imageToPdfOptions);

        /* Create Scene */
        Scene scene = new Scene(root);

        primaryStage.setTitle("ImageToPdfOptions");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.showAndWait();
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
