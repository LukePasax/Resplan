package view.effectstry;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class CompressorController implements Initializable {
	
	private final static double lowerbound = -100.0;
	private final static double upperbound = 0.0;
	
	@FXML
	Label lcurrent;
	@FXML
	TextField fvalue;
	@FXML
	ProgressBar compressor;
	
	private double value;

	@FXML
	public void refresh() {
		value = Double.parseDouble(fvalue.getText());
		if(value >= lowerbound && value <= upperbound) {
			compressor.setProgress(-value/(-lowerbound));
			lcurrent.setText("" + value);
		} else if (value < lowerbound) {
			compressor.setProgress(-lowerbound);
			lcurrent.setText("-inf");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		compressor.setProgress(upperbound);
		
	}
	
}
