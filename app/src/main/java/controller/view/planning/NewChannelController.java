package controller.view.planning;

import controller.view.planningApp;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

public class NewChannelController {
    public TextField titleSelection;
    public TextField descriptionSelection;
    public ChoiceBox<String> typeChoicebox;
    public Button newChannelConfirm;

    public void initialize() {
        this.typeChoicebox.getItems().addAll("Speaker", "Effects", "Soundtrack");
    }


    public void okButtonPressed(ActionEvent event) {
        try {
            planningApp.getController().newPlanningChannel(this.typeChoicebox.getValue(),
                    this.titleSelection.getText(),this.descriptionSelection.getText());
        } catch (IllegalArgumentException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("error");
            error.setContentText(e.getLocalizedMessage());
            error.showAndWait();
        }
    }
}
