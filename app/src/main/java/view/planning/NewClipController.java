package view.planning;

import Resplan.Starter;
import daw.core.clip.ClipNotFoundException;
import daw.manager.ImportException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import view.common.AlertDispatcher;
import view.common.NumberFormatConverter;
import view.common.TimeAxisSetter;
import view.common.WavFilePicker;

import java.io.File;
import java.util.AbstractList;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

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
    private NumberFormatConverter converter;

    public void initialize() {
        this.channelPicker.getItems().addAll(Starter.getController().getChannelList());
        this.channelPicker.setValue(this.channelPicker.getItems().get(0));
        this.typePicker.getItems().addAll("Speaker", "Effects", "Soundtrack");
        this.typePicker.setValue(this.typePicker.getItems().get(0));
        this.converter = new NumberFormatConverter();
        this.startTimePicker.setTextFormatter(new TextFormatter<>(this.converter.getFormatterUnaryOperator()));
        this.durationPicker.setTextFormatter(new TextFormatter<>(this.converter.getFormatterUnaryOperator()));

    }

    public void okButtonPressed(ActionEvent event) {
        if (this.startTimePicker.getText().equals("")) {
            AlertDispatcher.dispatchError("Select a start time");
        } else if (this.durationPicker.getText().equals("")) {
            AlertDispatcher.dispatchError("Select a duration");
        } else {
            try {
                Starter.getController().newClip(Starter.getController().getChannelType(this.channelPicker.getValue()),
                        this.clipTitleSelection.getText(), this.clipDescriptionSelection.getText(),
                        this.channelPicker.getValue(), this.converter.fromString(this.startTimePicker.getText()).doubleValue(),
                        this.converter.fromString(this.durationPicker.getText()).doubleValue(), this.file);
                this.clipTitleSelection.getScene().getWindow().hide();
            } catch (IllegalArgumentException | ImportException | NoSuchElementException | IllegalStateException |
                     ClipNotFoundException e) {
                AlertDispatcher.dispatchError(e.getLocalizedMessage());
            }
        }
    }

    public void cancelButtonPressed(ActionEvent event) {
        this.clipTitleSelection.getScene().getWindow().hide();
    }

    public void pickFilePressed(ActionEvent event) {
        WavFilePicker picker = new WavFilePicker();
        this.file = picker.getFileChooser().showOpenDialog(this.channelPicker.getScene().getWindow());
        if (this.file != null) {
            this.clipFileUrl.setText(this.file.getAbsolutePath());
        }
    }
}
