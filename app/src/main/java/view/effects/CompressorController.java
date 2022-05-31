package view.effects;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class CompressorController {
	
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
		if(value >= -100 && value <= 0) {
			compressor.setProgress(value);
			lcurrent.setText("" + value);
		} else if (value < -100) {
			compressor.setProgress(-100.0);
			lcurrent.setText("-inf");
		}
	}
	
}
