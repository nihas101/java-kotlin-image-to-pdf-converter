package de.nihas101.imageToPdfConverter.util;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static de.nihas101.imageToPdfConverter.util.FXMLObjects.loadFXMLObjects;

public class FXMLObjectsTest extends ApplicationTest {

    @Test
    public void loadMain(){
        testLoad("fxml/main.fxml");
    }

    @Test
    public void loadOptions(){
        testLoad("fxml/options.fxml");
    }

    @Test
    public void loadImageDisplay(){
        testLoad("fxml/imageDisplay.fxml");
    }

    @Test
    public void loadDirectoryContentDisplay(){
        testLoad("fxml/directoryContentDisplay.fxml");
    }

    private void testLoad(String resourceName){
        FXMLObjects fxmlObjects = loadFXMLObjects(resourceName);

        assert(null != fxmlObjects.getRoot());
        assert(null != fxmlObjects.getController());
    }
}