package de.nihas101.imageToPdfConverter.gui.controller;

import com.itextpdf.kernel.pdf.PdfVersion;
import de.nihas101.imageToPdfConverter.pdf.pdfOptions.ImageToPdfOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import java.util.List;

import static com.itextpdf.kernel.pdf.CompressionConstants.*;
import static com.itextpdf.kernel.pdf.PdfVersion.*;

public class OptionsMenuController {
    @FXML
    public CheckBox multipleDirectoriesCheckBox;
    public ToggleGroup pdfCompressionToggle;
    public ToggleGroup pdfVersionToggle;
    public CheckBox zipFilesCheckBox;
    public CheckBox deleteOnExitCheckBox;
    private ImageToPdfOptions imageToPdfOptions;

    public void setMultipleDirectoriesOption(ActionEvent actionEvent) {
        imageToPdfOptions.setMultipleDirectories(multipleDirectoriesCheckBox.isSelected());
        actionEvent.consume();
    }

    public void setup(ImageToPdfOptions imageToPdfOptions) {
        this.imageToPdfOptions = imageToPdfOptions;

        multipleDirectoriesCheckBox.setSelected(imageToPdfOptions.getIteratorOptions().getMultipleDirectories());
        zipFilesCheckBox.setSelected(imageToPdfOptions.getIteratorOptions().getZipFiles());
        deleteOnExitCheckBox.setSelected(imageToPdfOptions.getIteratorOptions().getDeleteOnExit());

        setupPdfVersionUserData(pdfVersionToggle);
        setSelectedPdfVersion(pdfVersionToggle);
        setupCompressionUserData(pdfCompressionToggle);
        setSelectedCompression(pdfCompressionToggle);
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
}
