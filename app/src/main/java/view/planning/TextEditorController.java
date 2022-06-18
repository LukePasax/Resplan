package view.planning;

import Resplan.Starter;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import view.common.AlertDispatcher;
import view.common.TextFilePicker;
import java.io.File;
import java.io.IOException;

public class TextEditorController {

    public TextArea textArea;
    public Button editButton;
    public Button saveButton;
    private String clipTitle;
    public Button uploadButton;

    public void setClipTitle(String clipTitle) {
        this.clipTitle = clipTitle;
        this.textArea.setPromptText("Insert clip text here...");
        Starter.getController().getClipText(this.clipTitle).ifPresent(s -> this.textArea.setText(s));
    }

    public void initialize() {
        this.textArea.setEditable(false);
        this.saved();
    }

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
        Starter.getController().setClipText(this.clipTitle, this.textArea.getText());
        this.textArea.setEditable(false);
        this.saved();
    }

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
                Starter.getController().setClipTextFromFile(this.clipTitle, file.getAbsolutePath());
                Starter.getController().getClipText(this.clipTitle).ifPresent(s -> this.textArea.setText(s));
            } catch (IOException e) {
                AlertDispatcher.dispatchError(e.getMessage());
            }
        }
        this.textArea.setEditable(true);
        this.editing();
    }

}
