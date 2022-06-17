package view.planning;

import Resplan.Starter;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import view.common.ViewDataImpl;

public class TextEditorController {

    public TextArea text;
    public Button editButton;
    public Button saveButton;
    public final String clipTitle;

    public TextEditorController(String clip) {
        this.clipTitle = clip;
    }

    public void initialize() {
        this.saveButton.setDisable(true);
        this.text.setText(Starter.getController().getClipText(this.clipTitle));
        this.text.setEditable(false);
    }

    public void editText(ActionEvent actionEvent) {
        this.text.setEditable(true);
        this.saveButton.setDisable(false);
    }

    public void saveText(ActionEvent actionEvent) {
        Starter.getController().setClipText(this.clipTitle, this.text.getText());
    }

}
