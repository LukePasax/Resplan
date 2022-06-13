package view.effects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LimiterPane extends BorderPane {
	
	public LimiterPane() {
		final HBox titlebox = new HBox(new Label("LimiterPane"));
		titlebox.setAlignment(Pos.CENTER);
		final HBox firstrow = new HBox();
		final HBox secondrow = new HBox();
		final HBox thirdrow = new HBox();
		final VBox firstcolumn = new VBox();
		
		final VUMeterPane compressor = new VUMeterPane(Double.NEGATIVE_INFINITY, 0.0);

		firstrow.setAlignment(Pos.CENTER);
		firstrow.getChildren().add(compressor);
		
		final ContinuousKnobPane threshold = new ContinuousKnobPane(Double.NEGATIVE_INFINITY, 0.0, 3, "THRESHOLD");
		final ContinuousKnobPane attack = new ContinuousKnobPane(0.2, 20.0, 3, "ATTACK");
		secondrow.getChildren().addAll(threshold, attack);
		
		final ContinuousKnobPane decay = new ContinuousKnobPane(0.2, 40.0, 3, "DECAY");
		thirdrow.getChildren().addAll(decay);
		
		firstcolumn.getChildren().addAll(titlebox, firstrow, secondrow, thirdrow);
		this.setCenter(firstcolumn);
		this.setPadding(new Insets(5));
		this.setStyle("-fx-border-color: black");
	}
}
