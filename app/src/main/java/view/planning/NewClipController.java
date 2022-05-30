package view.planning;

import Resplan.Starter;
import daw.manager.ImportException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import view.common.AlertDispatcher;

import java.io.File;
import java.util.NoSuchElementException;

public class NewClipController {
    public TextField clipTitleSelection;
    public TextField clipDescriptionSelection;
    public Button newClipConfirm;
    public Button clipFilePicker;
    public TextField clipFileUrl;
    public ChoiceBox<String> channelPicker;
    public ChoiceBox<String> typePicker;
    public TextField startTimePicker;
    public TextField durationPicker;
    private File file;

    public void initialize() {
        this.channelPicker.getItems().addAll(Starter.getController().getChannelList());
        this.channelPicker.setValue(this.channelPicker.getItems().get(0));
        this.typePicker.getItems().addAll("Speaker", "Effects", "Soundtrack");
        this.typePicker.setValue(this.typePicker.getItems().get(0));
    }

    public void okButtonPressed(ActionEvent event) {
        try {
            Starter.getController().newClip(this.typePicker.getValue(),
                    this.clipTitleSelection.getText(), this.clipDescriptionSelection.getText(),
                    this.channelPicker.getValue(), Double.parseDouble(startTimePicker.getText()),
                    Double.parseDouble(durationPicker.getText()), file);
            clipTitleSelection.getScene().getWindow().hide();
        } catch (IllegalArgumentException | ImportException | NoSuchElementException | IllegalStateException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void cancelButtonPressed(ActionEvent event) {
        clipTitleSelection.getScene().getWindow().hide();
    }

    public void pickFilePressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        file = chooser.showOpenDialog(channelPicker.getScene().getWindow());
        clipFileUrl.setText(file.getAbsolutePath());
    }
}
