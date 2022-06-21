package view.effects;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public final class JerkyKnobPane extends Pane{
	private String min;
	private String max;
	private String value = "";
	private List<String> values;
	private final Pane rotation;
	private final Label lvalue;
	private final static double correctionx = 18;
	private final static double correctiony = correctionx/1.5;
	private final static double size = 80.0;
	private final static double half = size/2;
	private final static double radius = 25.0;
	
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
		final Label lmin = new Label(min);
		lmin.setLayoutX(8.0);
		lmin.setLayoutY(56.0);
		lmin.setStyle("-fx-font-size: 11;");
		//Max label
		final Label lmax = new Label(max);
		lmax.setLayoutX(56.0);
		lmax.setLayoutY(56.0);
		lmax.setStyle("-fx-font-size: 11;");
		//Value label
		lvalue = new Label(value);
		lvalue.setLayoutX(15);
		lvalue.setLayoutY(half-10);
		lvalue.setPrefWidth(radius*2);
		lvalue.setAlignment(Pos.CENTER);
		lvalue.setStyle("-fx-font-weight: bold; -fx-font-size: 11;");
		
		rotation.getChildren().addAll(little);
		this.getChildren().addAll(circle, rotation, lmin, lmax, lvalue);
		
		//Tick labels
		for(int i = 1; i < values.size()-1; i++) {
			setValue(values.get(i));
			double angle = rotation.getRotate();
			Pane pane = new Pane();
			pane.setLayoutX(15);
			pane.setLayoutY(15);
			pane.setPrefHeight(50);
			pane.setPrefWidth(50);
			Label label = new Label(getValue());
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

		setValue(values.get(0));
		
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
	
	public final void setValue(String value) {
		if(values.contains(value)) {
			this.value = value;
			lvalue.setText(value);
			rotation.setRotate((double)(270*values.indexOf(value))/(values.size()-1));
		}
	}
	
	public final String getValue() {
		return this.value;
	}
	
	private final void mousePressed(MouseEvent e) {
		initialValue = e.getSceneY();
		current = getValue();
	}
	
	private final void mouseDragged(MouseEvent e) {
		double poslimit = 5.0;
		double neglimit = -5.0;
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
	
	private final void mouseReleased(MouseEvent e) {
		initialValue = 0.0;
		drag = false;
	}
}
