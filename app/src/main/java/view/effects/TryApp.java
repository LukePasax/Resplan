package view.effects;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class TryApp extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		CompressorPane root = new CompressorPane();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
