package view.effects;

import java.io.IOException;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.effects.Knob.KnobType;

public class ReverbPane extends BorderPane {

	public ReverbPane() throws IOException {
		final HBox firstrow = new HBox();
		final HBox secondrow = new HBox();
		final VBox firstcolumn = new VBox();
		
		final KnobPane damping = new KnobPane(0, 100, "DAMPING", KnobType.VOLUME);
		final KnobPane roomsize = new KnobPane(0.2, 20, "ROOMSIZE", KnobType.VOLUME);
		firstrow.getChildren().addAll(damping, roomsize);
		
		final KnobPane early = new KnobPane(0, 100, "EARLY REF", KnobType.VOLUME);
		final KnobPane late = new KnobPane(0.2, 20, "LATE REF", KnobType.VOLUME);
		secondrow.getChildren().addAll(early, late);
		
		firstcolumn.getChildren().addAll(firstrow, secondrow);
		this.setCenter(firstcolumn);
	}
}
