package de.nihas101.imagesToPdfConverter.controller;

import de.nihas101.imagesToPdfConverter.DirectoryContentDisplay;
import de.nihas101.imagesToPdfConverter.ImageDisplay;
import de.nihas101.imagesToPdfConverter.Main;
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator;
import de.nihas101.imagesToPdfConverter.fileReader.ImageDirectoriesIterator;
import de.nihas101.imagesToPdfConverter.listCell.ImageListCell;
import de.nihas101.imagesToPdfConverter.pdf.ImagePdfBuilder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.nihas101.imagesToPdfConverter.Constants.DIRECTORY_IMAGE_PATH;
import static de.nihas101.imagesToPdfConverter.Constants.NOTIFICATION_MAX_STRING_LENGTH;
import static de.nihas101.imagesToPdfConverter.DirectoryContentDisplay.createDirectoryContentDisplay;
import static de.nihas101.imagesToPdfConverter.ImageDisplay.createImageDisplay;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.paint.Color.*;

public class MainController {
    public Button directoryButton;
    public CheckBox multipleDirectoriesCheckBox;
    public Button buildButton;
    public ProgressBar buildProgressBar;
    public ListView<File> imageListView;
    public Text notificationText;
    public TextFlow notificationTextFlow;

    private Main main;
    private File chosenDirectory;
    private Map<String, Image> imageMap;

    private DirectoryChooser directoryChooser;
    private FileChooser saveFileChooser;

    public void setup(Main main){
        this.main = main;
        setupDirectoryChooser();
        setupSaveFileChooser();
        imageMap = new HashMap<>();
    }

    private void setupDirectoryChooser() {
        directoryChooser = new DirectoryChooser();
    }

    private void setupSaveFileChooser() {
        saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Choose a save location for the PDF");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        saveFileChooser.getExtensionFilters().add(extFilter);
    }

    /* TODO: Implement Drag and Drop on Listview to allow for changing of order
    *  TODO: - Change order in directoryIterator.getFiles()... */

    public void chooseDirectory(ActionEvent actionEvent) {
        if(multipleDirectoriesCheckBox.isSelected())
            directoryChooser.setTitle("Choose a directory of directories to turn into a PDF");
        else
            directoryChooser.setTitle("Choose a directory or file to turn into a PDF");

        chosenDirectory = directoryChooser.showDialog(directoryButton.getScene().getWindow());

        if (chosenDirectory != null) {
            if(multipleDirectoriesCheckBox.isSelected())
                main.setupDirectoriesIterator(chosenDirectory);
            else
                main.setupIterator(chosenDirectory);
            setListView(main.getDirectoryIterator());
        }
    }

    private void setListView(DirectoryIterator directoryIterator) {
        int nrOfFiles = directoryIterator.nrOfFiles();

        new Thread(() -> {
            setupImageMap(imageMap, directoryIterator.getFiles());

            Platform.runLater(() -> {
                imageListView.setItems(observableArrayList(directoryIterator.getFiles()));
                imageListView.setCellFactory(param -> new ImageListCell(imageMap));
                notifyUser("Images: " + nrOfFiles, BLACK);
            });
        }).start();
    }

    private void setupImageMap(Map<String, Image> imageMap, List<File> files) {
        imageMap.clear();

        if(files.size() == 0) return;

        for (int index = 0; index < files.size(); index++) {
            if(files.get(index).isDirectory()) {
                File directoryImage = new File(DIRECTORY_IMAGE_PATH);
                putIntoImageMap(imageMap, directoryImage);
            }else{
                notifyUser("Loading images... (" + index + "/" + files.size() + ")", BLACK);
                putIntoImageMap(imageMap, files.get(index));
            }
        }
    }

    private void putIntoImageMap(Map<String, Image> imageMap, File file){
        try {
            String url = file.toURI().toURL().toString();
            /*
             * Scale images for a smaller memory print.
             * Thanks: stackoverflow.com/questions/15088271/javafx-loading-images-and-memory-problems
             */
            imageMap.putIfAbsent(url, new Image(url, 100, 100, true, false));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void buildPdf(ActionEvent actionEvent) {
        if(!valuesSet()) return;

        if(!multipleDirectoriesCheckBox.isSelected()) {
            buildSinglePdf();
        }else{
            buildMultiplePdf();
        }
    }

    private void buildSinglePdf(){
        saveFileChooser.setInitialFileName(main.getDirectoryIterator().getParentDirectory().getName() + ".pdf");
        saveFileChooser.setInitialDirectory(chosenDirectory.getParentFile());
        File saveFile = saveFileChooser.showSaveDialog(buildButton.getScene().getWindow());

        if(saveFile != null) {
            ImagePdfBuilder imagePdfBuilder = ImagePdfBuilder.PdfBuilderFactory.createPdfImageBuilder();
            imagePdfBuilder.build(main.getDirectoryIterator(), saveFile, (progress) -> buildProgressBar.setProgress(progress));
            notifyUser("Finished building: " + saveFile.getAbsolutePath(), GREEN);
        }
    }

    private void buildMultiplePdf() {
        System.out.println("Not implemented yet");
        /* TODO */
    }

    private boolean valuesSet() {
        if(main.getDirectoryIterator() == null){
            notifyUser("Please choose a directory", RED);
            return false;
        }

        return true;
    }

    private void notifyUser(String message, Color color){
        if(message.length() > NOTIFICATION_MAX_STRING_LENGTH) message = message.substring(0, NOTIFICATION_MAX_STRING_LENGTH) + "...";

        notificationText.setText(message);
        notificationText.setFill(color);
    }

    public void displayListCell(MouseEvent mouseEvent) {
        if(imageListView.getItems().size() == 0) return;

        if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2){
            int index =imageListView.getSelectionModel().getSelectedIndex();
            //File file = imageListView.getSelectionModel().getSelectedItem();

            if(multipleDirectoriesCheckBox.isSelected()) displayDirectory(index);
            else displayImage(index);
        }
    }

    private void displayDirectory(int index) {
        DirectoryContentDisplay directoryContentDisplay;

        directoryContentDisplay = createDirectoryContentDisplay(
                ((ImageDirectoriesIterator) main.getDirectoryIterator()).getImageFilesIterator(index)
        );

        try { directoryContentDisplay.start(new Stage()); }
        catch (Exception e) { e.printStackTrace(); }
    }

    private void displayImage(int index) {
        ImageDisplay imageDisplay;
        File file = main.getDirectoryIterator().getFile(index);

        try {
            imageDisplay = createImageDisplay(
                    new Image(String.valueOf(file.toURI().toURL())),
                    file.getAbsolutePath()
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        try { imageDisplay.start(new Stage()); }
        catch (Exception e) { e.printStackTrace(); }
    }
}
