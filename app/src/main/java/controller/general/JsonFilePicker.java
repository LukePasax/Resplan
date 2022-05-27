package controller.general;

import javafx.stage.FileChooser;
import java.io.File;

public class JsonFilePicker {

    private final FileChooser fileChooser;

    public JsonFilePicker() {
        this.fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("File JSON", "*.json");
        this.fileChooser.getExtensionFilters().add(extensionFilter);
        this.fileChooser.setSelectedExtensionFilter(extensionFilter);
    }

    public FileChooser getFileChooser() {
        return this.fileChooser;
    }

}
