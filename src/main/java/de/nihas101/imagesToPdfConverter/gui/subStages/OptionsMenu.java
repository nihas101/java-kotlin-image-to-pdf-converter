package de.nihas101.imagesToPdfConverter.gui.subStages;

import de.nihas101.imagesToPdfConverter.gui.controller.OptionsMenuController;
import de.nihas101.imagesToPdfConverter.pdf.PdfWriterOptions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public final class OptionsMenu extends Application {
    private final PdfWriterOptions pdfWriterOptions;
    private OptionsMenuController optionsMenuController;

    private OptionsMenu(PdfWriterOptions pdfWriterOptions){
        this.pdfWriterOptions = pdfWriterOptions;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        /* Load root-node */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/options.fxml"));
        GridPane root = loader.load();
        optionsMenuController = loader.getController();
        optionsMenuController.setup(pdfWriterOptions);

        /* Create Scene */
        Scene scene = new Scene(root);

        primaryStage.setTitle("PdfWriterOptions");
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
        primaryStage.setResizable(false);
    }

    public static OptionsMenu createOptionsMenu(PdfWriterOptions pdfWriterOptions) {
        return new OptionsMenu(pdfWriterOptions);
    }

    /**
     * Returns the set pdfWriterOptions by the user
     * The method doesn't return until the displayed dialog is dismissed.
     * @return A new {@link PdfWriterOptions} instance holding the pdfWriterOptions the user
     * has set
     */
    public PdfWriterOptions setOptions() {
        try { start(new Stage()); }
        catch (Exception e) { e.printStackTrace(); }
        return optionsMenuController.getPdfWriterOptions();
    }
}
