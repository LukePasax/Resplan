package view.planning;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import resplan.Starter;
import view.common.AlertDispatcher;

public final class NewChannelController {
    @FXML
    private TextField titleSelection;
    @FXML
    private TextField descriptionSelection;
    @FXML
    private ChoiceBox<String> typeChoicebox;

    public void initialize() {
        this.typeChoicebox.getItems().addAll("SPEECH", "EFFECTS", "SOUNDTRACK");
        this.typeChoicebox.setValue(this.typeChoicebox.getItems().get(0));
    }


    public void okButtonPressed() {
        try {
            Starter.getController().newChannel(this.typeChoicebox.getValue(),
                    this.titleSelection.getText(), this.descriptionSelection.getText());
            titleSelection.getScene().getWindow().hide();
        } catch (IllegalArgumentException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void cancelButtonPressed() {
        this.typeChoicebox.getScene().getWindow().hide();
    }
}
