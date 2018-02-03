package de.nihas101.imagesToPdfConverter.contentDisplay;

import de.nihas101.imagesToPdfConverter.controller.ImageDisplayController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ImageDisplay extends Application {
    private Image image;
    private String imageLocation;

    private ImageDisplay(Image image, String imageLocation){
        this.image = image;
        this.imageLocation = imageLocation;
    }

    static ImageDisplay createImageDisplay(Image image, String imageLocation){
        return new ImageDisplay(image, imageLocation);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        /* Load root-node */
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/imageDisplay.fxml"));
        Pane root = loader.load();
        ImageDisplayController imageDisplayController = loader.getController();
        imageDisplayController.setup(image);

        /* Create Scene */
        Scene scene = new Scene(root);

        primaryStage.setTitle("ImageDisplay - " + imageLocation);
        primaryStage.setScene(scene);
        primaryStage.setHeight(image.getHeight()*.2);
        primaryStage.setWidth(image.getWidth()*.2);
        primaryStage.show();
    }
}
