package view.effects;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TryApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		HBox root = new HBox();
		EffectsPane effects = new EffectsPane();
		root.getChildren().addAll(effects);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		

	}

}
