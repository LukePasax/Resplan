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
		
		
		Button zoomIn = new Button("+");
		Button zoomOut = new Button("-");
		
		//chart test

		x.setSide(Side.TOP);
		CategoryAxis y = new CategoryAxis();
		y.setSide(Side.RIGHT);
		
		ScatterChart<Number,String> c = new ScatterChart<>(x,y);
		
		Series<Number,String> s = new Series<>();
		Data<Number,String> d = new Data<>(10, "vocals");
		d.setNode(new Label("puttana"));
		s.getData().add(d);
		s.getData().add(new Data<>(30, "vocals"));
		s.getData().add(new Data<>(1, "vocals"));
		s.getData().add(new Data<>(10, "fx"));
		s.getData().add(new Data<>(12, "fx"));
		s.getData().add(new Data<>(5, "fx"));
		
		c.getData().add(s);
		//root.getChildren().addAll(c, zoomIn, zoomOut);
		
		zoomIn.setOnAction(e->{
			NumberAxis xa = (NumberAxis) c.getXAxis();
			x.setAutoRanging(false);
			xa.setLowerBound(xa.getLowerBound()+10);
			xa.setUpperBound(xa.getUpperBound()-10);
			
		});
		zoomOut.setOnAction(e->{
			NumberAxis xa = (NumberAxis) c.getXAxis();
			xa.setAutoRanging(true);
		});
		
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("FXML with controller");
        stage.show();
	}

}
