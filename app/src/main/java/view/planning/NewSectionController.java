package view.planning;

import resplan.Starter;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import view.common.AlertDispatcher;
import view.common.NumberFormatConverter;

import java.util.NoSuchElementException;

public class NewSectionController {
    public TextField title;
    public TextField description;
    public TextField startTime;
    public Button ok;
    public Button cancel;
    private NumberFormatConverter converter;

    public void initialize() {
        this.converter = new NumberFormatConverter();
        this.startTime.setTextFormatter(new TextFormatter<>(this.converter.getFormatterUnaryOperator()));
    }
    public void okPressed() {
        if ("".equals(this.startTime.getText())) {
            AlertDispatcher.dispatchError("Select a start time");
        } else {
            try {
                Starter.getController().newSection(this.title.getText(), this.description.getText(),
                        this.converter.fromString(this.startTime.getText()).doubleValue(), 0d);
                this.title.getScene().getWindow().hide();
            } catch (IllegalArgumentException | NoSuchElementException | IllegalStateException e) {
                AlertDispatcher.dispatchError(e.getLocalizedMessage());
            }
        }
    }

    public void cancelPressed() {
        this.title.getScene().getWindow().hide();
    }
}
