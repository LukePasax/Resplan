package view.effects;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import view.effects.Knob.KnobType;

public class CompressorPane extends VBox {
	public CompressorPane() throws IOException {
		final HBox firstrow = new HBox();
		final HBox secondrow = new HBox();
		final HBox thirdrow = new HBox();
		
		final FXMLLoader loader = new FXMLLoader();
		final URL url = this.getClass().getResource("/view/Compressor.fxml");
		loader.setLocation(url);
		final Pane compressor = loader.load();
		firstrow.setAlignment(Pos.CENTER);
		firstrow.getChildren().add(compressor);
		
		final KnobPane threshold = new KnobPane(Double.NEGATIVE_INFINITY, 0, "THRESHOLD", KnobType.OTHER);
		final KnobPane attack = new KnobPane(0.2, 20, "ATTACK", KnobType.OTHER);
		secondrow.getChildren().addAll(threshold, attack);
		
		final KnobPane ratio = new KnobPane(Double.NEGATIVE_INFINITY, 0, "RATIO", KnobType.RATIO);
		final KnobPane decay = new KnobPane(0.2, 40, "DECAY", KnobType.OTHER);
		thirdrow.getChildren().addAll(ratio, decay);
		
		this.getChildren().addAll(firstrow, secondrow, thirdrow);
	}
}
