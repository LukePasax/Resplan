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

public class PassPane extends BorderPane {
	
	final private static Label frequencyValue = new Label("10.0");
	final private static VBox effects = new VBox();

	public PassPane() {
		final HBox titlebox = new HBox();
		titlebox.setAlignment(Pos.CENTER);
		final Label title = new Label("Pass");
		title.setStyle("-fx-font-weight: bold");
		titlebox.getChildren().add(title);
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
			Pass.show(Double.parseDouble(frequencyValue.getText()));
		});
		hfrequency.autosize();
		this.setCenter(effects);		
		
		effects.setAlignment(Pos.CENTER);

		
		this.setPadding(new Insets(10));
		this.setStyle("-fx-border-color: black");
	}
	
	public static class Pass {
		public static void show(final double currentFrequency) {
			BorderPane root = new BorderPane();
			final HBox titlebox = new HBox(new Label("Pass"));
			titlebox.setAlignment(Pos.CENTER);
			final ContinuousKnobPane frequency = new ContinuousKnobPane(10.0, 20000.0, currentFrequency, 3, "FREQUENCY");
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
			stage.setScene(scene);
			stage.show();
		}
	}
}
