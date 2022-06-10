package view.effectstry;

import java.io.IOException;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import view.effectstry.Knob.KnobType;

public class KnobPane extends VBox {
	
	public KnobPane(final double min, final double max, final String name, final KnobType type) throws IOException {
		final FXMLLoader loader = new FXMLLoader();
		final URL url = this.getClass().getResource("/view/Knob.fxml");
		loader.setLocation(url);
		final Pane knob = loader.load();
		final TextField value = new TextField();
		value.setPromptText("Insert a value");
		this.setAlignment(Pos.CENTER);
		value.setMaxWidth(150);
		value.setPrefWidth(150);
		value.setAlignment(Pos.CENTER);
		final Button refresh = new Button("Refresh");
		
		final ComboBox<String> ratio = new ComboBox<>();
		ratio.setPromptText("Choose a ratio");
		ratio.setItems(FXCollections.observableArrayList("1:1", "2:1", "4:1", "8:1"));
		
		final KnobController controller = loader.getController();
		controller.init(min, max, name, type);
		refresh.setOnMouseClicked(e -> {
			try {
				controller.rotate(Double.parseDouble(value.getText()));
			} catch (Exception exception) {}
		});
		ratio.setOnAction(e -> {
			controller.setRatio(ratio.getSelectionModel().getSelectedItem());
		});
		if(!(type.equals(KnobType.RATIO))) {
			this.getChildren().addAll(value, refresh, knob);
		} else {
			this.getChildren().addAll(ratio, knob);
		}
	}
}
