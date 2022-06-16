package view.common;

import Resplan.Starter;
import daw.core.clip.ClipNotFoundException;
import daw.manager.ImportException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;

public class RecorderController {

    public Button recButton;
    public Button stopButton;
    public Button cancelButton;
    public Label clipTitle;

    public void setClipTitle(String clipTitle) {
        this.clipTitle.setText(clipTitle);
    }

    public void recPressed(ActionEvent actionEvent) {
        Starter.getController().startRecording();
    }

    public void stopPressed(ActionEvent actionEvent) {
        WavFilePicker picker = new WavFilePicker();
        File file = picker.getFileChooser().showSaveDialog(this.recButton.getScene().getWindow());
        try {
            if (file != null) {
                Starter.getController().stopRecording(this.clipTitle.getText(), file);
                this.recButton.getScene().getWindow().hide();
            }
        } catch (ImportException | ClipNotFoundException | IOException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void cancelPressed(ActionEvent actionEvent) {
        this.recButton.getScene().getWindow().hide();
    }
}
