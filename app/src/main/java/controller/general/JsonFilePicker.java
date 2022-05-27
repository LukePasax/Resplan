package controller.general;

import javafx.stage.FileChooser;
import java.io.File;

public class JsonFilePicker {

    private final FileChooser fileChooser;

    public JsonFilePicker() {
        this.fileChooser = new FileChooser();
        this.fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("File JSON", "*.json"));
        this.fileChooser.setInitialDirectory(new File(System.getProperty("user.root")));
    }

    public FileChooser getFileChooser() {
        return this.fileChooser;
    }

}
