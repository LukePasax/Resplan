package view.planning;

import Resplan.Starter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.Objects;

public class RubricController {

    public TableView<Speaker> tableView;
    public TableColumn<Speaker, String> speakerCodeColumn;
    public TableColumn<Speaker, String> firstNameColumn;
    public TableColumn<Speaker, String> lastNameColumn;
    public TextField speakerCodeField;
    public TextField firstNameField;
    public TextField lastNameField;
    public Button addButton;
    private final ObservableList<Speaker> speakers;
    private TableRow<Speaker> clickedRow;

    public RubricController() {
        this.speakers = FXCollections.observableArrayList();
    }

    public void initialize() {
        this.tableView.setItems(this.speakers);
        this.speakerCodeField.setTextFormatter(new TextFormatter<String>(i -> {
            final String text = i.getText();
            return (text.matches("\\d")) ? i : null;
        }));
        final var removal = new MenuItem("Remove speaker");
        final var creation = new MenuItem("Create channel");
        removal.setOnAction(event -> {
            final var removedSpeaker = this.clickedRow.getItem();
            Starter.getController().removeSpeakerFromRubric(Integer.parseInt(removedSpeaker.getCode()),
                    removedSpeaker.getFirstName(), removedSpeaker.getLastName());
            this.speakers.remove(removedSpeaker);
        });
        creation.setOnAction(event -> {
            final var create = this.clickedRow.getItem();
            Starter.getController().newChannel("SPEECH", create.getFirstName() + " " + create.getLastName(), "");
        });
        final var menu = new ContextMenu(removal, creation);
        this.tableView.setRowFactory(t -> {
            final var row = new TableRow<Speaker>();
            row.setOnMouseClicked(e -> {
                if (!row.isEmpty()) {
                    row.setOnContextMenuRequested(event -> menu.show(row, e.getScreenX(), e.getScreenY()));
                    this.clickedRow = row;
                }
            });
            return row;
        });
    }

    public static class Speaker {

        final StringProperty code;
        final StringProperty firstName;
        final StringProperty lastName;

        public Speaker() {
            this.code = new SimpleStringProperty(this, "code");
            this.firstName = new SimpleStringProperty(this, "firstName");
            this.lastName = new SimpleStringProperty(this, "lastName");
        }

        public Speaker setCode(String code) {
            this.code.set(code);
            return this;
        }

        public Speaker setFirstName(String firstName) {
            this.firstName.set(firstName);
            return this;
        }

        public Speaker setLastName(String lastName) {
            this.lastName.set(lastName);
            return this;
        }

        public String getCode() {
            return this.code.get();
        }

        public String getFirstName() {
            return this.firstName.get();
        }

        public String getLastName() {
            return this.lastName.get();
        }

    }

    public void addPerson() {
        final var speaker = new Speaker().setCode(this.speakerCodeField.getText())
                .setFirstName(this.firstNameField.getText()).setLastName(this.lastNameField.getText());
        if (!Objects.equals(speaker.getCode(), "") &&
                !Objects.equals(speaker.getFirstName(), "") &&
                !Objects.equals(speaker.getLastName(), "")) {
            this.speakers.add(speaker);
            Starter.getController().addSpeakerToRubric(Integer.parseInt(speaker.getCode()), speaker.getFirstName(),
                    speaker.getLastName());
        }
    }

}
