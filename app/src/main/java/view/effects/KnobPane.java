package view.effects;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class KnobPane extends Pane {
	
	private double min;
	private double max;
	private double value = 0;
	final Pane rotation;
	private int tickCount;
	final Label lvalue;

	public KnobPane(final double min, final double max, final int tickCount) {
		this.min = min;
		this.max = max;
		this.tickCount = tickCount;
		
		this.setPrefHeight(150);
		this.setPrefWidth(150);
		
		//Big circle
		final Circle circle = new Circle(40.0);
		circle.setLayoutX(75.0);
		circle.setLayoutY(75.0);
		circle.setFill(Color.WHITE);
		circle.setStroke(Color.BLACK);
		
		//Pane for little circle
		rotation = new Pane();
		rotation.setLayoutX(35);
		rotation.setLayoutY(35);
		rotation.setPrefHeight(80);
		rotation.setPrefWidth(80);
		//Little circle
		final Circle little = new Circle(7.0);
		little.setFill(Color.RED);
		little.setStroke(Color.BLACK);
		little.setLayoutX(25.0);
		little.setLayoutY(62.0);
		
		//Min label
		final Label lmin = new Label("" + min);
		lmin.setLayoutX(30);
		lmin.setLayoutY(105);
		//Max label
		final Label lmax = new Label("" + max);
		lmax.setLayoutX(110);
		lmax.setLayoutY(105);
		//Value label
		lvalue = new Label("" + value);
		lvalue.setLayoutX(68);
		lvalue.setLayoutY(68);
		
		//Tick labels
		for(int i = 0; i < tickCount; i++) {
			setValue(Math.round((max-min)/(tickCount+1)*(i+1)));
			System.out.println(Math.round((max-min)/(tickCount+1)*(i+1)));
			System.out.println(little.getLayoutX()-(getValue()/(max-min)));
			System.out.println(little.getLayoutY()-(getValue()/(max-min)));
			double posx = (getValue());
			double posy = (getValue());
			final Label tick = new Label("" + getValue());
			tick.setLayoutX(posx-0);
			tick.setLayoutY(posy-0);
			this.getChildren().add(tick);
		}
		
		//setValue(25);
		
		rotation.getChildren().addAll(little);
		this.getChildren().addAll(circle, rotation, lmin, lmax, lvalue);
	}
	
	public void setValue(final double value) {
		if(value >= min && value <= max) {
			this.value = value;
			lvalue.setText("" + value);
			rotation.setRotate((270*value)/(max-min));
		}
	}
	
	public double getValue() {
		return this.value;
	}
}
