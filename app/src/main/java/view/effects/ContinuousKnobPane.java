package view.effects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public final class ContinuousKnobPane extends Pane {
	
	private Double min;
	private Double max;
	private Double value = 0.0;
	private final Pane rotation;
	private final Label lvalue;
	private final static double correctionx = 18;
	private final static double correctiony = correctionx/1.5;
	private final static double size = 80.0;
	private final static double half = size/2;
	private final static double radius = 25.0;
	
	private double initialValue = 0.0;
	private double current = 0.0;
	

	public ContinuousKnobPane(final Double min, final Double max, final double current, final int tickCount, final String name) {
		if(Double.compare(min, max) >= 0) {
			throw new IllegalArgumentException();
		}
		double currentangle = min;
		
		this.setPadding(new Insets(45));
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
		rotation.setLayoutX(15);
		rotation.setLayoutY(15);
		rotation.setPrefHeight(50);
		rotation.setPrefWidth(50);
		//Little circle
		final Circle little = new Circle(4.0);
		little.setFill(Color.RED);
		little.setStroke(Color.BLACK);
		little.setLayoutX(15.0);
		little.setLayoutY(35.0);
		
		//Min label
		final Label lmin = new Label();
		if(min.equals(Double.NEGATIVE_INFINITY)) {
			lmin.setText("-∞");
			lmin.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
			this.min = 0.0;
			this.max = 1.0;
		} else {
			lmin.setText("" + min);
			lmin.setStyle("-fx-font-weight: bold; -fx-font-size: 11;");
			this.min = min;
			this.max = max;
		}
		lmin.setLayoutX(8.0);
		lmin.setLayoutY(56.0);
		//Max label
		final Label lmax = new Label("" + max);
		lmax.setText("" + max);
		lmax.setStyle("-fx-font-weight: bold; -fx-font-size: 11;");
		lmax.setLayoutX(56.0);
		lmax.setLayoutY(56.0);
		//Value label
		lvalue = new Label("" + value);
		lvalue.setLayoutX(15);
		lvalue.setLayoutY(half-10);
		lvalue.setPrefWidth(radius*2);
		lvalue.setAlignment(Pos.CENTER);
		lvalue.setStyle("-fx-font-weight: bold; -fx-font-size: 11;");
		
		rotation.getChildren().addAll(little);
		this.getChildren().addAll(circle, rotation, lmin, lmax, lvalue);
		
		if(!min.equals(Double.NEGATIVE_INFINITY) && !max.equals(Double.POSITIVE_INFINITY)) {
			//Tick labels
			for(int i = 0; i < tickCount; i++) {
				double unit = Math.round((max-min)/(tickCount+1));
				currentangle += unit;
				var angle = setValue(currentangle);
				Pane pane = new Pane();
				pane.setLayoutX(15);
				pane.setLayoutY(15);
				pane.setPrefHeight(50);
				pane.setPrefWidth(50);
				Label label = new Label("" + getValue());
				label.setLayoutX(15.0);
				label.setLayoutY(35.0);
				label.setStyle("-fx-font-size: 10");
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
		setValue(current);
		
		//Drag and drop
		this.setOnMousePressed(this::mousePressed);
		this.setOnMouseDragged(this::mouseDragged);
		this.setOnMouseReleased(this::mouseReleased);
		
		//Name label
		final Label lname = new Label(name);
		lname.setLayoutY(70);
		lname.setPrefWidth(size);
		lname.setAlignment(Pos.CENTER);
		lname.setStyle("-fx-font-size: 10");
		this.getChildren().add(lname);
	}
	
	public final double setValue(double value) {
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
	
	public final double getValue() {
		return this.value;
	}
	
	private final void mousePressed(MouseEvent e) {
		initialValue = e.getSceneY();
		current = getValue();
	}
	
	private final void mouseDragged(MouseEvent e) {
		setValue(current + (((-e.getSceneY())+initialValue)*((max-min)/100)));
	}
	
	private final void mouseReleased(MouseEvent e) {
		initialValue = 0.0;
	}
}
