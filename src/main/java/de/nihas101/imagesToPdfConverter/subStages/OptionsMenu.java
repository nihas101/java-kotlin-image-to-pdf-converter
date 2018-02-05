package de.nihas101.imagesToPdfConverter.subStages;

import de.nihas101.imagesToPdfConverter.controller.OptionsMenuController;
import de.nihas101.imagesToPdfConverter.pdf.Options;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class OptionsMenu extends Application {
    private final Options options;
    private OptionsMenuController optionsMenuController;

    private OptionsMenu(Options options){
        this.options = options;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        /* Load root-node */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/options.fxml"));
        GridPane root = loader.load();
        optionsMenuController = loader.getController();
        optionsMenuController.setup(options);

        /* Create Scene */
        Scene scene = new Scene(root);

        primaryStage.setTitle("Options");
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
        primaryStage.setResizable(false);
    }

    public static OptionsMenu createOptionsMenu(Options options) {
        return new OptionsMenu(options);
    }

    /**
     * Returns the set options by the user
     * The method doesn't return until the displayed dialog is dismissed.
     * @return A new {@link Options} instance holding the options the user
     * has set
     */
    public Options setOptions() {
        try { start(new Stage()); }
        catch (Exception e) { e.printStackTrace(); }
        return optionsMenuController.getOptions();
    }
}
