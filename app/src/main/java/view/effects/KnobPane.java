package view.effects;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class KnobPane extends VBox {
	
	public KnobPane(final int min, final int max) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		URL url = this.getClass().getResource("/view/Knob.fxml");
		loader.setLocation(url);
		Pane knob = loader.load();
		TextField volume = new TextField();
		volume.setPromptText("Input an integer volume");
		this.setAlignment(Pos.CENTER);
		volume.setMaxWidth(150);
		volume.setPrefWidth(150);
		volume.setAlignment(Pos.CENTER);
		Button refresh = new Button("Refresh");
		
		KnobController controller = loader.getController();
		controller.init(0, 100);
		refresh.setOnMouseClicked(e -> {
			try {
				controller.rotate(Integer.parseInt(volume.getText()));
			} catch (Exception exception) {}
		});
		this.getChildren().addAll(volume, refresh, knob);
	}
}
