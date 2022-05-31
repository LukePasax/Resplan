package view.effects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TryApp extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		
		HBox root = new HBox();
		CompressorPane cpane = new CompressorPane();
		LimiterPane lpane = new LimiterPane();
		PassPane ppane = new PassPane();
		ReverbPane rpane = new ReverbPane();
		
		root.getChildren().addAll(cpane, lpane, ppane, rpane);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}

}
