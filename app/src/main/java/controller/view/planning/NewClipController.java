package controller.view.planning;

import controller.view.planningApp;
import daw.manager.ImportException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

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
    private File file;

    public void initialize() {
        this.channelPicker.getItems().addAll(planningApp.getController().getChannelList());
        this.channelPicker.setValue(this.channelPicker.getItems().get(0));
        this.typePicker.getItems().addAll("Speaker", "Effects", "Soundtrack");
        this.typePicker.setValue(this.typePicker.getItems().get(0));
    }

    public void okButtonPressed(ActionEvent event) {
        try {
            planningApp.getController().newPlanningClip(this.typePicker.getValue(),
                    this.clipTitleSelection.getText(), this.clipDescriptionSelection.getText(),
                    this.channelPicker.getValue(), 0.0, file);
            clipTitleSelection.getScene().getWindow().hide();
        } catch (IllegalArgumentException | ImportException | NoSuchElementException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setContentText(e.getLocalizedMessage());
            error.showAndWait();
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
