package view.effects;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class KnobController {
	
	final private static double ONEQUARTER = 0.25;
	final private static double HALF = 0.5;
	final private static double THREEQUARTER = 0.75;
	final private static int VOLUME = 100;
	final private static int DEGREE = 360;

	
	@FXML
	Label minmax;
	@FXML
	Label onequarter;
	@FXML
	Label half;
	@FXML
	Label threequarter;
	@FXML
	Label value;
	
	@FXML
	Pane rotator;
	
	private int val;
	private int min;
	private int max;
	
	public void init(final int min, final int max) {
		this.min = min;
		this.max = max;
		
		minmax.setText(min + " / " + max);
		onequarter.setText("" + (int) ((min + max) * ONEQUARTER));
		half.setText("" + (int) ((min + max) * HALF));
		threequarter.setText("" + (int) ((min + max) * THREEQUARTER));
	}
	
	public void rotate(final int volume) {
		if(volume >= min && volume <= max) {
			val = volume;
			rotator.setRotate(val * DEGREE / VOLUME);
			value.setText("" + val);
		}
	}
}
