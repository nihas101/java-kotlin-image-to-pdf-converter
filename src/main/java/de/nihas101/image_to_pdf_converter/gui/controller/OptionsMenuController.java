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

package de.nihas101.image_to_pdf_converter.gui.controller;

import com.itextpdf.kernel.pdf.PdfVersion;
import de.nihas101.image_to_pdf_converter.pdf.pdf_options.ImageToPdfOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

import static com.itextpdf.kernel.pdf.CompressionConstants.*;
import static com.itextpdf.kernel.pdf.PdfVersion.*;
import static javafx.geometry.Pos.CENTER;

public class OptionsMenuController {
    @FXML
    public CheckBox multipleDirectoriesCheckBox;
    public ToggleGroup pdfCompressionToggle;
    public ToggleGroup pdfVersionToggle;
    public CheckBox zipFilesCheckBox;
    public CheckBox deleteOnExitCheckBox;
    public MenuButton pdfMenu;
    public MenuButton compressionMenu;
    public RadioMenuItem PDF_1_0_item;
    public RadioMenuItem PDF_1_1_item;
    public RadioMenuItem PDF_1_2_item;
    public RadioMenuItem PDF_1_3_item;
    public RadioMenuItem PDF_1_4_item;
    public RadioMenuItem PDF_1_5_item;
    public RadioMenuItem PDF_1_6_item;
    public RadioMenuItem PDF_1_7_item;
    public RadioMenuItem PDF_2_0_item;
    public RadioMenuItem noCompression_item;
    public RadioMenuItem defaultCompression_item;
    public RadioMenuItem bestCompression_item;
    public RadioMenuItem speedCompression_item;
    public Spinner<Integer> maximalDepthSearchSpinner;
    public CheckBox saveToParticularLocationCheckBox;
    private ImageToPdfOptions imageToPdfOptions;

    public void setMultipleDirectoriesOption(ActionEvent actionEvent) {
        imageToPdfOptions.setMultipleDirectories(multipleDirectoriesCheckBox.isSelected());
        maximalDepthSearchSpinner.setDisable(!multipleDirectoriesCheckBox.isSelected());
        actionEvent.consume();
    }

    public void setup(ImageToPdfOptions imageToPdfOptions) {
        this.imageToPdfOptions = imageToPdfOptions;

        multipleDirectoriesCheckBox.setSelected(imageToPdfOptions.getIteratorOptions().getMultipleDirectories());
        maximalDepthSearchSpinner.setDisable(!multipleDirectoriesCheckBox.isSelected());
        maximalDepthSearchSpinner.getEditor().setAlignment(CENTER);
        zipFilesCheckBox.setSelected(imageToPdfOptions.getIteratorOptions().getIncludeZipFiles());
        deleteOnExitCheckBox.setSelected(imageToPdfOptions.getIteratorOptions().getDeleteOnExit());

        setupPdfVersionUserData(pdfVersionToggle);
        setSelectedPdfVersion(pdfVersionToggle);
        setupCompressionUserData(pdfCompressionToggle);
        setSelectedCompression(pdfCompressionToggle);
        maximalDepthSearchSpinner.getValueFactory().setValue(imageToPdfOptions.getIteratorOptions().getMaximalSearchDepth());
        saveToParticularLocationCheckBox.setSelected(imageToPdfOptions.getPdfOptions().getUseCustomLocation());
    }

    private void setupPdfVersionUserData(ToggleGroup pdfVersionToggle) {
        List<Toggle> toggles = pdfVersionToggle.getToggles();

        toggles.get(0).setUserData(PDF_1_0);
        toggles.get(1).setUserData(PDF_1_1);
        toggles.get(2).setUserData(PDF_1_2);
        toggles.get(3).setUserData(PDF_1_3);
        toggles.get(4).setUserData(PDF_1_4);
        toggles.get(5).setUserData(PDF_1_5);
        toggles.get(6).setUserData(PDF_1_6);
        toggles.get(7).setUserData(PDF_1_7);
        toggles.get(8).setUserData(PDF_2_0);
    }

    private void setSelectedPdfVersion(ToggleGroup pdfVersionToggle) {
        PdfVersion pdfVersion = imageToPdfOptions.getPdfOptions().getPdfVersion();
        int index = pdfVersionToIndex(pdfVersion);

        pdfVersionToggle.getToggles().get(index).setSelected(true);
    }

    private int pdfVersionToIndex(PdfVersion pdfVersion) {
        if (pdfVersion.equals(PDF_1_0)) return 0;
        else if (pdfVersion.equals(PDF_1_1)) return 1;
        else if (pdfVersion.equals(PDF_1_2)) return 2;
        else if (pdfVersion.equals(PDF_1_3)) return 3;
        else if (pdfVersion.equals(PDF_1_4)) return 4;
        else if (pdfVersion.equals(PDF_1_5)) return 5;
        else if (pdfVersion.equals(PDF_1_6)) return 6;
        else if (pdfVersion.equals(PDF_2_0)) return 8;
        else return 7;
    }

    private void setupCompressionUserData(ToggleGroup pdfCompressionToggle) {
        List<Toggle> toggles = pdfCompressionToggle.getToggles();

        toggles.get(0).setUserData(NO_COMPRESSION);
        toggles.get(1).setUserData(DEFAULT_COMPRESSION);
        toggles.get(2).setUserData(BEST_COMPRESSION);
        toggles.get(3).setUserData(BEST_SPEED);
    }

    private void setSelectedCompression(ToggleGroup pdfCompressionToggle) {
        switch (imageToPdfOptions.getPdfOptions().getCompressionLevel()) {
            case NO_COMPRESSION:
                pdfCompressionToggle.getToggles().get(0).setSelected(true);
                break;
            case BEST_COMPRESSION:
                pdfCompressionToggle.getToggles().get(2).setSelected(true);
                break;
            case BEST_SPEED:
                pdfCompressionToggle.getToggles().get(3).setSelected(true);
                break;
            default:
                pdfCompressionToggle.getToggles().get(1).setSelected(true);
                break;
        }
    }

    public ImageToPdfOptions getImageToPdfOptions() {
        imageToPdfOptions.setMaximalSearchDepth((Integer) maximalDepthSearchSpinner.getValue());
        return imageToPdfOptions;
    }

    public void setPdfVersion(ActionEvent actionEvent) {
        imageToPdfOptions.setPdfVersion((PdfVersion) pdfVersionToggle.getSelectedToggle().getUserData());
        actionEvent.consume();
    }

    public void setCompression(ActionEvent actionEvent) {
        imageToPdfOptions.setCompressionLevel((Integer) pdfCompressionToggle.getSelectedToggle().getUserData());
        actionEvent.consume();
    }

    public void setZipFilesOption(ActionEvent actionEvent) {
        imageToPdfOptions.setZipFiles(zipFilesCheckBox.isSelected());
        actionEvent.consume();
    }

    public void setDeleteOnExitOption(ActionEvent actionEvent) {
        imageToPdfOptions.setDeleteOnExit(deleteOnExitCheckBox.isSelected());
        actionEvent.consume();
    }

    public void setSaveToCustomLocation(ActionEvent actionEvent) {
        imageToPdfOptions.setCustomLocation(saveToParticularLocationCheckBox.isSelected());
        actionEvent.consume();
    }
}
