package view.edit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import view.common.TimeAxisSetter;

public class EditViewController implements Initializable{
	
	private final static String SEP = System.getProperty("file.separator");
	
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
	private TimeAxisSetter timeAxisSetter;
	
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
	
	@FXML
	private HBox fxPanel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//--------------setting time axis------------
		timeAxisSetter = new TimeAxisSetter(TimeAxisSetter.MS_TO_MIN*10); //10 min initial project length
		timelineToChannelsAligner.add(timeAxisSetter.getAxis(), 0, 1);
		timelineToChannelsAligner.add(timeAxisSetter.getNavigator(), 0, 0);
		
		//--------------CHANNEL CONTENT - INFO - TIMELINE SPLIT RESIZE--------------		
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
		//----Audio FX----
		FXMLLoader loader = new FXMLLoader();
		String path = System.getProperty("user.dir") + SEP + "src" + SEP + "main" + SEP + "resources" + SEP + "view" + SEP + "TextEditorView.fxml";
		FileInputStream fxmlStream;
		try {
			fxmlStream = new FileInputStream(path);
			Pane pane = new Pane();
			pane.getChildren().add(loader.load(fxmlStream));
			pane.setMaxWidth(fxPanel.getWidth());
			fxPanel.getChildren().add(pane);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Aggiunta di un canale.
	 */
	@FXML
	public void addChannel() {
		//--------CONTROLLER---------
		//TODO
	}

	public void addClip() {
		//TODO
	}
	
	/**
	 * Update all displayed channels
	 */
	public void updateChannels() {
		//--------INFOS--------------
		Pane newChannelInfos = new ChannelInfoPane("Channel");
		//--------CLIP PANE----------
		ChannelClipsPane newChannel = new ChannelClipsPane(timeAxisSetter.getAxis());	
		//--------CHANNEL PANES HEIGHT LINK------
		newChannelInfos.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
			newChannel.setMinHeight(newChannelInfos.getHeight());
		});
		
		//--------ADDING TO VIEW-----
		channelsInfoPane.getChildren().add(newChannelInfos);
		channelsContentPane.getChildren().add(newChannel);
		
	}
	
	/**
	 * Update all displayed clips
	 */
	public void updateClips() {
		
	}
}