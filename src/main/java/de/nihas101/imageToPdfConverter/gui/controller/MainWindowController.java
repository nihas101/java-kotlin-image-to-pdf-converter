package de.nihas101.imageToPdfConverter.gui.controller;

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator;
import de.nihas101.imageToPdfConverter.gui.MainWindow;
import de.nihas101.imageToPdfConverter.listCell.ImageListCell;
import de.nihas101.imageToPdfConverter.pdf.PdfWriterOptions;
import de.nihas101.imageToPdfConverter.pdf.builders.ImageDirectoriesPdfBuilder;
import de.nihas101.imageToPdfConverter.pdf.builders.ImagePdfBuilder;
import de.nihas101.imageToPdfConverter.util.ImageMap;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.IntStream;

import static com.itextpdf.kernel.pdf.CompressionConstants.DEFAULT_COMPRESSION;
import static com.itextpdf.kernel.pdf.PdfVersion.PDF_1_7;
import static de.nihas101.imageToPdfConverter.gui.subStages.DirectoryIteratorDisplayer.createContentDisplayer;
import static de.nihas101.imageToPdfConverter.gui.subStages.OptionsMenu.createOptionsMenu;
import static de.nihas101.imageToPdfConverter.util.Constants.NOTIFICATION_MAX_STRING_LENGTH;
import static de.nihas101.imageToPdfConverter.util.ImageMap.createImageMap;
import static javafx.application.Platform.runLater;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.paint.Color.*;

public class MainWindowController extends FileListViewController {
    @FXML
    public Button directoryButton;
    @FXML
    public Button buildButton;
    @FXML
    public ProgressBar buildProgressBar;
    @FXML
    public Text notificationText;
    @FXML
    public TextFlow notificationTextFlow;
    @FXML
    public Button optionsButton;

    /**
     * The {@link MainWindow} belonging to this Controller
     */
    private MainWindow mainWindow;
    /**
     * The directory from which to load more {@link File}s
     */
    private File chosenDirectory = new File("");
    /**
     * The {@link ImageMap} holding the loaded {@link Image}s
     */
    private ImageMap imageMap;

    /**
     * The selected {@link PdfWriterOptions} for building the PDF(s)
     */
    public PdfWriterOptions pdfWriterOptions;

    private DirectoryChooser directoryChooser;

    FileChooser saveFileChooser;

