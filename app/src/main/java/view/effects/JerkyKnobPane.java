package view.effects;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class JerkyKnobPane extends Pane{
	private String min;
	private String max;
	private String value = "";
	private List<String> values;
	final Pane rotation;
	final Label lvalue;
	private final static double correctionx = 25;
	private final static double correctiony = correctionx/1.5;
	private final static double size = 150.0;
	private final static double half = size/2;
	private final static double radius = 40.0;
	
	private boolean drag = false;
	
	private double initialValue = 0.0;
	private String current;

	public JerkyKnobPane(final List<String> values, final String name) {
		this.min = values.get(0);
		this.max = values.get(values.size()-1);
		this.values = values;
		
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
		final Label lmin = new Label(min);
		lmin.setLayoutX(30);
		lmin.setLayoutY(105);
		//Max label
		final Label lmax = new Label(max);
		lmax.setLayoutX(110);
		lmax.setLayoutY(105);
		//Value label
		lvalue = new Label(value);
		lvalue.setLayoutX(55);
		lvalue.setLayoutY(half-10);
		lvalue.setPrefWidth(radius);
		lvalue.setAlignment(Pos.CENTER);
		
		rotation.getChildren().addAll(little);
		this.getChildren().addAll(circle, rotation, lmin, lmax, lvalue);
		
		//Tick labels
		for(int i = 1; i < values.size()-1; i++) {
			setValue(values.get(i));
			double angle = rotation.getRotate();
			Pane pane = new Pane();
			pane.setLayoutX(35);
			pane.setLayoutY(35);
			pane.setPrefHeight(80);
			pane.setPrefWidth(80);
			Label label = new Label(getValue());
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

		setValue(values.get(0));
		
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
	
	public void setValue(String value) {
		if(values.contains(value)) {
			this.value = value;
			lvalue.setText(value);
			rotation.setRotate((270*values.indexOf(value))/(values.size()-1));
		}
	}
	
	public String getValue() {
		return this.value;
	}
	
	private void mousePressed(MouseEvent e) {
		initialValue = e.getSceneY();
		current = getValue();
	}
	
	private void mouseDragged(MouseEvent e) {
		double poslimit = 10.0;
		double neglimit = -10.0;
		double move = ((-e.getSceneY())+initialValue)*0.1;
		if(move > poslimit && !drag){
			if(values.indexOf(current) < values.size()-1) {
				setValue(values.get(values.indexOf(current)+1));
				drag = true;
			}
		} else if(move < neglimit && !drag){
			if(values.indexOf(current) > 0) {
				setValue(values.get(values.indexOf(current)-1));
				drag = true;
			}
		}
	}
	
	private void mouseReleased(MouseEvent e) {
		initialValue = 0.0;
		drag = false;
	}
}
