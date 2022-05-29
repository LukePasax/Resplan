package view.common;

import javafx.scene.control.Alert;

/**
 * This class is used to dispatch {@link Alert}
 */
public class AlertDispatcher {

    /**
     * This method dispatches an {@link Alert} of the {@link javafx.scene.control.Alert.AlertType} ERROR with the given
     * message
     * @param message the message that wants to be displayed
     */
    static public void dispatchError(String message) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setContentText(message);
        error.setTitle("Error");
        error.showAndWait();
    }
}
