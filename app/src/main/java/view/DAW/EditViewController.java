package view.DAW;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class EditViewController implements Initializable{
	
	private Map<Pane, String> channels = new HashMap<>(); 
	
	Map<Double, Region> clips = new HashMap<>();
	
	/**
	 * La griglia usata per dimensionare la timeline alla vista dei canali.
	 */
	@FXML
	private GridPane timelineToChannelsAligner;
	
	/**
	 * SplitPanel per dimensionare la divisione tra channel info e channel content.
	 */
	@FXML
	private SplitPane channelsInfoResizer;		
	/**
	 * Asse del tempo.
	 */
	@FXML
	private NumberAxis timeAxis;
	
	/**
	 * VBox per contenuto canali
	 */
	@FXML
	private VBox channelsContentPane;
		
	/**
	 * VBox per info canali
	 */
	@FXML
	private VBox channelsInfoPane;
	
	/**
	 * Aggiungi canale bottone
	 */
	@FXML
	private MenuItem addChannel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//channel content - info - timeline grid resize
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
	
	/**
	 * Aggiunta di un canale.
	 */
	@FXML
	public void addChannel() {
		//comunica al controller che vuoi creare un canale.
		
		//infos
		Pane newChannelInfos = new Pane();
		newChannelInfos.setBorder(new Border(new BorderStroke(null, null, Paint.valueOf("#999999"), null, 
				null, null, BorderStrokeStyle.SOLID, null, CornerRadii.EMPTY, null, null)));
		DragResizer.makeResizable(newChannelInfos);
		channelsInfoPane.getChildren().add(newChannelInfos);
		
		newChannelInfos.getChildren().add(new Label("Channel"));
		//clip panel
		Pane newChannel = new Pane();
		newChannel.setBorder(new Border(new BorderStroke(null, null, Paint.valueOf("#999999"), null, 
				null, null, BorderStrokeStyle.SOLID, null, CornerRadii.EMPTY, null, null)));
		newChannelInfos.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
			newChannel.setMinHeight(newChannelInfos.getHeight());
		});
		channelsContentPane.getChildren().add(newChannel);	
	}
}
