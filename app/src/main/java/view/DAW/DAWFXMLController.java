package view.DAW;

import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DAWFXMLController implements Initializable{
	
	@FXML
	private Label lTimeline;
	
	@FXML
	private Button btnChannel;

	@Override
	public void initialize(java.net.URL location, ResourceBundle resources) {
		String text = "";
        for(int i = 0; i < 20000; i++) {
            if(i == 0) {
                text = "| 0";
            } else {
                text = text + "             " + "| " + i;
            } 
        }
        lTimeline.setText(text);
	}   
}
