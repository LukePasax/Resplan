package view.effects;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.effects.Knob.KnobType;

public class PassPane extends BorderPane {
	public PassPane() throws IOException {
		final HBox titlebox = new HBox(new Label("PassPane"));
		titlebox.setAlignment(Pos.CENTER);
		final KnobPane frequency = new KnobPane(10, 20000, "FREQUENCY", KnobType.VOLUME);
		VBox firstcolumn = new VBox(titlebox, frequency);
		this.setCenter(firstcolumn);		
	}
}
