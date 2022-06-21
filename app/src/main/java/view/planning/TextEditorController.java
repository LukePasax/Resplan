package view.planning;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import view.common.AlertDispatcher;
import view.common.TextFilePicker;
import java.io.File;
import java.io.IOException;

public abstract class TextEditorController {

    public TextArea textArea;
    public Button editButton;
    public Button saveButton;
    protected String title;
    public Button uploadButton;

    public void initialize() {
        this.textArea.setEditable(false);
        this.saved();
    }

    public void setTitle( final String title) {
        this.title = title;
        this.setPromptText();
        this.setTextArea();
    }

    protected abstract void setPromptText();

    protected abstract void setTextArea();

    public void editText() {
        this.textArea.setEditable(true);
        this.editing();
    }

    private void editing() {
        this.editButton.setDisable(true);
        this.uploadButton.setDisable(true);
        this.saveButton.setDisable(false);
    }

    public void saveText() {
        this.onSave();
        this.textArea.setEditable(false);
        this.saved();
    }

    protected abstract void onSave();

    private void saved() {
        this.editButton.setDisable(false);
        this.uploadButton.setDisable(false);
        this.saveButton.setDisable(true);
    }

    public void uploadFromFile() {
        final TextFilePicker picker = new TextFilePicker();
        final File file = picker.getFileChooser().showOpenDialog(this.textArea.getScene().getWindow());
        if (file != null) {
            try {
                this.onUploadFromFile(file);
            } catch (IOException e) {
                AlertDispatcher.dispatchError(e.getMessage());
            }
        }
        this.textArea.setEditable(true);
        this.editing();
    }

    protected abstract void onUploadFromFile(final File file) throws IOException;

}
