package view.edit;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.common.TimeAxisSetter;
import view.common.MarkersPane;

public class EditViewController implements Initializable{
	
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
	 * VBox per contenuto canali
	 */
	@FXML private VBox channelsContentPane;
	/**
	 * VBox per info canali
	 */
	@FXML private VBox channelsInfoPane;
	@FXML private HBox fxPanel;
	@FXML private Button stop;
	@FXML private Button play;
	@FXML private Button pause;
	/**
	 * Asse del tempo.
	 */
	private TimeAxisSetter timeAxisSetter;
	private MarkersPane markersPane;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//playButton
		this.play.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
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
		new EditChannelsView(timeAxisSetter, channelsContentPane, channelsInfoPane);
		//--------------CHANNEL CONTENT - INFO - TIMELINE SPLIT RESIZE--------------		
		channelsInfoResizer.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
			timelineToChannelsAligner.getColumnConstraints().get(1).setPercentWidth((1-(channelsInfoResizer.getDividerPositions()[0]))*100);
		});
	}
	
	/**
	 * Aggiunta di un canale.
	 * @throws IOException 
	 */
	@FXML
	public void addChannel() throws IOException {
		//--------CONTROLLER---------
		//TODO
		 FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("view/newChannelWindow.fxml"));
	        Scene scene = new Scene(loader.load());
	        Stage stage = new Stage();
	        stage.setScene(scene);
	        stage.initModality(Modality.WINDOW_MODAL);
	        stage.setTitle("New Channel");
	        stage.initOwner(rootNode.getScene().getWindow());
	        stage.showAndWait();
	}
	
	public void addClip() {
		//TODO
	}
	
	public void stop() {
		//App.getController().stop();
		this.play.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
	}
	
	public void play() {
		//App.getController().play();
		this.play.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
	}
	
	public void pause() {
		//App.getController().pause();
		this.play.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
	}
}
