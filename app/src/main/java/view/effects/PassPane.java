package view.effects;

import java.io.IOException;

import javafx.scene.layout.BorderPane;
import view.effects.Knob.KnobType;

public class PassPane extends BorderPane {
	public PassPane() throws IOException {
		final KnobPane frequency = new KnobPane(10, 20000, "FREQUENCY", KnobType.VOLUME);
		this.setCenter(frequency);		
	}
}
