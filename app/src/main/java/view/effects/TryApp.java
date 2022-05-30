package view.effects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TryApp extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		
		VBox root = new VBox();
		CompressorPane pane = new CompressorPane();
		root.getChildren().addAll(pane);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}

}
