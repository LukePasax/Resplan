package view.DAW;

import java.io.FileInputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DAW extends Application {
	
	private final static String SEP = System.getProperty("file.separator");

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		String path = System.getProperty("user.dir") + SEP + "src" + SEP + "main" + SEP + "resources" + SEP + "view" + SEP + "DAWView.fxml";
		FileInputStream fxmlStream = new FileInputStream(path);
		VBox root = (VBox) loader.load(fxmlStream);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("FXML with controller");
        stage.show();
	}

}
