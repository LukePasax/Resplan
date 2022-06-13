package view.planning;

import Resplan.Starter;
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
        startTimePicker.setTextFormatter(new TextFormatter<>(converter.getFormatterUnaryOperator()));
        durationPicker.setTextFormatter(new TextFormatter<>(converter.getFormatterUnaryOperator()));

    }

    public void okButtonPressed(ActionEvent event) {
        try {

            Starter.getController().newClip(this.typePicker.getValue(),
                    this.clipTitleSelection.getText(), this.clipDescriptionSelection.getText(),
                    this.channelPicker.getValue(), this.converter.fromString(this.startTimePicker.getText()).doubleValue(),
                    this.converter.fromString(this.durationPicker.getText()).doubleValue(), file);
            clipTitleSelection.getScene().getWindow().hide();
        } catch (IllegalArgumentException | ImportException | NoSuchElementException | IllegalStateException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void cancelButtonPressed(ActionEvent event) {
        clipTitleSelection.getScene().getWindow().hide();
    }

    public void pickFilePressed(ActionEvent event) {
        WavFilePicker picker = new WavFilePicker();
        file = picker.getFileChooser().showOpenDialog(channelPicker.getScene().getWindow());
        clipFileUrl.setText(file.getAbsolutePath());
    }
}
