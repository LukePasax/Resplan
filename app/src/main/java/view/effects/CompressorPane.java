package view.effects;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.*;

public class CompressorPane extends BorderPane {
	
	final private VUMeterPane compressor;

	public CompressorPane() {
		final HBox titlebox = new HBox(new Label("CompressorPane"));
		titlebox.setAlignment(Pos.CENTER);
		final HBox firstrow = new HBox();
		final HBox secondrow = new HBox();
		final HBox thirdrow = new HBox();
		final VBox firstcolumn = new VBox();
		final VBox secondcolumn = new VBox(20);
		
		compressor = new VUMeterPane(Double.NEGATIVE_INFINITY, 0.0);
		firstrow.setAlignment(Pos.CENTER);
		firstrow.getChildren().add(compressor);
		
		final ContinuousKnobPane threshold = new ContinuousKnobPane(Double.NEGATIVE_INFINITY, 0.0, 0, "THRESHOLD");
		final ContinuousKnobPane attack = new ContinuousKnobPane(0.2, 20.0, 3, "ATTACK");
		secondrow.getChildren().addAll(threshold, attack);
		
		final JerkyKnobPane ratio = new JerkyKnobPane(List.of("1:1", "2:1", "4:1", "8:1"), "RATIO");
		final ContinuousKnobPane decay = new ContinuousKnobPane(0.2, 40.0, 3, "DECAY");
		thirdrow.getChildren().addAll(ratio, decay);
		
		firstcolumn.getChildren().addAll(titlebox, firstrow, secondrow, thirdrow);
		
		final Label title = new Label("SIDECHAIN");
		final ComboBox<String> channels = new ComboBox<>();
		channels.getItems().addAll(FXCollections.observableArrayList("channel1", "channel2", "channel3")); //TO COMPLETE with real channels
		final ToggleSwitch switcher = new ToggleSwitch("Internal");
		channels.setDisable(true);
		switcher.setOnMouseClicked(e -> {
			if(switcher.getText().equals("Internal")) {
				switcher.setText("External");
				channels.setDisable(false);
			} else {
				switcher.setText("Internal");
				channels.setDisable(true);
			}
		});
		
		secondcolumn.setAlignment(Pos.CENTER);
		secondcolumn.getChildren().addAll(title, channels, switcher);
		
		this.setCenter(firstcolumn);
		this.setRight(secondcolumn);
		
		this.setPadding(new Insets(10));
		this.setStyle("-fx-border-color: black");
	}
	
	public void setValue(final Double value) {
		this.compressor.setValue(value);
	}
}