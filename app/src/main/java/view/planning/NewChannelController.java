package view.planning;

import resplan.Starter;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import view.common.AlertDispatcher;

public class NewChannelController {
    public TextField titleSelection;
    public TextField descriptionSelection;
    public ChoiceBox<String> typeChoicebox;
    public Button newChannelConfirm;
    public Button cancelButton;

    public void initialize() {
        this.typeChoicebox.getItems().addAll("SPEECH", "EFFECTS", "SOUNDTRACK");
        this.typeChoicebox.setValue(this.typeChoicebox.getItems().get(0));
    }


    public void okButtonPressed(ActionEvent event) {
        try {
            Starter.getController().newChannel(this.typeChoicebox.getValue(),
                    this.titleSelection.getText(),this.descriptionSelection.getText());
            titleSelection.getScene().getWindow().hide();
        } catch (IllegalArgumentException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void cancelButtonPressed(ActionEvent event) {
        this.typeChoicebox.getScene().getWindow().hide();
    }
}
