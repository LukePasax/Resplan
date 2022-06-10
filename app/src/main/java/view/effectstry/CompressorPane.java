package view.effectstry;

import java.io.IOException;
import java.net.URL;

import daw.core.channel.RPChannel;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import view.effectstry.Knob.KnobType;

public class CompressorPane extends BorderPane {
	public CompressorPane() throws IOException {
		final HBox titlebox = new HBox(new Label("CompressorPane"));
		titlebox.setAlignment(Pos.CENTER);
		final HBox firstrow = new HBox();
		final HBox secondrow = new HBox();
		final HBox thirdrow = new HBox();
		final VBox firstcolumn = new VBox();
		final VBox secondcolumn = new VBox(20);
		
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
		
		firstcolumn.getChildren().addAll(titlebox, firstrow, secondrow, thirdrow);
		
		final Label title = new Label("SIDECHAIN");
		final ComboBox<String> channels = new ComboBox<>();
		channels.getItems().addAll(FXCollections.observableArrayList("channel1", "channel2", "channel3")); //TO COMPLETE with real channels
		final Button switcher = new Button("Disable");
		switcher.setOnMouseClicked(e -> {
			if(switcher.getText().equals("Disable")) {
				switcher.setText("Enable");
				channels.setDisable(true);
			} else {
				switcher.setText("Disable");
				channels.setDisable(false);
			}
		});
		
		secondcolumn.setAlignment(Pos.CENTER);
		secondcolumn.getChildren().addAll(title, channels, switcher);
		
		this.setCenter(firstcolumn);
		this.setRight(secondcolumn);
	}
}
