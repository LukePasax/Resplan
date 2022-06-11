package view.effects;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TryApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		KnobPane root = new KnobPane(0,100,5);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		

	}

}
