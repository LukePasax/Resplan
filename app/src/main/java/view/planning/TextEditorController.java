package view.planning;

import Resplan.Starter;
import javafx.event.ActionEvent;
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
    public Button upload;

    public TextEditorController() {
    }

    public void setClipTitle(String clipTitle) {
        this.clipTitle = clipTitle;
        this.text.setText(Starter.getController().getClipText(this.clipTitle));
    }

    public void initialize() {
        this.saveButton.setDisable(true);
        this.text.setEditable(false);
    }

    public void editText(ActionEvent actionEvent) {
        this.text.setEditable(true);
        this.saveButton.setDisable(false);
    }

    public void saveText(ActionEvent actionEvent) {
        Starter.getController().setClipText(this.clipTitle, this.text.getText());
        this.editButton.setDisable(false);
        this.text.setEditable(false);
        this.saveButton.setDisable(true);
    }

    public void uploadFromFile(ActionEvent actionEvent) {
        final TextFilePicker picker = new TextFilePicker();
        final File file = picker.getFileChooser().showOpenDialog(this.text.getScene().getWindow());
        if (file != null) {
            try {
                Starter.getController().uploadTextFromFile(this.clipTitle, file.getAbsolutePath());
            } catch (IOException e) {
                AlertDispatcher.dispatchError(e.getMessage());
            }
        }
    }

}
