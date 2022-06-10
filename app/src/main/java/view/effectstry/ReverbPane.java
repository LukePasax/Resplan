package view.effectstry;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.effectstry.Knob.KnobType;

public class ReverbPane extends BorderPane {

	public ReverbPane() throws IOException {
		final HBox titlebox = new HBox(new Label("ReverbPane"));
		titlebox.setAlignment(Pos.CENTER);
		final HBox firstrow = new HBox();
		final HBox secondrow = new HBox();
		final VBox firstcolumn = new VBox();
		
		final KnobPane damping = new KnobPane(0, 100, "DAMPING", KnobType.VOLUME);	//TO ADJUST...
		final KnobPane roomsize = new KnobPane(0.2, 20, "ROOMSIZE", KnobType.VOLUME); //
		firstrow.getChildren().addAll(damping, roomsize);
		
		final KnobPane early = new KnobPane(0, 100, "EARLY REF", KnobType.VOLUME);	//
		final KnobPane late = new KnobPane(0.2, 20, "LATE REF", KnobType.VOLUME);	//......
		secondrow.getChildren().addAll(early, late);
		
		firstcolumn.getChildren().addAll(titlebox, firstrow, secondrow);
		this.setCenter(firstcolumn);
	}
}
