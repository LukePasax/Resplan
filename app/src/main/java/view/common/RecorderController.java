package view.common;

import Resplan.Starter;
import daw.manager.ImportException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
        try {
            Starter.getController().stopRecording(this.clipTitle.getText());
        } catch (ImportException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void cancelPressed(ActionEvent actionEvent) {
        this.recButton.getScene().getWindow().hide();
    }
}
