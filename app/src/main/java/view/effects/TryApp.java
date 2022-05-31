package view.effects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TryApp extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		
		EffectsPane root = new EffectsPane();

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}

}
