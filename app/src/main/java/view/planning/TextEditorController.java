package view.planning;

import Resplan.Starter;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import view.common.AlertDispatcher;
import view.common.TextFilePicker;
import java.io.File;
import java.io.IOException;

public class TextEditorController {

    public TextArea text;
    public Button editButton;
    public Button saveButton;
    private String clipTitle;
    public Button uploadButton;

    public void setClipTitle(String clipTitle) {
        this.clipTitle = clipTitle;
        this.text.setText(Starter.getController().getClipText(this.clipTitle));
    }

    public void initialize() {
        this.text.setEditable(false);
        this.saved();
    }

    public void editText() {
        this.text.setEditable(true);
        this.editing();
    }

    private void editing() {
        this.editButton.setDisable(true);
        this.uploadButton.setDisable(true);
        this.saveButton.setDisable(false);
    }

    public void saveText() {
        Starter.getController().setClipText(this.clipTitle, this.text.getText());
        this.text.setEditable(false);
        this.saved();
    }

    private void saved() {
        this.editButton.setDisable(false);
        this.uploadButton.setDisable(false);
        this.saveButton.setDisable(true);
    }

    public void uploadFromFile() {
        final TextFilePicker picker = new TextFilePicker();
        final File file = picker.getFileChooser().showOpenDialog(this.text.getScene().getWindow());
        if (file != null) {
            try {
                Starter.getController().uploadTextFromFile(this.clipTitle, file.getAbsolutePath());
                this.text.setText(Starter.getController().getClipText(this.clipTitle));
            } catch (IOException e) {
                AlertDispatcher.dispatchError(e.getMessage());
            }
        }
        this.text.setEditable(true);
        this.editing();
    }

}
