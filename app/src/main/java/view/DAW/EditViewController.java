package view.DAW;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.*;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class EditViewController implements Initializable{
	
	private Map<Pane, String> channels = new HashMap<>(); 
	
	Map<Double, Region> clips = new HashMap<>();
			
	@FXML
	private NumberAxis timeAxis;
	
	@FXML
	private SplitPane channelsPane;
	
	@FXML
	private SplitPane channelsLabelsPane;
	
	@FXML
	private MenuItem addChannel;
	
	@FXML
	public void addChannel() {
		Pane newChannel = new Pane();
		newChannel.setMinHeight(30);
		newChannel.setMaxHeight(140);
		channels.put(newChannel, "Channel");
		
		channelsPane.getItems().add(newChannel);
		
		Pane newLabel = new Pane();
		newLabel.setMinHeight(30);
		newLabel.setMaxHeight(140);
		newLabel.getChildren().add(new Label("Channel"));
		channelsLabelsPane.getItems().add(newLabel);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//resize channels labels pane with channels pane
		channelsPane.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
			if(!needsLayout) {
				int i = 0;
				for(Double d : channelsPane.getDividerPositions()) {
					channelsLabelsPane.setDividerPosition(i, d);
					i++;
				}	
			}
		});
		
		//disable resize for channels labels pane
		channelsLabelsPane.setDisable(true);
		
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
	
}
