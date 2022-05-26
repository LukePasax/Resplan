package view.edit;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.common.ChannelsView;
import view.common.TimeAxisSetter;

public class EditViewController implements Initializable{
	
	@FXML
	private VBox rootNode;
	
	/**
	 * La griglia usata per dimensionare la timeline alla vista dei canali.
	 */
	@FXML
	private GridPane timelineToChannelsAligner;
	
	@FXML
	private ScrollPane scrollPane;
	
	/**
	 * SplitPanel per dimensionare la divisione tra channel info e channel content.
	 */
	@FXML
	private SplitPane channelsInfoResizer;	
	
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
	 * Asse del tempo.
	 */
	private TimeAxisSetter timeAxisSetter;

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
		timelineToChannelsAligner.add(timeAxisSetter.getAxis(), 0, 2);
		timelineToChannelsAligner.add(timeAxisSetter.getNavigator(), 0, 0);
		timelineToChannelsAligner.setMaxWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.8);
		//--------set channels view----------
		new ChannelsView(timeAxisSetter, channelsContentPane, channelsInfoPane);
		
		//--------------CHANNEL CONTENT - INFO - TIMELINE SPLIT RESIZE--------------		
		channelsInfoResizer.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
			((GridPane)scrollPane.getParent()).getColumnConstraints().get(1).setPercentWidth((1-(channelsInfoResizer.getDividerPositions()[0]))*100);
		});
		//----Audio FX----
		/*FXMLLoader loader = new FXMLLoader();
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
		}*/
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
}
