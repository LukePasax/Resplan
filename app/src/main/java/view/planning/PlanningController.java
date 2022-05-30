package view.planning;

import Resplan.Starter;
import controller.general.DownloadingException;
import controller.general.LoadingException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.common.AlertDispatcher;
import view.common.JsonFilePicker;
import view.common.MarkersPane;
import view.common.TimeAxisSetter;

import java.awt.Toolkit;
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
    public Button launchEditViewButton;
    private TimeAxisSetter timeAxisSetter;
    private JsonFilePicker filePicker;
    private MarkersPane markersPane;

    public void initialize() {
    	//--------------setting time axis------------
		timeAxisSetter = new TimeAxisSetter(TimeAxisSetter.MS_TO_MIN*10); //10 min initial project length
		GridPane.setMargin(timeAxisSetter.getAxis(), new Insets(0, 5, 0, 0));
		timelineToChannelsAligner.add(timeAxisSetter.getAxis(), 0, 2);
		timelineToChannelsAligner.add(timeAxisSetter.getNavigator(), 0, 0);
		timelineToChannelsAligner.setMaxWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.8);
		//--------set markers pane---------
		markersPane = new MarkersPane(timeAxisSetter.getAxis());
		timelineToChannelsAligner.add(markersPane, 0, 1, 1, 3);
		GridPane.setVgrow(markersPane, Priority.ALWAYS);
		//--------set channels view----------
        new PlanningChannelsView(timeAxisSetter, channelsContentPane, channelsInfoPane);
      //--------------CHANNEL CONTENT - INFO - TIMELINE SPLIT RESIZE--------------		
  		channelsInfoResizer.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
  			timelineToChannelsAligner.getColumnConstraints().get(1).setPercentWidth((1-(channelsInfoResizer.getDividerPositions()[0]))*100);
  		});
    }

    private void switchScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditView.fxml"));
        try {
            menuBar.getScene().setRoot(loader.load());
        } catch (IOException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void newChannelPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("view/NewChannelWindow.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("New Channel");
        stage.initOwner(menuBar.getScene().getWindow());
        stage.showAndWait();
    }

    public void newClipPressed(ActionEvent event) throws IOException {
        if (Starter.getController().getChannelList().isEmpty()) {
            AlertDispatcher.dispatchError("No channels present");
        } else {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("view/NewClipWindow.fxml"));
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
            Starter.getController().openProject(this.filePicker.getFileChooser().showOpenDialog(this.menuBar.getScene().getWindow()));
        } catch (LoadingException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    private void saveProject() {
        try {
            Starter.getController().save();
        } catch (DownloadingException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        } catch (IllegalStateException e) {
            this.filePicker =  new JsonFilePicker();
            try {
                Starter.getController().saveWithName(this.filePicker.getFileChooser().showSaveDialog(this.menuBar.getScene().getWindow()));
            } catch (DownloadingException ex) {
                AlertDispatcher.dispatchError(e.getLocalizedMessage());
            }
        }
    }

    public void closeProjectPressed(ActionEvent event) {
        this.menuBar.getScene().getWindow().hide();
    }

    public void setTemplatePressed(ActionEvent event) {
        try {
            Starter.getController().setTemplateProject();
        } catch (DownloadingException | IllegalStateException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void resetTemplatePressed(ActionEvent event) {

    }

    public void delChannelPressed(ActionEvent event) {
    }

    public void delClipPressed(ActionEvent event) {
    }

    public void launchEditViewPressed(ActionEvent event) {
        this.switchScene();
    }
    public void saveProjectPressed(ActionEvent event) {
        this.saveProject();
    }
}
