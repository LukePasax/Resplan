package view.planning;

import resplan.Starter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import view.common.AlertDispatcher;

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
        this.initializeSpeakers();
        this.tableView.setItems(this.speakers);
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
            try {
                Starter.getController().newChannel("SPEECH", create.getFirstName() + " " + create.getLastName(), "");
            } catch (IllegalArgumentException e) {
                AlertDispatcher.dispatchError(e.getMessage());
            }
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

    private void initializeSpeakers() {
        Starter.getController().getSpeakers().forEach(speaker -> {
            final var viewSpeaker = new Speaker();
            viewSpeaker.setCode(String.valueOf(speaker.getSpeakerCode()));
            viewSpeaker.setFirstName(speaker.getFirstName());
            viewSpeaker.setLastName(speaker.getLastName());
            this.speakers.add(viewSpeaker);
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

        public void setCode(final String code) {
            this.code.set(code);
        }

        public void setFirstName(final String firstName) {
            this.firstName.set(firstName);
        }

        public void setLastName(final String lastName) {
            this.lastName.set(lastName);
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
        final var speaker = new Speaker();
        speaker.setCode(this.speakerCodeField.getText());
        speaker.setFirstName(this.firstNameField.getText());
        speaker.setLastName(this.lastNameField.getText());
        if (!Objects.equals(speaker.getCode(), "")
                && !Objects.equals(speaker.getFirstName(), "")
                && !Objects.equals(speaker.getLastName(), "")
                && this.checkIntegrity(speaker.getCode())) {
            try {
                Starter.getController().addSpeakerToRubric(Integer.parseInt(speaker.getCode()), speaker.getFirstName(),
                        speaker.getLastName());
                this.speakers.add(speaker);
                this.speakerCodeField.clear();
                this.firstNameField.clear();
                this.lastNameField.clear();
            } catch (NumberFormatException e) {
                AlertDispatcher.dispatchError("Speaker code must be an integer.");
            }
        }
    }

    private boolean checkIntegrity(String code) {
        return this.speakers.stream().noneMatch(i -> i.getCode().equals(code));
    }

}
