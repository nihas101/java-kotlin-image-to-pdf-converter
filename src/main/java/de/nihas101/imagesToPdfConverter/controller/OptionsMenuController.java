package de.nihas101.imagesToPdfConverter.controller;

import com.itextpdf.kernel.pdf.CompressionConstants;
import com.itextpdf.kernel.pdf.PdfVersion;
import de.nihas101.imagesToPdfConverter.pdf.Options;
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
    private Options options;

    public void setMultipleDirectoriesOption(ActionEvent actionEvent) {
        options = options.copy(multipleDirectoriesCheckBox.isSelected(), options.getCompressionLevel(), options.getPdfVersion());
        actionEvent.consume();
    }

    public void setup(Options options) {
        this.options = options;
        multipleDirectoriesCheckBox.setSelected(options.getMultipleDirectories());
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
        PdfVersion pdfVersion = options.getPdfVersion();

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
        switch (options.getCompressionLevel()){
            case NO_COMPRESSION: pdfCompressionToggle.getToggles().get(0).setSelected(true); break;
            case BEST_COMPRESSION: pdfCompressionToggle.getToggles().get(2).setSelected(true); break;
            case BEST_SPEED: pdfCompressionToggle.getToggles().get(3).setSelected(true); break;
            default: pdfCompressionToggle.getToggles().get(1).setSelected(true); break;
        }
    }

    public Options getOptions() {
        return options;
    }

    public void setPdfVersion(ActionEvent actionEvent) {
        options = options.copy(options.getMultipleDirectories(), options.getCompressionLevel(), (PdfVersion) pdfVersionToggle.getSelectedToggle().getUserData());
        actionEvent.consume();
    }

    public void setCompression(ActionEvent actionEvent) {
        options = options.copy(options.getMultipleDirectories(), (Integer) pdfCompressionToggle.getSelectedToggle().getUserData(), options.getPdfVersion());
        actionEvent.consume();
    }
}
