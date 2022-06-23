package view.effects;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import resplan.Starter;

public final class PassPane extends BorderPane {
	
	final private Label frequencyValue = new Label("10.0");
	final private VBox effects = new VBox();
	private String channel;
	private int index;

	public PassPane(final String title, final String channel, final int index) {
		final HBox titlebox = new HBox();
		titlebox.setAlignment(Pos.CENTER);
		final Label ltitle = new Label(title);
		ltitle.setStyle("-fx-font-weight: bold");
		titlebox.getChildren().add(ltitle);
		effects.getChildren().add(titlebox);
		//Frequency
		final HBox hfrequency = new HBox();
		final Label frequency = new Label("Frequency: ");
		hfrequency.getChildren().addAll(frequency, frequencyValue);
		hfrequency.setAlignment(Pos.CENTER);
		effects.getChildren().add(hfrequency);
		
		final Button expand = new Button("View details");
		final HBox hbutton = new HBox();
		hbutton.getChildren().add(expand);
		HBox.setMargin(expand, new Insets(10,0,0,0));
		hbutton.setAlignment(Pos.CENTER);
		effects.getChildren().add(hbutton);
		
		expand.setOnMouseClicked(e -> {
			final Pass pass = new Pass();
			pass.show(title, Double.parseDouble(frequencyValue.getText()));
		});
		hfrequency.autosize();
		this.setCenter(effects);		
		
		effects.setAlignment(Pos.CENTER);

		
		this.setPadding(new Insets(10));
		this.setStyle("-fx-border-color: black");
		String css = this.getClass().getResource("/stylesheets/planning.css").toExternalForm(); 
		this.getStylesheets().add(css);
	}
	
	public final class Pass {
		public final void show(final String title, final double currentFrequency) {
			BorderPane root = new BorderPane();
			final HBox titlebox = new HBox(new Label(title));
			titlebox.setAlignment(Pos.CENTER);
			final ContinuousKnobPane frequency = new ContinuousKnobPane(10.0, 20000.0, currentFrequency, 3, "FREQUENCY");
			frequency.getValueProperty().addListener((ch, old, n) -> {
				setParameters("frequency", n.floatValue());
			});
			HBox firstcolumn = new HBox(frequency);
			firstcolumn.setAlignment(Pos.CENTER);
			root.setCenter(firstcolumn);
			
			root.setTop(titlebox);
			
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
				frequencyValue.setText("" + frequency.getValue());
				effects.autosize();
				stage.close();
			});
			
			Scene scene = new Scene(root);
			String css = scene.getClass().getResource("/stylesheets/planning.css").toExternalForm(); 
			scene.getStylesheets().add(css);
			stage.setScene(scene);
			stage.show();
		}
		
		private final void setParameters(String par, Float value) {
			final Map<String, Float> values = new HashMap<>();
			values.put(par, value);
			Starter.getController().setEffectParameters(channel, index, values);
		}
	}
}
