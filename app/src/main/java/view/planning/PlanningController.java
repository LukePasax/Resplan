package view.planning;

import Resplan.App;
import controller.general.DownloadingException;
import controller.general.JsonFilePicker;
import controller.general.LoadingException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import view.common.ChannelsView;
import view.common.TimeAxisSetter;
import static javafx.scene.control.Alert.AlertType.ERROR;

import java.io.File;
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
    public MenuItem setTemplate;
    public MenuItem resetTemplate;
    public Button delChannelButton;
    public Button delClipButton;
    public Button launchDawButton;
    private TimeAxisSetter timeAxisSetter;
    private JsonFilePicker filePicker;
    private ChannelsView channelsView;

    public void initialize() {
        timeAxisSetter = new TimeAxisSetter(TimeAxisSetter.MS_TO_MIN*10); //10 min initial project length
        timelineToChannelsAligner.add(timeAxisSetter.getAxis(), 0, 1);
        timelineToChannelsAligner.setPadding(new Insets(0,0,0,20));
        timelineToChannelsAligner.add(timeAxisSetter.getNavigator(), 0, 0);
        channelsInfoResizer.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
            timelineToChannelsAligner.getColumnConstraints().get(1).setPercentWidth((1-(channelsInfoResizer.getDividerPositions()[0]))*100);
        });
        this.channelsView = new ChannelsView(timeAxisSetter, channelsContentPane, channelsInfoPane);
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
            Alert error = new Alert(ERROR);
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

    }

    public void addClip(String title, String description, String channel, Double time) {
    }

    public void newProjectPressed(ActionEvent event) {

    }

    public void openProjectPressed(ActionEvent event) {
        this.filePicker = new JsonFilePicker();
        this.saveProject();
        try {
            App.getController().openProject(this.filePicker.getFileChooser().showOpenDialog(this.menuBar.getScene().getWindow()));
        } catch (LoadingException e) {
            Alert error = new Alert(ERROR);
            error.setContentText(e.getLocalizedMessage());
            error.setTitle("Error");
        }
    }

    private void saveProject() {
        try {
            App.getController().save();
        } catch (DownloadingException e) {
            Alert error = new Alert(ERROR);
            error.setContentText(e.getLocalizedMessage());
            error.setTitle("Error");
        } catch (IllegalStateException e) {
            this.filePicker =  new JsonFilePicker();
            try {
                App.getController().saveWithName(this.filePicker.getFileChooser().showSaveDialog(this.menuBar.getScene().getWindow()));
            } catch (DownloadingException ex) {
                Alert error = new Alert(ERROR);
                error.setContentText(e.getLocalizedMessage());
                error.setTitle("Error");
            }
        }
    }

    public void closeProjectPressed(ActionEvent event) {
        this.menuBar.getScene().getWindow().hide();
    }

    public void setTemplatePressed(ActionEvent event) {
        try {
            App.getController().setTemplateProject();
        } catch (DownloadingException | IllegalStateException e) {
            Alert error = new Alert(ERROR);
            error.setContentText(e.getLocalizedMessage());
            error.setTitle("Error");
        }
    }

    public void resetTemplatePressed(ActionEvent event) {
    }

    public void delChannelPressed(ActionEvent event) {
    }

    public void delClipPressed(ActionEvent event) {
    }

    public void launchDawPressed(ActionEvent event) {
    }

    public void saveProjectPressed(ActionEvent event) {
        this.saveProject();
    }
}
