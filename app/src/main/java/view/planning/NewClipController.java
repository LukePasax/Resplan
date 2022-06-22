package view.planning;

import daw.manager.ImportException;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import resplan.Starter;
import view.common.AlertDispatcher;
import view.common.NumberFormatConverter;
import view.common.WavFilePicker;

import java.io.File;
import java.util.NoSuchElementException;

public final class NewClipController {
    @FXML
    private TextField clipTitleSelection;
    @FXML
    private TextField clipDescriptionSelection;
    @FXML
    private TextField clipFileUrl;
    @FXML
    private ChoiceBox<String> channelPicker;
    @FXML
    private TextField startTimePicker;
    @FXML
    private TextField durationPicker;
    private File file;
    private NumberFormatConverter converter;

    public void initialize() {
        this.channelPicker.getItems().addAll(Starter.getController().getChannelList());
        this.channelPicker.setValue(this.channelPicker.getItems().get(0));
        this.converter = new NumberFormatConverter();
        this.startTimePicker.setTextFormatter(new TextFormatter<>(this.converter.getFormatterUnaryOperator()));
        this.durationPicker.setTextFormatter(new TextFormatter<>(this.converter.getFormatterUnaryOperator()));

    }

    public ChoiceBox<String> getChannelPicker() {
        return channelPicker;
    }

    public TextField getStartTimePicker() {
        return startTimePicker;
    }

    public TextField getDurationPicker() {
        return durationPicker;
    }

    public void okButtonPressed() {
        if ("".equals(this.startTimePicker.getText())) {
            AlertDispatcher.dispatchError("Select a start time");
        } else if ("".equals(this.durationPicker.getText())) {
            AlertDispatcher.dispatchError("Select a duration");
        } else {
            try {
                Starter.getController().newClip(Starter.getController().getChannelType(this.channelPicker.getValue()),
                        this.clipTitleSelection.getText(), this.clipDescriptionSelection.getText(),
                        this.channelPicker.getValue(), this.converter.fromString(this.startTimePicker.getText()).doubleValue(),
                        this.converter.fromString(this.durationPicker.getText()).doubleValue(), this.file);
                this.clipTitleSelection.getScene().getWindow().hide();
            } catch (IllegalArgumentException | ImportException | NoSuchElementException | IllegalStateException e) {
                AlertDispatcher.dispatchError(e.getLocalizedMessage());
            }
        }
    }

    public void cancelButtonPressed() {
        this.clipTitleSelection.getScene().getWindow().hide();
    }

    public void pickFilePressed() {
        final WavFilePicker picker = new WavFilePicker();
        this.file = picker.getFileChooser().showOpenDialog(this.channelPicker.getScene().getWindow());
        if (this.file != null) {
            this.clipFileUrl.setText(this.file.getAbsolutePath());
        }
    }
}