    /**
     * Sets up the {@link MainWindowController}
     *
     * @param mainWindow The {@link MainWindow} belonging to this Controller
     */
    public void setup(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setupDirectoryChooser();
        setupSaveFileChooser();
        imageMap = createImageMap();
        pdfWriterOptions = PdfWriterOptions.OptionsFactory.createOptions(false, DEFAULT_COMPRESSION, PDF_1_7);

        imageListView.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != imageListView && dragEvent.getDragboard().hasFiles()) {
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            dragEvent.consume();
        });

        imageListView.setOnDragDropped(dragEvent -> {
            if (dragEvent.getDragboard().hasFiles()) {
                List<File> files = dragEvent.getDragboard().getFiles();
                setupIteratorFromDragAndDrop(files);
                dragEvent.setDropCompleted(true);
            }

            dragEvent.consume();
        });
    }

    private void setupIteratorFromDragAndDrop(List<File> files) {
        chosenDirectory = files.get(0);

        new Thread(() -> {
            setupIterator();
            if (files.size() > 1) {
                runLater(() -> {
                    buildProgressBar.setProgress(0);
                    notifyUser("Loading files...", BLACK);
                    disableInput(true);
                    mainWindow.getDirectoryIterator().addAll(files.subList(1, files.size()));
                    imageListView.getItems().addAll(files.subList(1, files.size()));
                    disableInput(false);
                });
            }
        }).start();
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
     *
     * @param actionEvent The delivered {@link Event}
     */
    public void chooseDirectory(ActionEvent actionEvent) {
        if (pdfWriterOptions.getMultipleDirectories())
            directoryChooser.setTitle("Choose a directory of directories to turn into a PDF");
        else
            directoryChooser.setTitle("Choose a directory or sourceFile to turn into a PDF");

        File givenDirectory = directoryChooser.showDialog(directoryButton.getScene().getWindow());

        if (givenDirectory != null) {
            chosenDirectory = givenDirectory;
            new Thread(this::setupIterator).start();
            buildProgressBar.setProgress(0);
        }

        actionEvent.consume();
    }

    private void setupIterator() {
        disableInput(true);
        notifyUser("Loading files...", BLACK);
        mainWindow.setupIterator(chosenDirectory, pdfWriterOptions.getMultipleDirectories());
        runLater(() -> setupListView(mainWindow.getDirectoryIterator()));
        disableInput(false);
    }

    /**
     * Sets up the {@link ListView}
     *
     * @param directoryIterator The {@link DirectoryIterator} for iterating over files
     */
    private void setupListView(DirectoryIterator directoryIterator) {
        int nrOfFiles = directoryIterator.nrOfFiles();

        new Thread(() -> {
            imageMap.loadImages(directoryIterator.getFiles(),
                    (loadedFiles) ->
                            notifyUser("Loading files... (" + (int) loadedFiles + "/" + nrOfFiles + ")", BLACK));

            setupObservableList(directoryIterator);
        }).start();
    }

    /**
     * Sets up the {@link ObservableList<File>} and adds it to the {@link ListView<File>}
     *
     * @param directoryIterator The {@link DirectoryIterator} for iterating over directories
     */
    private void setupObservableList(DirectoryIterator directoryIterator) {
        runLater(() -> {
            ObservableList<File> observableFiles = observableArrayList(directoryIterator.getFiles());
            observableFiles.addListener(setupListChangeListener(directoryIterator, observableFiles));
            imageListView.setItems(observableFiles);
            imageListView.setCellFactory(param -> new ImageListCell(imageMap, directoryIterator.getFiles(), observableFiles));
            notifyUser("Files: " + directoryIterator.nrOfFiles(), BLACK);
        });
    }

    /**
     * Sets up a {@link ListChangeListener} that forwards all changes on the {@link ObservableList} to the underlying {@link List}
     *
     * @param directoryIterator The {@link DirectoryIterator} for iterating over directories
     * @return The created {@link ListChangeListener}
     */
    private ListChangeListener<File> setupListChangeListener(DirectoryIterator directoryIterator, ObservableList<File> observableFiles) {
        return change -> {
            while (change.next()) handleChange(directoryIterator, change);
            notifyUser("Files: " + observableFiles.size(), BLACK);
        };
    }

    private void handleChange(DirectoryIterator directoryIterator, Change<? extends File> change) {
        if (change.wasRemoved()) removeChange(directoryIterator, change);

        if (change.wasAdded() && change.getAddedSize() == 1) addAtChangePosition(directoryIterator, change);
        else if (change.wasAdded()) addChange(directoryIterator, change);
    }

    private void addChange(DirectoryIterator directoryIterator, Change<? extends File> change) {
        IntStream.range(0, change.getAddedSubList().size()).forEach(index -> {
            if (!directoryIterator.add(change.getAddedSubList().get(index)))
                imageListView.getItems().remove(change.getAddedSubList().get(index));
        });
    }

    private void removeChange(DirectoryIterator directoryIterator, Change<? extends File> change) {
        File removedFile = change.getRemoved().get(0);
        directoryIterator.remove(removedFile);
        if (imageMap.contains(change.getRemoved().get(0))) {
            try {
                imageMap.remove(removedFile.toURI().toURL().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void addAtChangePosition(DirectoryIterator directoryIterator, Change<? extends File> change) {
        if (!directoryIterator.add(change.getFrom(), change.getAddedSubList().get(0)))
            imageListView.getItems().remove(change.getAddedSubList().get(0));
    }

    /**
     * Builds single or multiple {@link de.nihas101.imageToPdfConverter.pdf.ImagePdf}s
     *
     * @param actionEvent The delivered {@link Event}
     */
    public void buildPdf(ActionEvent actionEvent) {
        if (!valuesSetForBuilding()) return;

        notifyUser("Building PDF...", BLACK);

        if (pdfWriterOptions.getMultipleDirectories()) buildMultiplePdf();
        else buildSinglePdf();

        actionEvent.consume();
    }

    /**
     * Disables and enables input
     *
     * @param isDisabled True to disable, false to enable input
     */
    private void disableInput(boolean isDisabled) {
        imageListView.setDisable(isDisabled);
        buildButton.setDisable(isDisabled);
        directoryButton.setDisable(isDisabled);
        optionsButton.setDisable(isDisabled);
    }

    /**
     * Builds a single {@link de.nihas101.imageToPdfConverter.pdf.ImagePdf}s
     */
    private void buildSinglePdf() {
        saveFileChooser.setInitialFileName(mainWindow.getDirectoryIterator().getParentDirectory().getName() + ".pdf");
        saveFileChooser.setInitialDirectory(chosenDirectory.getParentFile());
        File saveFile = saveFileChooser.showSaveDialog(buildButton.getScene().getWindow());

        if (saveFile != null) {
            new Thread(() -> {
                disableInput(true);
                ImagePdfBuilder.ImagePdfBuilderFactory.createImagePdfBuilder().build(
                        mainWindow.getDirectoryIterator(),
                        saveFile,
                        pdfWriterOptions,
                        progress -> {
                            buildProgressBar.setProgress(progress);
                            System.gc();
                        }
                );
                notifyUser("Finished building: " + saveFile.getAbsolutePath(), GREEN);
                disableInput(false);
            }).start();
        } else notifyUser("Build cancelled by user", BLACK);
    }

    /**
     * Builds multiple {@link de.nihas101.imageToPdfConverter.pdf.ImagePdf}s
     */
    private void buildMultiplePdf() {
        directoryChooser.setInitialDirectory(mainWindow.getDirectoryIterator().getParentDirectory());
        directoryChooser.setTitle("Choose a folder to save the PDFs in");
        File saveFile = directoryChooser.showDialog(buildButton.getScene().getWindow());

        if (saveFile != null) {
            disableInput(true);
            new Thread(() -> {
                ImageDirectoriesPdfBuilder.PdfBuilderFactory.createImageDirectoriesPdfBuilder().build(
                        mainWindow.getDirectoryIterator(),
                        saveFile,
                        pdfWriterOptions,
                        progress -> buildProgressBar.setProgress(progress)
                );
                notifyUser("Finished building: " + mainWindow.getDirectoryIterator().getParentDirectory().getAbsolutePath(), GREEN);
                disableInput(false);
            }).start();
        } else notifyUser("Build cancelled by user", BLACK);
    }

    /**
     * @return True if a all necessary values for building the PDF are set, false otherwise
     */
    private boolean valuesSetForBuilding() {
        if (mainWindow.getDirectoryIterator() == null) {
            notifyUser("Please choose a directory", RED);
            return false;
        } else if (mainWindow.getDirectoryIterator().nrOfFiles() == 0) {
            notifyUser("There are no files to turn into a PDF", RED);
            return false;
        }

        return true;
    }

    /**
     * Notifies the user by setting the {@link MainWindowController#notificationText} to the given message
     *
     * @param message The message of the  notification
     * @param color   The color with which the message should be displayed
     */
    public void notifyUser(String message, Color color) {
        if (message.length() > NOTIFICATION_MAX_STRING_LENGTH)
            message = message.substring(0, NOTIFICATION_MAX_STRING_LENGTH) + "...";

        notificationText.setText(message);
        notificationText.setFill(color);
    }

    /**
     * Displays the content of the {@link DirectoryIterator}
     *
     * @param mouseEvent The delivered {@link Event}
     */
    public void displayListCell(MouseEvent mouseEvent) {
        if (imageListView.getItems().size() == 0) return;

        if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
            disableInput(true);
            int index = imageListView.getSelectionModel().getSelectedIndex();
            try {
                createContentDisplayer(mainWindow.getDirectoryIterator()).displayContent(index, this);
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                disableInput(false);
            }
        }
    }

    public void openOptionsMenu(ActionEvent actionEvent) {
        try {
            pdfWriterOptions = createOptionsMenu(pdfWriterOptions).setOptions();
        } catch (Exception e) {
            e.printStackTrace();
        }
        actionEvent.consume();
    }
}
