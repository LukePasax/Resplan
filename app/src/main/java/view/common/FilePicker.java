package view.common;

import javafx.stage.FileChooser;

import java.util.Arrays;
import java.util.List;


public abstract class FilePicker {

    FileChooser fileChooser;

    public FilePicker(String description, String... extension) {
        this.fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(description, Arrays.asList(extension));
        this.fileChooser.getExtensionFilters().add(filter);
        this.fileChooser.setSelectedExtensionFilter(filter);
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }
}
