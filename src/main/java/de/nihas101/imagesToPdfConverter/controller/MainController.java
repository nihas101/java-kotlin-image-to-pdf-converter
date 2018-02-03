package de.nihas101.imagesToPdfConverter.controller;

import de.nihas101.imagesToPdfConverter.ImageMap;
import de.nihas101.imagesToPdfConverter.Main;
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator;
import de.nihas101.imagesToPdfConverter.fileReader.ImageDirectoriesIterator;
import de.nihas101.imagesToPdfConverter.listCell.ImageListCell;
import de.nihas101.imagesToPdfConverter.pdf.ImageDirectoriesPdfBuilder;
import de.nihas101.imagesToPdfConverter.pdf.ImagePdfBuilder;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.HashMap;

import static de.nihas101.imagesToPdfConverter.Constants.NOTIFICATION_MAX_STRING_LENGTH;
import static de.nihas101.imagesToPdfConverter.contentDisplay.ContentDisplayer.createContentDisplayer;
import static de.nihas101.imagesToPdfConverter.ImageMap.createImageMap;
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
    private ImageMap imageMap;

    private DirectoryChooser directoryChooser;
    private FileChooser saveFileChooser;

    public void setup(Main main){
        this.main = main;
        setupDirectoryChooser();
        setupSaveFileChooser();
        imageMap = createImageMap(new HashMap<>());
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

        actionEvent.consume();
    }

    private void setListView(DirectoryIterator directoryIterator) {
        int nrOfFiles = directoryIterator.nrOfFiles();

        new Thread(() -> {
            imageMap.setupImageMap(directoryIterator.getFiles(),
                    (loadedFiles) ->
                    notifyUser("Loading files... (" + loadedFiles + "/" + nrOfFiles + ")", BLACK)
            );

            Platform.runLater(() -> {
                ObservableList<File> observableFiles = observableArrayList(directoryIterator.getFiles());
                observableFiles.addListener((ListChangeListener<File>) c ->
                        notifyUser("Files: " + observableFiles.size(), BLACK)
                );
                imageListView.setItems(observableFiles);
                imageListView.setCellFactory(param -> new ImageListCell(imageMap, directoryIterator.getFiles(), observableFiles));
                notifyUser("Files: " + nrOfFiles, BLACK);
            });
        }).start();
    }

    public void buildPdf(ActionEvent actionEvent) {
        if(!valuesSet()) return;

        if(multipleDirectoriesCheckBox.isSelected()) buildMultiplePdf();
        else buildSinglePdf();

        actionEvent.consume();
    }

    private void buildSinglePdf(){
        saveFileChooser.setInitialFileName(main.getDirectoryIterator().getParentDirectory().getName() + ".pdf");
        saveFileChooser.setInitialDirectory(chosenDirectory.getParentFile());
        File saveFile = saveFileChooser.showSaveDialog(buildButton.getScene().getWindow());

        if(saveFile != null) {
            /* TODO: Turn this into a Thread, so the progressbar is updated */
            ImagePdfBuilder imagePdfBuilder = ImagePdfBuilder.PdfBuilderFactory.createPdfImageBuilder();
            imagePdfBuilder.build(
                    main.getDirectoryIterator(),
                    saveFile,
                    progress -> buildProgressBar.setProgress(progress)
            );
            notifyUser("Finished building: " + saveFile.getAbsolutePath(), GREEN);
        }
    }

    private void buildMultiplePdf() {
        directoryChooser.setInitialDirectory(main.getDirectoryIterator().getParentDirectory());
        directoryChooser.setTitle("Choose a folder to save the PDFs in");
        File saveFile = directoryChooser.showDialog(buildButton.getScene().getWindow());

        if(saveFile != null) {
            ImageDirectoriesPdfBuilder imageDirectoriesPdfBuilder = ImageDirectoriesPdfBuilder.PdfBuilderFactory.createPdfBuilderFactory();
            imageDirectoriesPdfBuilder.build(
                    main.getDirectoryIterator(),
                    saveFile,
                    progress -> buildProgressBar.setProgress(progress)
            );
            notifyUser("Finished building: " + main.getDirectoryIterator().getParentDirectory().getAbsolutePath(), GREEN);
        }
    }

    private boolean valuesSet() {
        if(main.getDirectoryIterator() == null){
            notifyUser("Please choose a directory", RED);
            return false;
        }else if(main.getDirectoryIterator().nrOfFiles() == 0){
            notifyUser("There are no files to turn into a PDF", RED);
            return false;
        }

        return true;
    }

    private void notifyUser(String message, Color color){
        if(message.length() > NOTIFICATION_MAX_STRING_LENGTH)
            message = message.substring(0, NOTIFICATION_MAX_STRING_LENGTH) + "...";

        notificationText.setText(message);
        notificationText.setFill(color);
    }

    public void displayListCell(MouseEvent mouseEvent) {
        if(imageListView.getItems().size() == 0) return;

        if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2){
            int index = imageListView.getSelectionModel().getSelectedIndex();
            createContentDisplayer(main.getDirectoryIterator()).displayContent(index);
        }
    }
}
