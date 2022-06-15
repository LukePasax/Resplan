package view.effects;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class VUMeterPane extends HBox {

	private double lowerbound;
	private double upperbound;
	private final ProgressBar compressor = new ProgressBar();
	private final Label lcurrent;
	
	public VUMeterPane(final Double lowerbound, final Double upperbound) {
		AnchorPane root = new AnchorPane();
		if(Double.compare(lowerbound, upperbound) >= 0) {
			throw new IllegalArgumentException();
		}
		final Label llower;
		if(lowerbound.equals(Double.NEGATIVE_INFINITY)) {
			this.lowerbound = -100.0;
			llower = new Label("-∞");
			llower.setStyle("-fx-font-size: 15");
		} else {
			this.lowerbound = lowerbound;
			llower = new Label("" + lowerbound);			
		}
		this.upperbound = upperbound;
		compressor.setRotate(180);
		compressor.setProgress(upperbound);
		compressor.setLayoutY(10);
		compressor.setPrefWidth(100);
		compressor.setPrefHeight(20);
		compressor.setStyle("-fx-accent: #00fa9a;");
		
		//Value, min, max labels
		lcurrent = new Label("" + upperbound);
		final Label lupper = new Label("" + upperbound);

		lcurrent.setPrefWidth(compressor.getPrefWidth());
		lcurrent.setAlignment(Pos.CENTER);
		lcurrent.setLayoutX(0);
		lcurrent.setLayoutY(10);
		lcurrent.setStyle("-fx-font-weight: bold;");
		llower.setLayoutX(compressor.getLayoutX()-10);
		llower.setLayoutY(compressor.getPrefHeight()+10);
		lupper.setLayoutX(compressor.getPrefWidth()-10);
		lupper.setLayoutY(compressor.getPrefHeight()+10);
		
		root.getChildren().addAll(compressor, lcurrent, llower, lupper);
		this.getChildren().add(root);
	}
	
	public void setValue(final double value) {
		if(value >= lowerbound && value <= upperbound) {
			compressor.setProgress(-value/(-lowerbound));
		} else if (value < lowerbound) {
			compressor.setProgress(-lowerbound);
		}
		lcurrent.setText("" + value);
	}
}
