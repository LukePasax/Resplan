package view.planning;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import resplan.Starter;
import view.common.AlertDispatcher;
import view.common.NumberFormatConverter;

import java.util.NoSuchElementException;

public final class NewSectionController {
    @FXML
    private TextField title;
    @FXML
    private TextField description;
    @FXML
    private TextField startTime;
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
