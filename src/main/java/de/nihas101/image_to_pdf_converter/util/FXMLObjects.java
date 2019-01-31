package de.nihas101.image_to_pdf_converter.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class FXMLObjects {
    private Pane root;
    private static JaKoLogger logger = JaKoLogger.createLogger(FXMLObjects.class);
    private Object controller;

    private FXMLObjects() {
    }

    public static FXMLObjects loadFXMLObjects(String resourceName) {
        FXMLObjects fxmlObjects = new FXMLObjects();
        fxmlObjects.loadFXML(resourceName);
        return fxmlObjects;
    }

    public void loadFXML(String resourceName) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceName));
        try {
            root = loader.load();
        } catch (IOException exception) {
            logger.error("{}", exception);
            System.exit(1);
        }
        logger.info("{}", "FXML loaded");
        controller = loader.getController();
    }

    public Pane getRoot() {
        return root;
    }

    public Object getController() {
        return controller;
    }
}
