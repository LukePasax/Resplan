package view.effects;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class TryApp extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		HBox root = new HBox();
		CompressorPane cpane = new CompressorPane();
		LimiterPane lpane = new LimiterPane();
		root.getChildren().addAll(cpane, lpane);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
