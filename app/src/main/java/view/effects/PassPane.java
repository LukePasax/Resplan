package view.effects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PassPane extends BorderPane {

	public PassPane() {
		final HBox titlebox = new HBox(new Label("PassPane"));
		titlebox.setAlignment(Pos.CENTER);
		final ContinuousKnobPane frequency = new ContinuousKnobPane(10.0, 20000.0, 3, "FREQUENCY");
		VBox firstcolumn = new VBox(titlebox, frequency);
		this.setCenter(firstcolumn);
		this.setPadding(new Insets(10));
		this.setStyle("-fx-border-color: black");
	}

}
