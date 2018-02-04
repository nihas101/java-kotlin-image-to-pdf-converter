package de.nihas101.imagesToPdfConverter.controller;

import de.nihas101.imagesToPdfConverter.ImageMap;
import de.nihas101.imagesToPdfConverter.Main;
import de.nihas101.imagesToPdfConverter.fileReader.DirectoryIterator;
import de.nihas101.imagesToPdfConverter.listCell.ImageListCell;
import de.nihas101.imagesToPdfConverter.pdf.ImageDirectoriesPdfBuilder;
import de.nihas101.imagesToPdfConverter.pdf.ImagePdfBuilder;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
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

import java.io.File;
import java.util.HashMap;

import static de.nihas101.imagesToPdfConverter.Constants.NOTIFICATION_MAX_STRING_LENGTH;
import static de.nihas101.imagesToPdfConverter.contentDisplay.DirectoryIteratorDisplayer.createContentDisplayer;
import static de.nihas101.imagesToPdfConverter.ImageMap.createImageMap;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.paint.Color.*;

public class MainController {
    @FXML
    public Button directoryButton;
    @FXML
    public CheckBox multipleDirectoriesCheckBox;
    public Button buildButton;
    @FXML
    public ProgressBar buildProgressBar;
    @FXML
    public ListView<File> imageListView;
    @FXML
    public Text notificationText;
    @FXML
    public TextFlow notificationTextFlow;
    @FXML
    public Button optionsButton;

    /**
     * The {@link Main} belonging to this Controller
     */
    private Main main;
    /**
     * The directory from which to load more {@link File}s
     */
    private File chosenDirectory;
    /**
     * The {@link ImageMap} holding the loaded {@link Image}s
     */
    private ImageMap imageMap;

    private DirectoryChooser directoryChooser;
    private FileChooser saveFileChooser;

    /**
     * Sets up the {@link MainController}
     * @param main The {@link Main} belonging to this Controller
     */
    public void setup(Main main){
        this.main = main;
        setupDirectoryChooser();
        setupSaveFileChooser();
        imageMap = createImageMap(new HashMap<>());
    }

    /**
     * Sets up the {@link DirectoryChooser} instance
     */
    private void setupDirectoryChooser() {
        directoryChooser = new DirectoryChooser();
    }

    /**
     * Sets up the {@link FileChooser} instance
     */
    private void setupSaveFileChooser() {
        saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Choose a save location for the PDF");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        saveFileChooser.getExtensionFilters().add(extFilter);
    }

    /**
     * Opens a {@link DirectoryChooser} and loads the files contained within it
     * @param actionEvent The delivered {@link Event}
     */
    public void chooseDirectory(ActionEvent actionEvent) {
        if(multipleDirectoriesCheckBox.isSelected())
            directoryChooser.setTitle("Choose a directory of directories to turn into a PDF");
        else
            directoryChooser.setTitle("Choose a directory or file to turn into a PDF");

        chosenDirectory = directoryChooser.showDialog(directoryButton.getScene().getWindow());

        if (chosenDirectory != null) {
            new Thread(() -> {
                setDisableInput(true);
                notifyUser("Loading files...", BLACK);
                if (multipleDirectoriesCheckBox.isSelected())
                    main.setupDirectoriesIterator(chosenDirectory);
                else
                    main.setupIterator(chosenDirectory);
                setupListView(main.getDirectoryIterator());
                setDisableInput(false);
            }).start();
        }

        actionEvent.consume();
    }

    /**
     * Sets up the {@link ListView}
     * @param directoryIterator  The {@link DirectoryIterator} for iterating over files
     */
    private void setupListView(DirectoryIterator directoryIterator) {
        int nrOfFiles = directoryIterator.nrOfFiles();

        new Thread(() -> {
            imageMap.loadImages(directoryIterator.getFiles(),
                    (loadedFiles) ->
                            notifyUser("Loading files... (" + (int)loadedFiles + "/" + nrOfFiles + ")", BLACK));

            Platform.runLater(() -> setupObservableList(directoryIterator));
        }).start();
    }

    /**
     * Sets up the {@link ObservableList<File>} and adds it to the {@link ListView<File>}
     * @param directoryIterator The {@link DirectoryIterator} for iterating over directories
     */
    private void setupObservableList(DirectoryIterator directoryIterator) {
        ObservableList<File> observableFiles = observableArrayList(directoryIterator.getFiles());
        observableFiles.addListener(setupListChangeListener(directoryIterator, observableFiles));
        imageListView.setItems(observableFiles);
        imageListView.setCellFactory(param -> new ImageListCell(imageMap, directoryIterator.getFiles(), observableFiles));
        notifyUser("Files: " + directoryIterator.getFiles(), BLACK);
    }

