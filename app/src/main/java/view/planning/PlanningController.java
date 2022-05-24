package view.planning;

import Resplan.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.daw.ChannelClipsPane;
import view.daw.ChannelInfoPane;
import view.daw.TimeAxisSetter;

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
    public GridPane timelineToChannelsAligner;
    public SplitPane channelsInfoResizer;
    public VBox channelsContentPane;
    public VBox channelsInfoPane;
    private TimeAxisSetter timeAxisSetter;


    public void initialize() {
        timeAxisSetter = new TimeAxisSetter(TimeAxisSetter.MS_TO_MIN*10); //10 min initial project length
        timelineToChannelsAligner.add(timeAxisSetter.getAxis(), 0, 1);
        timelineToChannelsAligner.setPadding(new Insets(0,0,0,20));
        timelineToChannelsAligner.add(timeAxisSetter.getNavigator(), 0, 0);
        channelsInfoResizer.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
            timelineToChannelsAligner.getColumnConstraints().get(1).setPercentWidth((1-(channelsInfoResizer.getDividerPositions()[0]))*100);
        });
		
		/*
		timeAxis.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
            if(!needsLayout) {
                clips.forEach((num, point) -> {
                    double posX = timeAxis.getDisplayPosition(num);
                    point.setLayoutX(posX);
                    point.setLayoutY(50D);
                });
            }
        });*/
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
        if (App.getController().getChannelList().isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setContentText("No channels present");
            error.showAndWait();
        } else {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("view/newClipWindow.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("New Clip");
            stage.initOwner(menuBar.getScene().getWindow());
            stage.showAndWait();
        }
    }

    public void addChannel(String type, String title, String description) {
        Pane newChannelInfos = new ChannelInfoPane(title);
        ChannelClipsPane newChannel = new ChannelClipsPane(timeAxisSetter.getAxis());
        newChannelInfos.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
            newChannel.setMinHeight(newChannelInfos.getHeight());
        });
        channelsInfoPane.getChildren().add(newChannelInfos);
        channelsContentPane.getChildren().add(newChannel);
    }

    public void addClip(String title, String description, String channel, Double time) {
    }
}
