package view.edit;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import resplan.Starter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.common.App;
import view.common.MarkersPane;
import view.common.TimeAxisSetter;
import view.common.Tool;
import view.common.ToolBarSetter;
import view.common.WindowBar;

public final class EditViewController implements Initializable {

	private static final int INFO_PANE_MAX_WIDTH = 250;
	@FXML private AnchorPane windowBar;
	@FXML private VBox rootNode;
	
	/**
	 * La griglia usata per dimensionare la timeline alla vista dei canali.
	 */
	@FXML private GridPane timelineToChannelsAligner;
	@FXML private ScrollPane scrollPane;
	
	/**
	 * SplitPanel per dimensionare la divisione tra channel info e channel content.
	 */
	@FXML private SplitPane channelsInfoResizer;	
	
	/**
	 * VBox per contenuto canali.
	 */
	@FXML private VBox channelsContentPane;
	
	/**
	 * VBox per info canali.
	 */
	@FXML private VBox channelsInfoPane;
	@FXML private HBox fxPanel;
	@FXML private Button cursorButton;
	@FXML private Button moveClipsButton;
	@FXML private Button addClipsButton;
	@FXML private Button splitClipsButton;
	@FXML private Button stop;
	@FXML private Button play;
	@FXML private Button pause;
	@FXML private Label playbackTimeLabel;
	/**
	 * Asse del tempo.
	 */
	private TimeAxisSetter timeAxisSetter;
	private MarkersPane markersPane;
	private final ToolBarSetter toolBarSetter = new ToolBarSetter();
	
	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		//set buttons and tools
		this.play.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		this.toolBarSetter.addTool(Tool.CURSOR, cursorButton)
							.addTool(Tool.MOVE, moveClipsButton)
							.addTool(Tool.ADDCLIPS, addClipsButton)
							.addTool(Tool.SPLIT, splitClipsButton);
		this.cursorToolSelected();
		//--------------setting time axis------------
		timeAxisSetter = new TimeAxisSetter(App.getData().getProjectLenghtProperty().get());
		GridPane.setMargin(timeAxisSetter.getAxis(), new Insets(0, 16, 0, 0));
		timelineToChannelsAligner.add(timeAxisSetter.getAxis(), 0, 2);
		timelineToChannelsAligner.add(timeAxisSetter.getNavigator(), 0, 0);
		//--------set markers pane---------
		markersPane = new MarkersPane(timeAxisSetter.getAxis());
		timelineToChannelsAligner.add(markersPane, 0, 1, 1, 3);
		GridPane.setVgrow(markersPane, Priority.ALWAYS);
		//--------set channels view----------
		new FXView(fxPanel, new EditChannelsView(timeAxisSetter, channelsContentPane, channelsInfoPane, toolBarSetter));
		channelsInfoPane.setMaxWidth(INFO_PANE_MAX_WIDTH);
		//--------------CHANNEL CONTENT - INFO - TIMELINE SPLIT RESIZE--------------
		channelsInfoResizer.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
			timelineToChannelsAligner.getColumnConstraints().get(1).setPercentWidth((1 - (channelsInfoResizer.getDividerPositions()[0])) * 100);
		});
		//-------PLAYBACK TIME SET-----
		channelsContentPane.setOnMouseClicked(e -> {
			if (Starter.getController().isPaused() && toolBarSetter.getCurrentTool().equals(Tool.CURSOR)) {
				Starter.getController().setPlaybackTime(timeAxisSetter.getAxis().getValueForDisplay(e.getX()).doubleValue());
			}
		});
		this.setPlaybackMarkerPosition(0);
		new WindowBar(this.windowBar);
	}
	
	/**
	 * Aggiunta di un canale.
	 * @throws IOException 
	 */
	@FXML
	public void addChannel() throws IOException {
		//--------CONTROLLER---------
		 FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("view/NewChannelWindow.fxml"));
	        Scene scene = new Scene(loader.load());
	        Stage stage = new Stage();
	        stage.setScene(scene);
	        stage.initModality(Modality.WINDOW_MODAL);
	        stage.setTitle("New Channel");
	        stage.initOwner(rootNode.getScene().getWindow());
	        stage.showAndWait();
	}
	
	//tools
	@FXML
	public void cursorToolSelected() {
		toolBarSetter.selectTool(Tool.CURSOR);
	}
	
	@FXML
	public void moveToolSelected() {
		toolBarSetter.selectTool(Tool.MOVE);
	}
	
	@FXML
	public void addClipsToolSelected() {
		toolBarSetter.selectTool(Tool.ADDCLIPS);
	}
	
	@FXML
	public void splitClipsToolSelected() {
		toolBarSetter.selectTool(Tool.SPLIT);
	}
	
	//engine
	@FXML
	public void stop() {
		Starter.getController().stop();
		setPlaybackMarkerPosition(Starter.getController().getPlaybackTime());
		this.play.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
	}
	
	@FXML
	public void play() {
		Starter.getController().start();
		this.play.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
	}
	
	@FXML
	public void pause() {
		Starter.getController().pause();
		this.play.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
	}
	
	public void setPlaybackMarkerPosition(final double time) {
		Platform.runLater(() -> {
			this.markersPane.updatePlaybackMarker(time);
			this.playbackTimeLabel.setText(timeAxisSetter.getAxis().getTickLabelFormatter().toString(time));
		});
	}
}
