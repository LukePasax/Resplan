package view.effects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LimiterPane extends BorderPane {
	
	final private static VUMeterPane compressor = new VUMeterPane(Double.NEGATIVE_INFINITY, 0.0);
	
	final private static VBox effects = new VBox();
	
	final private static Label thresholdValue = new Label("0.0");
	final private static Label attackValue = new Label("0.2");
	final private static Label decayValue = new Label("0.2");
	
	public LimiterPane() {
		final HBox titlebox = new HBox();
		titlebox.setAlignment(Pos.CENTER);
		final Label title = new Label("Limiter");
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
		//Decay
		final HBox hdecay = new HBox();
		final Label decay = new Label("Decay: ");
		hdecay.getChildren().addAll(decay, decayValue);
		hdecay.setAlignment(Pos.CENTER);
		effects.getChildren().add(hdecay);
		
		final Button expand = new Button("View details");
		final HBox hbutton = new HBox();
		hbutton.getChildren().add(expand);
		HBox.setMargin(expand, new Insets(10,0,0,0));
		hbutton.setAlignment(Pos.CENTER);
		effects.getChildren().add(hbutton);
		
		expand.setOnMouseClicked(e -> {
			Limiter.show();
		});
		this.setCenter(effects);		
		
		effects.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(10));
		this.setStyle("-fx-border-color: black");
	}
	
	public void setValue(final Double value) {
		compressor.setValue(value);
	}
	
	public static class Limiter {
		public static void show() {
			BorderPane root = new BorderPane();
			final HBox titlebox = new HBox(new Label("Limiter"));
			titlebox.setAlignment(Pos.CENTER);
			
			final HBox firstrow = new HBox();
			final HBox secondrow = new HBox();
			final VBox firstcolumn = new VBox();
			final VBox secondcolumn = new VBox(20);
			
			final ContinuousKnobPane threshold = new ContinuousKnobPane(Double.NEGATIVE_INFINITY, 0.0, 3, "THRESHOLD");
			final ContinuousKnobPane attack = new ContinuousKnobPane(0.2, 20.0, 3, "ATTACK");
			firstrow.getChildren().addAll(threshold, attack);
			
			final ContinuousKnobPane decay = new ContinuousKnobPane(0.2, 40.0, 3, "DECAY");
			secondrow.getChildren().addAll(decay);
			secondrow.setAlignment(Pos.CENTER);
			
			firstcolumn.getChildren().addAll(firstrow, secondrow);
			
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
				decayValue.setText("" + decay.getValue());
				effects.autosize();
				stage.close();
			});
			
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
}
