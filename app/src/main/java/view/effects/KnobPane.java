package view.effects;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
	private final static double correctionx = 25;
	private final static double correctiony = correctionx/1.5;

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
		
		rotation.getChildren().addAll(little);
		this.getChildren().addAll(circle, rotation, lmin, lmax, lvalue);
		
		//Tick labels
		for(int i = 0; i < tickCount; i++) {
			setValue(Math.round((max-min)/(tickCount+1)*(i+1)));
			double angle = rotation.getRotate();
			Pane pane = new Pane();
			pane.setLayoutX(35);
			pane.setLayoutY(35);
			pane.setPrefHeight(80);
			pane.setPrefWidth(80);
			Label label = new Label("" + getValue());
			label.setLayoutX(25.0);
			label.setLayoutY(62.0);
			pane.getChildren().add(label);
			pane.setRotate(angle);
			label.setRotate(360-angle);
			
			if(label.getLayoutX() < 75 && label.getLayoutY() >= 75) {
				label.setLayoutX(label.getLayoutX()+correctionx);
				label.setLayoutY(label.getLayoutY()+correctiony);
			} else if(label.getLayoutX() < 75 && label.getLayoutY() < 75) {
				label.setLayoutX(label.getLayoutX()-correctionx);
				label.setLayoutY(label.getLayoutY()+correctiony);
			} else if(label.getLayoutX() >= 75 && label.getLayoutY() < 75) {
				label.setLayoutX(label.getLayoutX()-correctionx);
				label.setLayoutY(label.getLayoutY()+correctiony);
			} else if(label.getLayoutX() >= 75 && label.getLayoutY() >= 75) {
				label.setLayoutX(label.getLayoutX()-correctionx);
				label.setLayoutY(label.getLayoutY()-correctiony);
			}
			
			this.getChildren().add(pane);
		}

		setValue(0);
		
		//Drag and drop
		rotation.setOnDragEntered(this::dragEntered);
		
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
	
	public void dragEntered(MouseEvent e) {
		
	}
}