    /**
     * Sets up a {@link ListChangeListener} that forwards all changes on the {@link ObservableList} to the underlying {@link java.util.List}
     * @param directoryIterator The {@link DirectoryIterator} for iterating over directories
     * @return The created {@link ListChangeListener}
     */
    private ListChangeListener<File> setupListChangeListener(DirectoryIterator directoryIterator , ObservableList<File> observableFiles){
        return change -> {
            while (change.next()) {
                if (change.wasRemoved()) directoryIterator.getFiles().remove(change.getRemoved().get(0));
                if (change.wasAdded()) directoryIterator.getFiles().add(change.getFrom(), change.getAddedSubList().get(0));
                notifyUser("Files: " + observableFiles.size(), BLACK); }
        };
    }

    /**
     * Builds single or multiple {@link de.nihas101.imagesToPdfConverter.pdf.ImagePdf}s
     * @param actionEvent The delivered {@link Event}
     */
    public void buildPdf(ActionEvent actionEvent) {
        if(!valuesSetForBuilding()) return;

        notifyUser("Building PDF...", BLACK);

        setDisableInput(true);
        if(multipleDirectoriesCheckBox.isSelected()) buildMultiplePdf();
        else buildSinglePdf();
        setDisableInput(false);

        actionEvent.consume();
    }

    /**
     * Disables and enables input
     * @param isDisabled True to disable, false to enable input
     */
    private void setDisableInput(boolean isDisabled) {
        imageListView.setDisable(isDisabled);
        buildButton.setDisable(isDisabled);
        directoryButton.setDisable(isDisabled);
        multipleDirectoriesCheckBox.setDisable(isDisabled);
    }

    /**
     * Builds a single {@link de.nihas101.imagesToPdfConverter.pdf.ImagePdf}s
     */
    private void buildSinglePdf(){
        saveFileChooser.setInitialFileName(main.getDirectoryIterator().getParentDirectory().getName() + ".pdf");
        saveFileChooser.setInitialDirectory(chosenDirectory.getParentFile());
        File saveFile = saveFileChooser.showSaveDialog(buildButton.getScene().getWindow());

        if(saveFile != null) {
            new Thread(() -> {
                ImagePdfBuilder.PdfBuilderFactory.createPdfImageBuilder().build(
                        main.getDirectoryIterator(),
                        saveFile,
                        progress -> buildProgressBar.setProgress(progress)
                );
                notifyUser("Finished building: " + saveFile.getAbsolutePath(), GREEN);
            }).start();
        }
    }

    /**
     * Builds multiple {@link de.nihas101.imagesToPdfConverter.pdf.ImagePdf}s
     */
    private void buildMultiplePdf() {
        directoryChooser.setInitialDirectory(main.getDirectoryIterator().getParentDirectory());
        directoryChooser.setTitle("Choose a folder to save the PDFs in");
        File saveFile = directoryChooser.showDialog(buildButton.getScene().getWindow());

        if(saveFile != null) {
            new Thread(() -> {
                ImageDirectoriesPdfBuilder.PdfBuilderFactory.createPdfBuilderFactory().build(
                        main.getDirectoryIterator(),
                        saveFile,
                        progress -> buildProgressBar.setProgress(progress)
                );
                notifyUser("Finished building: " + main.getDirectoryIterator().getParentDirectory().getAbsolutePath(), GREEN);
            }).start();
        }
    }

    /**
     * @return True if a all necessary values for building the PDF are set, false otherwise
     */
    private boolean valuesSetForBuilding() {
        if(main.getDirectoryIterator() == null){
            notifyUser("Please choose a directory", RED);
            return false;
        }else if(main.getDirectoryIterator().nrOfFiles() == 0){
            notifyUser("There are no files to turn into a PDF", RED);
            return false;
        }

        return true;
    }

    /**
     * Notifies the user by setting the {@link MainController#notificationText} to the given message
     * @param message The message of the  notification
     * @param color The color with which the message should be displayed
     */
    private void notifyUser(String message, Color color){
        if(message.length() > NOTIFICATION_MAX_STRING_LENGTH)
            message = message.substring(0, NOTIFICATION_MAX_STRING_LENGTH) + "...";

        notificationText.setText(message);
        notificationText.setFill(color);
    }

    /**
     * Displays the content of the {@link DirectoryIterator}
     * @param mouseEvent The delivered {@link Event}
     */
    public void displayListCell(MouseEvent mouseEvent) {
        if(imageListView.getItems().size() == 0) return;

        if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2){
            int index = imageListView.getSelectionModel().getSelectedIndex();
            createContentDisplayer(main.getDirectoryIterator()).displayContent(index);
        }
    }

    public void openOptionsMenu(ActionEvent actionEvent) {
        /* TODO */
    }
}
