package view.effects;

import java.io.IOException;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.effects.Knob.KnobType;

public class CompressorPane extends VBox {
	public CompressorPane() throws IOException {
		final HBox firstrow = new HBox();
		final HBox secondrow = new HBox();
		
		final KnobPane threshold = new KnobPane(Double.NEGATIVE_INFINITY, 0, "THRESHOLD", KnobType.OTHER);
		final KnobPane attack = new KnobPane(0.2, 20, "ATTACK", KnobType.OTHER);
		firstrow.getChildren().addAll(threshold, attack);
		
		final KnobPane ratio = new KnobPane(Double.NEGATIVE_INFINITY, 0, "RATIO", KnobType.RATIO);
		final KnobPane decay = new KnobPane(0.2, 40, "DECAY", KnobType.OTHER);
		secondrow.getChildren().addAll(ratio, decay);
		
		this.getChildren().addAll(firstrow, secondrow);
	}
}
