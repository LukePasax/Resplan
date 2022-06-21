package view.planning;

import resplan.Starter;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.common.*;
import java.io.IOException;

public class PlanningController {
    public SplitPane mainPanel;
    public VBox commandBox;
    public Button newChannelButton;
    public Button newClipButton;
    public GridPane timelineToChannelsAligner;
    public SplitPane channelsInfoResizer;
    public VBox channelsContentPane;
    public VBox channelsInfoPane;
    public Button newSectionButton;
    public AnchorPane windowBar;
    public Button rubricButton;
    public Button magicButton;
    private TimeAxisSetter timeAxisSetter;
    private final ToolBarSetter toolBarSetter = new ToolBarSetter();

    public void initialize() {
        this.magicButton.setVisible(false);
        this.toolBarSetter.addTool(Tool.MOVE, this.magicButton);
        this.toolBarSetter.selectTool(Tool.MOVE);
    	//--------------setting time axis------------
    	timeAxisSetter = new TimeAxisSetter(App.getData().getProjectLenghtProperty().get());
		App.getData().getProjectLenghtProperty().addListener((obs,old,n)->{
			timeAxisSetter.setProjectLength(n.doubleValue());
		});
		GridPane.setMargin(timeAxisSetter.getAxis(), new Insets(0, 16, 0, 0));
		timelineToChannelsAligner.add(timeAxisSetter.getAxis(), 0, 2);
		timelineToChannelsAligner.add(timeAxisSetter.getNavigator(), 0, 0);
		//--------set markers pane---------
        final MarkersPane markersPane = new MarkersPane(timeAxisSetter.getAxis());
		timelineToChannelsAligner.add(markersPane, 0, 1, 1, 3);
		GridPane.setVgrow(markersPane, Priority.ALWAYS);
		//--------set channels view----------
        new PlanningChannelsView(timeAxisSetter, channelsContentPane, channelsInfoPane, toolBarSetter);
        this.channelsInfoPane.setMaxWidth(200);
      //--------------CHANNEL CONTENT - INFO - TIMELINE SPLIT RESIZE--------------		
  		channelsInfoResizer.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
  			timelineToChannelsAligner.getColumnConstraints().get(1)
                    .setPercentWidth((1 - (channelsInfoResizer.getDividerPositions()[0]))*100);
  		});
          new WindowBar(this.windowBar);
    }

    public void newChannelPressed() throws IOException {
        this.launchWindow("view/NewChannelWindow.fxml","New Channel");
    }

    public void newClipPressed() throws IOException {
        if (Starter.getController().getChannelList().isEmpty()) {
            AlertDispatcher.dispatchError("No channels present");
        } else {
            this.launchWindow("view/NewClipWindow.fxml","New Clip");
        }
    }

    public void newSectionPressed() throws IOException {
        this.launchWindow("view/NewSectionWindow.fxml","New Section");
    }

    public void goToRubricPressed() throws IOException {
        this.launchWindow("view/RubricView.fxml", "Rubric");
    }

    private void launchWindow(final String fxml, final String title) throws IOException {
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource(fxml));
        final Scene scene = new Scene(loader.load());
        final Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.initOwner(this.windowBar.getScene().getWindow());
        stage.setResizable(false);
        stage.showAndWait();
    }

}
