package view.effects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ReverbPane extends BorderPane {

	public ReverbPane() {
		final HBox titlebox = new HBox(new Label("ReverbPane"));
		titlebox.setAlignment(Pos.CENTER);
		final HBox firstrow = new HBox();
		final HBox secondrow = new HBox();
		final VBox firstcolumn = new VBox();
		
		final ContinuousKnobPane damping = new ContinuousKnobPane(0.0, 100.0, 3, "DAMPING");
		final ContinuousKnobPane roomsize = new ContinuousKnobPane(0.0, 100.0, 3, "ROOMSIZE");
		firstrow.getChildren().addAll(damping, roomsize);
		
		final ContinuousKnobPane early = new ContinuousKnobPane(0.0, 100.0, 3, "EARLY REF");
		final ContinuousKnobPane late = new ContinuousKnobPane(0.0, 100.0, 3, "LATE REF");
		secondrow.getChildren().addAll(early, late);
		
		firstcolumn.getChildren().addAll(titlebox, firstrow, secondrow);
		this.setCenter(firstcolumn);
		
		this.setPadding(new Insets(10));
		this.setStyle("-fx-border-color: black");
	}
}
