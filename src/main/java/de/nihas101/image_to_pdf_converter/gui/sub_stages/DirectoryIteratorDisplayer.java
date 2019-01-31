/*
 JaKoImage2PDF is a program for converting images to PDFs with the use of iText 7
 Copyright (C) 2018  Nikita Hasert

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.nihas101.image_to_pdf_converter.gui.sub_stages;

import de.nihas101.image_to_pdf_converter.directory_iterators.DirectoryIterator;
import de.nihas101.image_to_pdf_converter.gui.controller.MainWindowController;
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.IteratorOptions;
import de.nihas101.image_to_pdf_converter.tasks.CallClosure;
import de.nihas101.image_to_pdf_converter.tasks.SetupIteratorTask;
import de.nihas101.image_to_pdf_converter.util.IteratorSetupProgressUpdater;
import de.nihas101.image_to_pdf_converter.util.JaKoLogger;
import javafx.scene.image.Image;
import kotlin.Unit;

import java.io.File;
import java.net.MalformedURLException;

import static de.nihas101.image_to_pdf_converter.gui.sub_stages.DirectoryContentDisplay.createDirectoryContentDisplay;
import static de.nihas101.image_to_pdf_converter.gui.sub_stages.ImageDisplay.createImageDisplay;
import static de.nihas101.image_to_pdf_converter.util.JaKoLogger.createLogger;
import static javafx.application.Platform.runLater;
import static javafx.scene.paint.Color.WHITE;

/**
 * A class for displaying the content of a {@link DirectoryIterator}
 */
public final class DirectoryIteratorDisplayer {
    /**
     * The {@link DirectoryIterator} of which to display the content
     */
    private final DirectoryIterator directoryIterator;

    private static JaKoLogger logger = createLogger(DirectoryIteratorDisplayer.class);

    private DirectoryIteratorDisplayer(DirectoryIterator directoryIterator) {
        this.directoryIterator = directoryIterator;
    }

    /**
     * The factory method for creating {@link DirectoryIteratorDisplayer}s
     *
     * @param directoryIterator The {@link DirectoryIterator} of which to display the content
     * @return The created {@link DirectoryIteratorDisplayer} instance
     */
    public static DirectoryIteratorDisplayer createContentDisplayer(DirectoryIterator directoryIterator) {
        return new DirectoryIteratorDisplayer(directoryIterator);
    }

    /**
     * Displays the content found at the given index of the {@link DirectoryIterator} instance
     *
     * @param index The index of the content to display
     */
    public void displayContent(int index, MainWindowController mainWindowController) {
        if (directoryIterator.getFile(index).isDirectory()) displayDirectory(index, mainWindowController);
        else displayImage(index, mainWindowController);
    }

    /**
     * Displays the content of the directory found at the given index of the {@link DirectoryIterator}
     *
     * @param index The index of the directory to display
     */
    private void displayDirectory(int index, MainWindowController mainWindowController) {
        DirectoryIterator imageFilesIterator = DirectoryIterator.DirectoryIteratorFactory.createDirectoryIterator(
                new IteratorOptions()
        );

        CallClosure callClosure = new CallClosure(() -> {
            runLater(() -> mainWindowController.disableInput(true));
            return Unit.INSTANCE;
        }, () -> {
            runLater(() -> {
                mainWindowController.buildProgressBar.setProgress(0);
                mainWindowController.notifyUser("Files: " + directoryIterator.numberOfFiles(), WHITE);
                DirectoryContentDisplay directoryContentDisplay = createDirectoryContentDisplay(
                        imageFilesIterator,
                        index,
                        mainWindowController
                );

                directoryContentDisplay.displayContent();
                mainWindowController.disableInput(false);
            });
            return Unit.INSTANCE;
        });

        SetupIteratorTask setupIteratorTask = SetupIteratorTask.SetupIteratorTaskFactory.createSetupIteratorTask(
                imageFilesIterator,
                directoryIterator.getFile(index),
                new IteratorSetupProgressUpdater(mainWindowController),
                callClosure
        );

        mainWindowController.mainWindow.taskManager.start(setupIteratorTask, true);
    }

    /**
     * Displays the image found at the given index of the {@link DirectoryIterator}
     *
     * @param index The index of the image to display
     */
    private void displayImage(int index, MainWindowController mainWindowController) {
        File file = directoryIterator.getFile(index);

        runLater(() -> {
            mainWindowController.disableInput(true);
            ImageDisplay imageDisplay;
            try {
                imageDisplay = createImageDisplay(
                        new Image(String.valueOf(file.toURI().toURL())),
                        file.getName()
                );
            } catch (MalformedURLException exception) {
                logger.error("{}", exception);
                return;
            }

            imageDisplay.displayImage();
            mainWindowController.disableInput(false);
        });
    }
}
