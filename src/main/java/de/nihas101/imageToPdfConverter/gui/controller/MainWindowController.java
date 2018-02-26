package de.nihas101.imageToPdfConverter.gui.controller;

import de.nihas101.imageToPdfConverter.directoryIterators.DirectoryIterator;
import de.nihas101.imageToPdfConverter.gui.MainWindow;
import de.nihas101.imageToPdfConverter.listCell.ImageListCell;
import de.nihas101.imageToPdfConverter.pdf.builders.ImageDirectoriesPdfBuilder;
import de.nihas101.imageToPdfConverter.pdf.builders.ImagePdfBuilder;
import de.nihas101.imageToPdfConverter.pdf.builders.PdfBuilder;
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions;
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.IteratorOptions;
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.PdfOptions;
import de.nihas101.imageToPdfConverter.tasks.BuildPdfTask;
import de.nihas101.imageToPdfConverter.tasks.LoadImagesTask;
import de.nihas101.imageToPdfConverter.tasks.SetupIteratorFromDragAndDropTask;
import de.nihas101.imageToPdfConverter.tasks.SetupIteratorTask;
import de.nihas101.imageToPdfConverter.util.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import kotlin.Unit;

import java.io.File;
import java.util.List;

import static de.nihas101.imageToPdfConverter.gui.subStages.DirectoryIteratorDisplayer.createContentDisplayer;
import static de.nihas101.imageToPdfConverter.gui.subStages.OptionsMenu.createOptionsMenu;
import static de.nihas101.imageToPdfConverter.util.Constants.NOTIFICATION_MAX_STRING_LENGTH;
import static de.nihas101.imageToPdfConverter.util.FileChooserFactoryKt.*;
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
    public ProgressIndicator progressIndicator;

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
     * The selected {@link ImageToPdfOptions} for building the PDF(s)
     */
    public ImageToPdfOptions imageToPdfOptions;

    private ListChangeListenerFactory listChangeListenerFactory;

    public FileChooser saveFileChooser;

    /* TODO: Hold onto threads to cancel them if something goes wrong / Use ExecutorService */

    /**
     * Sets up the {@link MainWindowController}
     *
     * @param mainWindow The {@link MainWindow} belonging to this Controller
     */
    public void setup(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        saveFileChooser = createSaveFileChooser();
        imageMap = createImageMap();

        imageToPdfOptions = ImageToPdfOptions.OptionsFactory.createOptions(
                new IteratorOptions(),
                new PdfOptions()
        );

        listChangeListenerFactory = ListChangeListenerFactory.ListChangeListenerFactoryFactory
                .createListChangeListenerFactory(imageListView, imageMap);

        setOnDrag();
    }

    private void setOnDrag() {
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

        Thread thread = SetupIteratorFromDragAndDropTask.SetupIteratorFromDragAndDropTaskFactory.createSetupIteratorThread(
                this,
                () -> {
                    if (files.size() > 1) {
                        runLater(() -> {
                            buildProgressBar.setProgress(0.0);
                            notifyUser("Preparing files...", BLACK);
                            disableInput(true);
                            mainWindow.getDirectoryIterator().addAll(files.subList(1, files.size()));
                            imageListView.getItems().addAll(files.subList(1, files.size()));
                            disableInput(false);
                        });
                    }
                    return Unit.INSTANCE;
                });

        thread.start();
    }

    /**
     * Opens a {@link DirectoryChooser} and loads the files contained within it
     *
     * @param actionEvent The delivered {@link Event}
     */
    public void chooseDirectory(ActionEvent actionEvent) {
        File givenDirectory;

        if (imageToPdfOptions.getIteratorOptions().getZipFiles() && !imageToPdfOptions.getIteratorOptions().getMultipleDirectories())
            givenDirectory = createZipFileChooser().showOpenDialog(directoryButton.getScene().getWindow());
        else
            givenDirectory = createDirectoryChooser(imageToPdfOptions.getIteratorOptions()).showDialog(directoryButton.getScene().getWindow());

        if (givenDirectory != null) {
            buildProgressBar.setProgress(0);
            chosenDirectory = givenDirectory;
            SetupIteratorTask.SetupIteratorTaskFactory.createSetupIteratorThread(this).start();
        }

        actionEvent.consume();
    }

    public void setupIterator() {
        disableInput(true);
        notifyUser("Preparing files...", BLACK);
        try {
            mainWindow.setupIterator(chosenDirectory, imageToPdfOptions);
            runLater(() -> setupListView(mainWindow.getDirectoryIterator()));
        } catch (Exception exception) {
            exception.printStackTrace();
            notifyUser("An error occurred while trying to prepare the files", RED);
        } finally {
            disableInput(false);
        }
    }

    /**
     * Sets up the {@link ListView}
     *
     * @param directoryIterator The {@link DirectoryIterator} for iterating over files
     */
    private void setupListView(DirectoryIterator directoryIterator) {
        Thread loadImagesThread = createLoadImagesThread(directoryIterator);
        loadImagesThread.start();
    }

    private Thread createLoadImagesThread(DirectoryIterator directoryIterator) {
        return LoadImagesTask.LoadImagesThreadFactory.createLoadImagesThread(
                directoryIterator,
                imageMap,
                createLoadProgressUpdater(directoryIterator),
                () -> {
                    setupObservableList(directoryIterator);
                    return Unit.INSTANCE;
                });
    }

    private ProgressUpdater createLoadProgressUpdater(DirectoryIterator directoryIterator) {
        return new LoadProgressUpdater(
                (message, color) -> {
                    notifyUser(message, color);
                    return Unit.INSTANCE;
                },
                directoryIterator.numberOfFiles()
        );
    }

    /**
     * Sets up the {@link ObservableList<File>} and adds it to the {@link ListView<File>}
     *
     * @param directoryIterator The {@link DirectoryIterator} for iterating over directories
     */
    private void setupObservableList(DirectoryIterator directoryIterator) {
        runLater(() -> {
            ObservableList<File> observableFiles = observableArrayList(directoryIterator.getFiles());
            observableFiles.addListener(listChangeListenerFactory.setupListChangeListener(directoryIterator, () -> {
                notifyUser("Files: " + observableFiles.size(), BLACK);
                return Unit.INSTANCE;
            }));
            imageListView.setItems(observableFiles);
            imageListView.setCellFactory(param -> new ImageListCell(imageMap, directoryIterator.getFiles(), observableFiles));
            notifyUser("Files: " + directoryIterator.numberOfFiles(), BLACK);
        });
    }

    /**
     * Builds single or multiple {@link de.nihas101.imageToPdfConverter.pdf.ImagePdf}s
     *
     * @param actionEvent The delivered {@link Event}
     */
    public void buildPdf(ActionEvent actionEvent) {
        if (!valuesSetForBuilding()) return;

        if (imageToPdfOptions.getIteratorOptions().getMultipleDirectories()) buildMultiplePdf();
        else buildSinglePdf();

        actionEvent.consume();
    }

    /**
     * Disables and enables input
     *
     * @param isDisabled True to disable, false to enable input
     */
    public void disableInput(boolean isDisabled) {
        progressIndicator.setVisible(isDisabled);
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
            setSaveLocation(saveFile);
            createPdfBuilderThread(ImagePdfBuilder.ImagePdfBuilderFactory.createImagePdfBuilder()).start();
        } else notifyUser("Build cancelled by user", BLACK);
    }

    private Thread createPdfBuilderThread(PdfBuilder pdfBuilder) {
        return BuildPdfTask.BuildPdfThreadFactory.createBuildPdfThread(
                () -> {
                    build(pdfBuilder);
                    return Unit.INSTANCE;
                }
        );
    }

    private void build(PdfBuilder pdfBuilder) {
        disableInput(true);
        try {
            buildPdf(pdfBuilder);
        } catch (Exception exception) {
            exception.printStackTrace();
            notifyUser("An error occurred while trying to build the PDF(s)", RED);
        } finally {
            disableInput(false);
        }
    }

    private void buildPdf(PdfBuilder pdfBuilder) {
        pdfBuilder.build(
                mainWindow.getDirectoryIterator(),
                imageToPdfOptions,
                new BuildProgressUpdater(this)
        );
        notifyUser("Finished building: " + imageToPdfOptions.getPdfOptions().getSaveLocation().getAbsolutePath(), GREEN);
    }

    /**
     * Builds multiple {@link de.nihas101.imageToPdfConverter.pdf.ImagePdf}s
     */
    private void buildMultiplePdf() {
        DirectoryChooser directoryChooser = createDirectoryChooser(imageToPdfOptions.getIteratorOptions());
        directoryChooser.setInitialDirectory(mainWindow.getDirectoryIterator().getParentDirectory());
        directoryChooser.setTitle("Choose a folder to save the PDFs in");
        File saveFile = directoryChooser.showDialog(buildButton.getScene().getWindow());

        if (saveFile != null) {
            setSaveLocation(saveFile);
            createPdfBuilderThread(ImageDirectoriesPdfBuilder.PdfBuilderFactory.createImageDirectoriesPdfBuilder()).start();
        } else notifyUser("Build cancelled by user", BLACK);
    }

    /**
     * @return True if a all necessary values for building the PDF are set, false otherwise
     */
    private boolean valuesSetForBuilding() {
        if (mainWindow.getDirectoryIterator() == null) {
            notifyUser("Please choose a directory", RED);
            return false;
        } else if (mainWindow.getDirectoryIterator().numberOfFiles() == 0) {
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
    public void notifyUser(String message, Paint color) {
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
            int index = imageListView.getSelectionModel().getSelectedIndex();
            try {
                createContentDisplayer(mainWindow.getDirectoryIterator()).displayContent(index, this);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void openOptionsMenu(ActionEvent actionEvent) {
        try {
            imageToPdfOptions = createOptionsMenu(imageToPdfOptions).setOptions();
        } catch (Exception e) {
            e.printStackTrace();
        }
        actionEvent.consume();
    }

    private void setSaveLocation(File saveLocation) {
        imageToPdfOptions.setSaveLocation(saveLocation);
    }
}
