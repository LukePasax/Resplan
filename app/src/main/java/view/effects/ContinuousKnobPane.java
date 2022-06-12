package view.effects;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ContinuousKnobPane extends Pane {
	
	private Double min;
	private Double max;
	private Double value = 0.0;
	final Pane rotation;
	final Label lvalue;
	private final static double correctionx = 25;
	private final static double correctiony = correctionx/1.5;
	private final static double size = 150.0;
	private final static double half = size/2;
	private final static double radius = 40.0;
	
	private double initialValue = 0.0;
	private double current = 0.0;
	

	public ContinuousKnobPane(final Double min, final Double max, final int tickCount, final String name) {
		if(Double.compare(min, max) >= 0) {
			throw new IllegalArgumentException();
		}
		this.min = min;
		this.max = max;
		double currentangle = min;
		
		this.setPrefHeight(size);
		this.setPrefWidth(size);
		
		//Big circle
		final Circle circle = new Circle(radius);
		circle.setLayoutX(half);
		circle.setLayoutY(half);
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
		lmin.setStyle("-fx-font-weight: bold;");
		//Max label
		final Label lmax = new Label("" + max);
		lmax.setLayoutX(110);
		lmax.setLayoutY(105);
		lmax.setStyle("-fx-font-weight: bold;");
		//Value label
		lvalue = new Label("" + value);
		lvalue.setLayoutX(55);
		lvalue.setLayoutY(half-10);
		lvalue.setPrefWidth(radius);
		lvalue.setAlignment(Pos.CENTER);
		
		rotation.getChildren().addAll(little);
		this.getChildren().addAll(circle, rotation, lmin, lmax, lvalue);
		
		if(!min.equals(Double.NEGATIVE_INFINITY) && !max.equals(Double.POSITIVE_INFINITY)) {
			//Tick labels
			for(int i = 0; i < tickCount; i++) {
				double unit = Math.round((max-min)/(tickCount+1));
				currentangle += unit;
				var angle = setValue(currentangle);
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
				
				if(label.getLayoutX() < half && label.getLayoutY() >= half) {
					label.setLayoutX(label.getLayoutX()+correctionx);
					label.setLayoutY(label.getLayoutY()+correctiony);
				} else if(label.getLayoutX() < half && label.getLayoutY() < half) {
					label.setLayoutX(label.getLayoutX()-correctionx);
					label.setLayoutY(label.getLayoutY()+correctiony);
				} else if(label.getLayoutX() >= half && label.getLayoutY() < half) {
					label.setLayoutX(label.getLayoutX()-correctionx);
					label.setLayoutY(label.getLayoutY()+correctiony);
				} else if(label.getLayoutX() >= half && label.getLayoutY() >= half) {
					label.setLayoutX(label.getLayoutX()-correctionx);
					label.setLayoutY(label.getLayoutY()-correctiony);
				}
				
				this.getChildren().add(pane);
			}
		}

		setValue(min);
		
		//Drag and drop
		this.setOnMousePressed(this::mousePressed);
		this.setOnMouseDragged(this::mouseDragged);
		this.setOnMouseReleased(this::mouseReleased);
		
		//Name label
		final Label lname = new Label(name);
		lname.setLayoutY(130);
		lname.setPrefWidth(size);
		lname.setAlignment(Pos.CENTER);
		this.getChildren().add(lname);
	}
	
	public double setValue(double value) {
		double minangle;
		double newangle = 0.0;
		value = (double)Math.round(value*100)/100;
		if(min < 0) {
			minangle = (270*-min)/(max-min);
		} else {
			minangle = (270*min)/(max-min);
		}
		if(min.equals(Double.NEGATIVE_INFINITY)) {
			if(value <= max) {
				min = -500.0;
				this.value = value;
				lvalue.setText("" + value);
			}
		}
		else if(max.equals(Double.POSITIVE_INFINITY)) {
			if(value >= min) {
				max = 500.0;
				this.value = value;
				lvalue.setText("" + value);
			}
		} else {
			if(value >= min && value <= max) {			
				this.value = value;
				lvalue.setText("" + value);
				newangle = minangle + ((270*value)/(max-min));
				rotation.setRotate(newangle);
			}
		}
		return newangle;
	}
	
	public double getValue() {
		return this.value;
	}
	
	private void mousePressed(MouseEvent e) {
		initialValue = e.getSceneY();
		current = getValue();
	}
	
	private void mouseDragged(MouseEvent e) {
		setValue(current + (((-e.getSceneY())+initialValue)*((max-min)/100)));
	}
	
	private void mouseReleased(MouseEvent e) {
		initialValue = 0.0;
	}
}
