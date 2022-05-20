package controller.view.planning;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class PlanningController {

    public MenuBar menuBar;
    public Menu fileMenu;
    public MenuItem newFile;
    public MenuItem openFile;
    public MenuItem closeFile;
    public SplitPane mainPanel;
    public VBox commandBox;
    public Button newChannelButton;
    public Button newClipButton;
    public ScrollPane channelScrollPane;
    public SplitPane channelPane;
    public AnchorPane timelineRuler;
    public VBox infoContainer;

    public void initialize() {
    }

    public void newChannelPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("view/newChannelWindow.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("New Channel");
        stage.initOwner(menuBar.getScene().getWindow());
        stage.showAndWait();
    }

    public void newClipPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("view/newClipWindow.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("New Clip");
        stage.initOwner(menuBar.getScene().getWindow());
        stage.showAndWait();
    }

    public void addChannel(String type, String title, String description) {
        AnchorPane channel = new AnchorPane();
        channel.setMinHeight(40);
        channel.setPrefSize(160,100);
        Label channelTitle = new Label(title);
        this.infoContainer.getChildren().add(channelTitle);
        this.channelPane.getItems().add(channel);
    }
}
