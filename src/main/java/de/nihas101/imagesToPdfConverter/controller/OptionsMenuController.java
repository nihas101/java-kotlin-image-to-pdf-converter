package de.nihas101.imagesToPdfConverter.controller;

import com.itextpdf.kernel.pdf.PdfVersion;
import de.nihas101.imagesToPdfConverter.pdf.PdfWriterOptions;
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
    private PdfWriterOptions pdfWriterOptions;

    public void setMultipleDirectoriesOption(ActionEvent actionEvent) {
        pdfWriterOptions = pdfWriterOptions.copy(multipleDirectoriesCheckBox.isSelected(), pdfWriterOptions.getCompressionLevel(), pdfWriterOptions.getPdfVersion());
        actionEvent.consume();
    }

    public void setup(PdfWriterOptions pdfWriterOptions) {
        this.pdfWriterOptions = pdfWriterOptions;
        multipleDirectoriesCheckBox.setSelected(pdfWriterOptions.getMultipleDirectories());
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
        PdfVersion pdfVersion = pdfWriterOptions.getPdfVersion();

        if(pdfVersion.equals(PDF_1_0))
            pdfVersionToggle.getToggles().get(0).setSelected(true);
        else if(pdfVersion.equals(PDF_1_1))
            pdfVersionToggle.getToggles().get(1).setSelected(true);
        else if(pdfVersion.equals(PDF_1_2))
            pdfVersionToggle.getToggles().get(2).setSelected(true);
        else if(pdfVersion.equals(PDF_1_3))
            pdfVersionToggle.getToggles().get(3).setSelected(true);
        else if(pdfVersion.equals(PDF_1_4))
            pdfVersionToggle.getToggles().get(4).setSelected(true);
        else if(pdfVersion.equals(PDF_1_5))
            pdfVersionToggle.getToggles().get(5).setSelected(true);
        else if(pdfVersion.equals(PDF_1_6))
            pdfVersionToggle.getToggles().get(6).setSelected(true);
        else if(pdfVersion.equals(PDF_2_0))
            pdfVersionToggle.getToggles().get(8).setSelected(true);
        else
            pdfVersionToggle.getToggles().get(7).setSelected(true);
    }

    private void setupCompressionUserData(ToggleGroup pdfCompressionToggle) {
        List<Toggle> toggles = pdfCompressionToggle.getToggles();

        toggles.get(0).setUserData(NO_COMPRESSION);
        toggles.get(1).setUserData(DEFAULT_COMPRESSION);
        toggles.get(2).setUserData(BEST_COMPRESSION);
        toggles.get(3).setUserData(BEST_SPEED);
    }

    private void setSelectedCompression(ToggleGroup pdfCompressionToggle) {
        switch (pdfWriterOptions.getCompressionLevel()){
            case NO_COMPRESSION: pdfCompressionToggle.getToggles().get(0).setSelected(true); break;
            case BEST_COMPRESSION: pdfCompressionToggle.getToggles().get(2).setSelected(true); break;
            case BEST_SPEED: pdfCompressionToggle.getToggles().get(3).setSelected(true); break;
            default: pdfCompressionToggle.getToggles().get(1).setSelected(true); break;
        }
    }

    public PdfWriterOptions getPdfWriterOptions() {
        return pdfWriterOptions;
    }

    public void setPdfVersion(ActionEvent actionEvent) {
        pdfWriterOptions = pdfWriterOptions.copy(pdfWriterOptions.getMultipleDirectories(), pdfWriterOptions.getCompressionLevel(), (PdfVersion) pdfVersionToggle.getSelectedToggle().getUserData());
        actionEvent.consume();
    }

    public void setCompression(ActionEvent actionEvent) {
        pdfWriterOptions = pdfWriterOptions.copy(pdfWriterOptions.getMultipleDirectories(), (Integer) pdfCompressionToggle.getSelectedToggle().getUserData(), pdfWriterOptions.getPdfVersion());
        actionEvent.consume();
    }
}
