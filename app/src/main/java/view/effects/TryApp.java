package view.effects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TryApp extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		
		VBox root = new VBox();
		KnobPane knobpane = new KnobPane(0, 100);
		root.getChildren().addAll(knobpane);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}

}
