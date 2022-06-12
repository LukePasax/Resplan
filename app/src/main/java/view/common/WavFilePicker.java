package view.common;

import javafx.stage.FileChooser;

public class WavFilePicker {

    private final FileChooser fileChooser;

    public WavFilePicker() {
        this.fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("File WAV", "*.wav");
        this.fileChooser.getExtensionFilters().add(extensionFilter);
        this.fileChooser.setSelectedExtensionFilter(extensionFilter);
    }

    public FileChooser getFileChooser() {
        return this.fileChooser;
    }
}
