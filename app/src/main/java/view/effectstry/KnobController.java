package view.effectstry;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import view.effectstry.Knob.KnobType;

public class KnobController {
	
	final private static double ONEQUARTER = 0.25;
	final private static double HALF = 0.5;
	final private static double THREEQUARTER = 0.75;
	final private static double DEGREE = 360;

	
	@FXML
	Label minmax;
	@FXML
	Label onequarter;
	@FXML
	Label half;
	@FXML
	Label threequarter;
	@FXML
	Label lvalue;
	
	@FXML
	Label lname;
	
	@FXML
	Pane rotator;
	
	private double min;
	private double max;
	
	public void init(final double min, final double max, final String name, final KnobType type) {
		this.min = min;
		this.max = max;
		
		if(type.equals(KnobType.RATIO)) {
			minmax.setText("1:1");
			lvalue.setText("1:1");
		} else {
			if(min == Double.NEGATIVE_INFINITY) {
				minmax.setText("-inf / " + max);
			} else {
				minmax.setText(min + " / " + max);
			}
		}
		
		if(type.equals(KnobType.VOLUME)) {
			onequarter.setText("" + ((min + max) * ONEQUARTER));
			half.setText("" + ((min + max) * HALF));
			threequarter.setText("" + ((min + max) * THREEQUARTER));
		}
		if(type.equals(KnobType.RATIO)) {
			onequarter.setText("2:1");
			half.setText("4:1");
			threequarter.setText("8:1");
		}
		
		lname.setText(name);
	}
	
	public void rotate(final double value) {
		if(value >= min && value <= max) {
			rotator.setRotate(value * DEGREE / (min + max));
			lvalue.setText("" + value);
		}
	}
	
	public void setRatio(final String ratio) {
		switch (ratio) {
		case "1:1":
			rotator.setRotate(0);
			lvalue.setText("1:1");
			break;
		case "2:1":
			rotator.setRotate(90);
			lvalue.setText("2:1");
			break;
		case "4:1":
			rotator.setRotate(180);
			lvalue.setText("4:1");
			break;
		case "8:1":
			rotator.setRotate(270);
			lvalue.setText("8:1");
			break;
		default:
			rotator.setRotate(0);
			lvalue.setText("1:1");
			break;
		}
	}
}
