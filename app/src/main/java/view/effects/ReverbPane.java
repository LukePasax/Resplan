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

public final class ReverbPane extends BorderPane {
	
	final private VBox effects = new VBox();
	
	final private Label dampingValue = new Label("0.0");
	final private Label roomsizeValue = new Label("0.0");
	final private Label earlyValue = new Label("0.0");
	final private Label lateValue = new Label("0.0");
	final private Label dryValue = new Label("0.0");
	final private Label wetValue = new Label("0.0");
	
	public ReverbPane(final String title) {
		final HBox titlebox = new HBox();
		titlebox.setAlignment(Pos.CENTER);
		final Label ltitle = new Label(title);
		ltitle.setStyle("-fx-font-weight: bold");
		titlebox.getChildren().add(ltitle);
		effects.getChildren().add(titlebox);
		//Damping
		final HBox hdamping = new HBox();
		final Label damping = new Label("Damping: ");
		hdamping.getChildren().addAll(damping, dampingValue);
		hdamping.setAlignment(Pos.CENTER);
		effects.getChildren().add(hdamping);
		//Roomsize
		final HBox hroomsize = new HBox();
		final Label roomsize = new Label("Roomsize: ");
		hroomsize.getChildren().addAll(roomsize, roomsizeValue);
		hroomsize.setAlignment(Pos.CENTER);
		effects.getChildren().add(hroomsize);
		//Early
		final HBox hearly = new HBox();
		final Label early = new Label("Early: ");
		hearly.getChildren().addAll(early, earlyValue);
		hearly.setAlignment(Pos.CENTER);
		effects.getChildren().add(hearly);
		//Late
		final HBox hlate = new HBox();
		final Label late = new Label("Late: ");
		hlate.getChildren().addAll(late, lateValue);
		hlate.setAlignment(Pos.CENTER);
		effects.getChildren().add(hlate);
		//Dry
		final HBox hdry = new HBox();
		final Label dry = new Label("Dry: ");
		hdry.getChildren().addAll(dry, dryValue);
		hdry.setAlignment(Pos.CENTER);
		effects.getChildren().add(hdry);
		//Wet
		final HBox hwet = new HBox();
		final Label wet = new Label("Wet: ");
		hwet.getChildren().addAll(wet, wetValue);
		hwet.setAlignment(Pos.CENTER);
		effects.getChildren().add(hwet);
		
		final Button expand = new Button("View details");
		final HBox hbutton = new HBox();
		hbutton.getChildren().add(expand);
		HBox.setMargin(expand, new Insets(10,0,0,0));
		hbutton.setAlignment(Pos.CENTER);
		effects.getChildren().add(hbutton);
		
		expand.setOnMouseClicked(e -> {
			final Reverb reverb = new Reverb();
			reverb.show(title, Double.parseDouble(dampingValue.getText()), Double.parseDouble(roomsizeValue.getText()), Double.parseDouble(earlyValue.getText()),
						Double.parseDouble(lateValue.getText()), Double.parseDouble(dryValue.getText()), Double.parseDouble(wetValue.getText()));
		});
		this.setCenter(effects);
		effects.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(10));
		this.setStyle("-fx-border-color: black");
		String css = this.getClass().getResource("/stylesheets/planning.css").toExternalForm(); 
		this.getStylesheets().add(css);
	}
	
	public final class Reverb {
		public final void show(final String title, final double currentDamping, final double currentRoomsize, final double currentEarly,
								final double currentLate, final double currentDry, final double currentWet) {
			BorderPane root = new BorderPane();
			final HBox titlebox = new HBox(new Label(title));
			titlebox.setAlignment(Pos.CENTER);
			final HBox firstrow = new HBox();
			final HBox secondrow = new HBox();
			final HBox thirdrow = new HBox();
			final VBox firstcolumn = new VBox();
			
			final ContinuousKnobPane damping = new ContinuousKnobPane(0.0, 100.0, currentDamping, 3, "DAMPING");
			final ContinuousKnobPane roomsize = new ContinuousKnobPane(0.0, 100.0, currentRoomsize, 3, "ROOMSIZE");
			firstrow.getChildren().addAll(damping, roomsize);
			
			final ContinuousKnobPane early = new ContinuousKnobPane(0.0, 100.0, currentEarly, 3, "EARLY REF");
			final ContinuousKnobPane late = new ContinuousKnobPane(0.0, 100.0, currentLate, 3, "LATE REF");
			secondrow.getChildren().addAll(early, late);
			
			final ContinuousKnobPane dry = new ContinuousKnobPane(0.0, 100.0, currentDry, 3, "DRY");
			final ContinuousKnobPane wet = new ContinuousKnobPane(0.0, 100.0, currentWet, 3, "WET");
			thirdrow.getChildren().addAll(dry, wet);
			
			firstcolumn.getChildren().addAll(titlebox, firstrow, secondrow, thirdrow);
			root.setCenter(firstcolumn);
			
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
				dampingValue.setText("" + damping.getValue());
				roomsizeValue.setText("" + roomsize.getValue());
				earlyValue.setText("" + early.getValue());
				lateValue.setText("" + late.getValue());
				dryValue.setText("" + dry.getValue());
				wetValue.setText("" + wet.getValue());
				effects.autosize();
				stage.close();
			});
			
			Scene scene = new Scene(root);
			String css = scene.getClass().getResource("/stylesheets/planning.css").toExternalForm(); 
			scene.getStylesheets().add(css);
			stage.setScene(scene);
			stage.show();
		}
	}
}
