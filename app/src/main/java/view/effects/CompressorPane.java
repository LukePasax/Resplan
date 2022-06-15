package view.effects;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.*;

public class CompressorPane extends BorderPane {
	
	final private static VUMeterPane compressor = new VUMeterPane(Double.NEGATIVE_INFINITY, 0.0);
	
	final private static VBox effects = new VBox();
	
	final private static Label thresholdValue = new Label("0.0");
	final private static Label attackValue = new Label("0.2");
	final private static Label ratioValue = new Label("1:1");
	final private static Label decayValue = new Label("0.2");
	final private static Label sidechainValue = new Label("OFF");

	public CompressorPane() {		
		final HBox titlebox = new HBox();
		titlebox.setAlignment(Pos.CENTER);
		final Label title = new Label("Compressor");
		title.setStyle("-fx-font-weight: bold");
		titlebox.getChildren().add(title);
		effects.getChildren().add(titlebox);
		//VUMiter
		final HBox firstrow = new HBox();
		firstrow.getChildren().add(compressor);
		firstrow.setAlignment(Pos.CENTER);
		effects.getChildren().add(firstrow);
		//Threshold
		final HBox hthreshold = new HBox();
		final Label threshold = new Label("Threshold: ");
		hthreshold.getChildren().addAll(threshold, thresholdValue);
		hthreshold.setAlignment(Pos.CENTER);
		effects.getChildren().add(hthreshold);
		//Attack
		final HBox hattack = new HBox();
		final Label attack = new Label("Attack: ");
		hattack.getChildren().addAll(attack, attackValue);
		hattack.setAlignment(Pos.CENTER);
		effects.getChildren().add(hattack);
		//Ratio
		final HBox hratio = new HBox();
		final Label ratio = new Label("Ratio: ");
		hratio.getChildren().addAll(ratio, ratioValue);
		hratio.setAlignment(Pos.CENTER);
		effects.getChildren().add(hratio);
		//Decay
		final HBox hdecay = new HBox();
		final Label decay = new Label("Decay: ");
		hdecay.getChildren().addAll(decay, decayValue);
		hdecay.setAlignment(Pos.CENTER);
		effects.getChildren().add(hdecay);
		//Sidechain
		final HBox hsidechain = new HBox();
		final Label sidechain = new Label("Sidechain: ");
		hsidechain.getChildren().addAll(sidechain, sidechainValue);
		hsidechain.setAlignment(Pos.CENTER);
		effects.getChildren().add(hsidechain);
		
		final Button expand = new Button("View details");
		final HBox hbutton = new HBox();
		hbutton.getChildren().add(expand);
		HBox.setMargin(expand, new Insets(10,0,0,0));
		hbutton.setAlignment(Pos.CENTER);
		effects.getChildren().add(hbutton);
		
		expand.setOnMouseClicked(e -> {
			Compressor.show(Double.parseDouble(thresholdValue.getText()), Double.parseDouble(attackValue.getText()), Double.parseDouble(decayValue.getText()));
		});
		this.setCenter(effects);
		effects.setPadding(new Insets(10));
		effects.setAlignment(Pos.CENTER);
		this.setStyle("-fx-border-color: black");
	}
	
	public void setValue(final Double value) {
		compressor.setValue(value);
	}
	
	public static class Compressor {
		public static void show(final double currentThreshold, final double currentAttack, final double currentDecay) {
			BorderPane root = new BorderPane();
			final HBox titlebox = new HBox(new Label("Compressor"));
			titlebox.setAlignment(Pos.CENTER);
			
			final HBox firstrow = new HBox();
			final HBox secondrow = new HBox();
			final VBox firstcolumn = new VBox();
			final VBox secondcolumn = new VBox(20);			
			
			final ContinuousKnobPane threshold = new ContinuousKnobPane(Double.NEGATIVE_INFINITY, 0.0, currentThreshold, 0, "THRESHOLD");
			final ContinuousKnobPane attack = new ContinuousKnobPane(0.2, 20.0, currentAttack, 3, "ATTACK");
			firstrow.getChildren().addAll(threshold, attack);
			
			final JerkyKnobPane ratio = new JerkyKnobPane(List.of("1:1", "2:1", "4:1", "8:1"), "RATIO");
			final ContinuousKnobPane decay = new ContinuousKnobPane(0.2, 40.0, currentDecay, 3, "DECAY");
			secondrow.getChildren().addAll(ratio, decay);
			
			firstcolumn.getChildren().addAll(firstrow, secondrow);
			
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
			
			root.setTop(titlebox);
			root.setCenter(firstcolumn);
			root.setRight(secondcolumn);
			
			root.setPadding(new Insets(10));
			root.setStyle("-fx-border-color: black");
			
			Stage stage = new Stage();
			stage.setResizable(false);
			
			final HBox hrefresh = new HBox();
			final Button refresh = new Button("Refresh values and close");
			hrefresh.setAlignment(Pos.CENTER);
			hrefresh.getChildren().add(refresh);
			root.setBottom(hrefresh);
			refresh.setOnMouseClicked(e -> {
				thresholdValue.setText("" + threshold.getValue());
				attackValue.setText("" + attack.getValue());
				ratioValue.setText(ratio.getValue());
				decayValue.setText("" + decay.getValue());
				if(switcher.isSelected()) {
					sidechainValue.setText(channels.getSelectionModel().getSelectedItem());
				} else {
					sidechainValue.setText("OFF");
				}
				effects.autosize();
				stage.close();
			});
			
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
}