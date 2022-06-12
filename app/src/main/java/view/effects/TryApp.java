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
		ContinuousKnobPane cont = new ContinuousKnobPane(0.2,Double.POSITIVE_INFINITY,5, "Volume");
		JerkyKnobPane jerky = new JerkyKnobPane(List.of("1:1", "2:2", "3:3", "4:4"), "Ratio");
		CompressorPane comp = new CompressorPane(-100.0, 0.0);
		comp.setValue(-60);
		root.getChildren().addAll(cont, jerky, comp);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		

	}

}
