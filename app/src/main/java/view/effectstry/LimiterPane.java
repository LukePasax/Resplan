package view.effectstry;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import view.effectstry.Knob.KnobType;

public class LimiterPane extends BorderPane{
	public LimiterPane() throws IOException {
		final HBox titlebox = new HBox(new Label("LimiterPane"));
		titlebox.setAlignment(Pos.CENTER);
		final HBox firstrow = new HBox();
		final HBox secondrow = new HBox();
		final HBox thirdrow = new HBox();
		final VBox firstcolumn = new VBox();
		
		final FXMLLoader loader = new FXMLLoader();
		final URL url = this.getClass().getResource("/view/Compressor.fxml");
		loader.setLocation(url);
		final Pane compressor = loader.load();
		firstrow.setAlignment(Pos.CENTER);
		firstrow.getChildren().add(compressor);
		
		final KnobPane threshold = new KnobPane(Double.NEGATIVE_INFINITY, 0, "THRESHOLD", KnobType.OTHER);
		final KnobPane attack = new KnobPane(0.2, 20, "ATTACK", KnobType.OTHER);
		secondrow.getChildren().addAll(threshold, attack);
		
		final KnobPane decay = new KnobPane(0.2, 40, "DECAY", KnobType.OTHER);
		thirdrow.getChildren().addAll(decay);
		
		firstcolumn.getChildren().addAll(titlebox, firstrow, secondrow, thirdrow);
		this.setCenter(firstcolumn);
	}
}
