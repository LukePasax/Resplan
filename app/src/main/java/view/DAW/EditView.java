package view.DAW;

import java.io.FileInputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditView extends Application {
	
	private final static String SEP = System.getProperty("file.separator");
		NumberAxis x = new NumberAxis();
		
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		String path = System.getProperty("user.dir") + SEP + "src" + SEP + "main" + SEP + "resources" + SEP + "view" + SEP + "EditView.fxml";
		FileInputStream fxmlStream = new FileInputStream(path);
		VBox root = (VBox) loader.load(fxmlStream);
		
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Resplan");
        stage.show();
	}

}
