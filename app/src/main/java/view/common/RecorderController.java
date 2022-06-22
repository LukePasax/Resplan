package view.common;

import resplan.Starter;
import daw.core.clip.ClipNotFoundException;
import daw.manager.ImportException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;

public class RecorderController {

    public Button recButton;
    public Button stopButton;
    public Button cancelButton;
    public Label clipTitle;
    public Label timeLabel;
    public TextArea textArea;
    public VBox recorderBox;
    public GridPane grid;

    public void setClipTitle(final String clipTitle) {
        this.clipTitle.setText(clipTitle);
        Starter.getController().getClipText(clipTitle).ifPresent(s -> {
            this.textArea = new TextArea(s);
            this.grid.addRow(1);
            this.grid.getScene().getWindow().setHeight(370);
            this.grid.add(this.textArea, 0, 1);
            this.textArea.setEditable(false);
        });
    }

    public void recPressed(final ActionEvent actionEvent) {
        Starter.getController().startRecording();
        timeLabel.setText("Recording...");
    }

    public void stopPressed(final ActionEvent actionEvent) {
        final WavFilePicker picker = new WavFilePicker();
        final File file = picker.getFileChooser().showSaveDialog(this.recButton.getScene().getWindow());
        try {
            if (file != null) { 
            	timeLabel.setText("");
                Starter.getController().stopRecording(this.clipTitle.getText(), file);
                this.recButton.getScene().getWindow().hide();
            }
        } catch (ImportException | ClipNotFoundException | IOException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void cancelPressed(final ActionEvent actionEvent) {
        this.recButton.getScene().getWindow().hide();
    }
}
