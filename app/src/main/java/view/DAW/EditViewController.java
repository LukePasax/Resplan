package view.DAW;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class EditViewController {
	
	@FXML
	private SplitPane channelsPane;
	
	@FXML
	private MenuItem addChannel;
	
	@FXML
	public void addChannel() {
		channelsPane.getItems().add(new AnchorPane());
	}
	
}
