package de.nihas101.imagesToPdfConverter.gui.subStages;

import de.nihas101.imagesToPdfConverter.gui.controller.DirectoryContentDisplayController;
import de.nihas101.imagesToPdfConverter.gui.controller.MainWindowController;
import de.nihas101.imagesToPdfConverter.directoryIterators.DirectoryIterator;
import de.nihas101.imagesToPdfConverter.directoryIterators.ImageFilesIterator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * An {@link Application} for displaying the content of a directory
 */
public final class DirectoryContentDisplay extends Application {
    /**
     * The {@link DirectoryIterator} of which to display the content
     */
    private DirectoryIterator directoryIterator;
    private final int directoryIteratorIndex;
    private final MainWindowController mainWindowController;


    private DirectoryContentDisplay(DirectoryIterator directoryIterator, int directoryIteratorIndex, MainWindowController mainWindowController) {
        this.directoryIterator = directoryIterator;
        this.directoryIteratorIndex = directoryIteratorIndex;
        this.mainWindowController = mainWindowController;
    }

    /**
     * The factory method for creating {@link DirectoryContentDisplay}s
     * @param imageFilesIterator The {@link DirectoryIterator} of which to display the content
     * @return The created {@link ImageFilesIterator} instance
     */
    static DirectoryContentDisplay createDirectoryContentDisplay(DirectoryIterator imageFilesIterator, int directoryIteratorIndex, MainWindowController mainWindowController){
        return new DirectoryContentDisplay(imageFilesIterator, directoryIteratorIndex, mainWindowController);
    }

    public void displayContent(){
        try { start(new Stage()); }
        catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        /* Load root-node */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/directoryContentDisplay.fxml"));
        Pane root = loader.load();
        DirectoryContentDisplayController directoryContentDisplayController = loader.getController();
        directoryContentDisplayController.setup(directoryIterator, directoryIteratorIndex, primaryStage, mainWindowController);

        /* Create Scene */
        Scene scene = new Scene(root);

        primaryStage.setTitle("ContentDisplay - " + directoryIterator.getParentDirectory().getAbsolutePath());
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
        primaryStage.setResizable(false);
    }
}
