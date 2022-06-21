package view.common;

import javafx.stage.FileChooser;

import java.util.Arrays;


public abstract class FilePicker {

    private FileChooser fileChooser;

    public FilePicker(final String description, final String... extension) {
        this.fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(description, Arrays.asList(extension));
        this.fileChooser.getExtensionFilters().add(filter);
        this.fileChooser.setSelectedExtensionFilter(filter);
    }

    /**
     * @return The file chooser.
     */
    public FileChooser getFileChooser() {
        return fileChooser;
    }
}
