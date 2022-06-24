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

public final class ReverbPane extends BorderPane {
	
	final private VBox effects = new VBox();
	
	final private Label dampingValue = new Label("0.0");
	final private Label roomsizeValue = new Label("0.0");
	final private Label earlyValue = new Label("0.0");
	final private Label lateValue = new Label("0.0");
	final private Label dryWetValue = new Label("0.0");
	private String channel;
	private int index;
	
	public ReverbPane(final String title, final String channel, final int index) {
		this.index = index;
		this.channel = channel;
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
		final HBox hdryWet = new HBox();
		final Label dryWet = new Label("Dry Wet: ");
		hdryWet.getChildren().addAll(dryWet, dryWetValue);
		hdryWet.setAlignment(Pos.CENTER);
		effects.getChildren().add(hdryWet);
		
		final Button expand = new Button("View details");
		final HBox hbutton = new HBox();
		hbutton.getChildren().add(expand);
		HBox.setMargin(expand, new Insets(10,0,0,0));
		hbutton.setAlignment(Pos.CENTER);
		effects.getChildren().add(hbutton);
		
		expand.setOnMouseClicked(e -> {
			final Reverb reverb = new Reverb();
			reverb.show(title, Double.parseDouble(dampingValue.getText()), Double.parseDouble(roomsizeValue.getText()), Double.parseDouble(earlyValue.getText()),
						Double.parseDouble(lateValue.getText()), Double.parseDouble(dryWetValue.getText()));
		});
		this.setCenter(effects);
		effects.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(10));
		this.setStyle("-fx-border-color: black");
		String css = this.getClass().getResource("/stylesheets/resplan.css").toExternalForm();
		this.getStylesheets().add(css);
	}
	
	public final class Reverb {
		public final void show(final String title, final double currentDamping, final double currentRoomsize, final double currentEarly,
								final double currentLate, final double currentDryWet) {
			BorderPane root = new BorderPane();
			final HBox titlebox = new HBox(new Label(title));
			titlebox.setAlignment(Pos.CENTER);
			final HBox firstrow = new HBox();
			final HBox secondrow = new HBox();
			final HBox thirdrow = new HBox();
			final VBox firstcolumn = new VBox();
			
			final ContinuousKnobPane damping = new ContinuousKnobPane(0.0, 100.0, currentDamping, 3, "DAMPING");
			damping.getValueProperty().addListener((ch, old, n) -> {
				setParameters("damping", (n.floatValue()/100));
			});
			final ContinuousKnobPane roomsize = new ContinuousKnobPane(0.0, 100.0, currentRoomsize, 3, "ROOMSIZE");
			roomsize.getValueProperty().addListener((ch, old, n) -> {
				setParameters("roomSize", (n.floatValue()/100));
			});
			firstrow.getChildren().addAll(damping, roomsize);
			
			final ContinuousKnobPane early = new ContinuousKnobPane(0.0, 100.0, currentEarly, 3, "EARLY REF");
			early.getValueProperty().addListener((ch, old, n) -> {
				setParameters("earlyReflectionsLevel", (n.floatValue()/100));
			});
			final ContinuousKnobPane late = new ContinuousKnobPane(0.0, 100.0, currentLate, 3, "LATE REF");
			late.getValueProperty().addListener((ch, old, n) -> {
				setParameters("lateReverbLevel", (n.floatValue()/100));
			});
			secondrow.getChildren().addAll(early, late);
			
			final ContinuousKnobPane dryWet = new ContinuousKnobPane(0.0, 100.0, currentDryWet, 3, "DRYWET");
			dryWet.getValueProperty().addListener((ch, old, n) -> {
				setParameters("dryWet", (n.floatValue()/100));
			});
			thirdrow.setAlignment(Pos.CENTER);
			thirdrow.getChildren().addAll(dryWet);
			
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
				dryWetValue.setText("" + dryWet.getValue());
				effects.autosize();
				stage.close();
			});
			
			Scene scene = new Scene(root);
			String css = scene.getClass().getResource("/stylesheets/resplan.css").toExternalForm();
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
