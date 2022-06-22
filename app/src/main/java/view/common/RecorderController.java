package view.common;

import daw.core.clip.ClipNotFoundException;
import daw.manager.ImportException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import resplan.Starter;
import java.io.File;
import java.io.IOException;

public final class RecorderController {

    public static final int HEIGHT = 370;
    @FXML
    private Button recButton;
    @FXML
    private Label clipTitle;
    @FXML
    private Label timeLabel;
    private TextArea textArea;
    @FXML
    private GridPane grid;

    public void setClipTitle(final String clipTitle) {
        this.clipTitle.setText(clipTitle);
        Starter.getController().getClipText(clipTitle).ifPresent(s -> {
            this.textArea = new TextArea(s);
            this.grid.addRow(1);
            this.grid.getScene().getWindow().setHeight(HEIGHT);
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
