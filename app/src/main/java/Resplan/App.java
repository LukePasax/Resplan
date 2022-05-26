package Resplan;

import java.io.FileInputStream;

import controller.general.Controller;
import controller.general.ControllerImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.common.ViewDataImpl;

public class App extends Application {
	
	private final static String SEP = System.getProperty("file.separator");
	
    final static Controller controller = new ControllerImpl();

    public static Controller getController() {
        return controller;
    }
    
    static final ViewDataImpl viewData = new ViewDataImpl();
    
    public static ViewDataImpl getData() {
    	return viewData;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader planningLoader = new FXMLLoader(getClass().getResource("/view/planningView.fxml"));
        Scene planningScene = new Scene(planningLoader.load());
        App.getController().setPlanningController(planningLoader.getController());
        stage.setScene(planningScene);
        stage.getIcons().add(new Image("images/icon.png"));
        stage.setTitle("Resplan");
        stage.show(); 
        
        FXMLLoader loader = new FXMLLoader();
        String path = System.getProperty("user.dir") + SEP + "src" + SEP + "main" + SEP + "resources" + SEP + "view" + SEP + "EditView.fxml";
		FileInputStream fxmlStream = new FileInputStream(path);
		VBox root = (VBox) loader.load(fxmlStream);
		
        Scene editScene = new Scene(root);
        planningScene.setOnKeyPressed(e->{
        	if(e.getCode().equals(KeyCode.A)) {
        		stage.setScene(editScene);
        	}
        	
        });
        editScene.setOnKeyPressed(e->{
        	if(e.getCode().equals(KeyCode.A)) {
            	stage.setScene(planningScene);
        	}
        });
    }
}
