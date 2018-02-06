package de.nihas101.imagesToPdfConverter.gui.subStages;

import de.nihas101.imagesToPdfConverter.gui.controller.ImageDisplayController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * An Application for displaying {@link Image}s
 */
public final class ImageDisplay extends Application {
    /**
     * The {@link Image} to display
     */
    private Image image;
    /**
     * The name of the {@link Image}
     */
    private String imageName;

    private ImageDisplay(Image image, String imageName){
        this.image = image;
        this.imageName = imageName;
    }

    /**
     * The factory method for creating {@link ImageDisplay}s
     * @param image The {@link Image} to display
     * @param imageName The name of the {@link Image}
     */
    public static ImageDisplay createImageDisplay(Image image, String imageName){
        return new ImageDisplay(image, imageName);
    }

    public void displayImage(){
        try { start(new Stage()); }
        catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        /* Load root-node */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/imageDisplay.fxml"));
        Pane root = loader.load();
        ImageDisplayController imageDisplayController = loader.getController();
        imageDisplayController.setup(image);

        /* Create Scene */
        Scene scene = new Scene(root);

        primaryStage.setTitle("ImageDisplay - " + imageName);
        primaryStage.setScene(scene);
        primaryStage.setHeight(image.getHeight()*.2);
        primaryStage.setWidth(image.getWidth()*.2);
        primaryStage.showAndWait();
    }
}
