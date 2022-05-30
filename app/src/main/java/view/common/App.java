package view.common;

import Resplan.Starter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class App extends Application {
	
	private final static String SEP = System.getProperty("file.separator");
    
    static final ViewData viewData = new ViewDataImpl();
    
    public static ViewData getData() {
    	return viewData;
    }

    private Scene activeScene;
    private Parent sleepingRoot;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader planningLoader = new FXMLLoader(getClass().getResource("/view/PlanningView.fxml"));
        this.activeScene = new Scene(planningLoader.load());
        stage.setScene(activeScene);
        stage.getIcons().add(new Image("/images/icon.png"));
        stage.setTitle("Resplan");
        stage.show();
        FXMLLoader editLoader = new FXMLLoader(getClass().getResource("/view/EditView.fxml"));
        this.sleepingRoot = (Parent) editLoader.load();
        activeScene.setOnKeyPressed(this::switchScene);
    }

    private void switchScene(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.A)) {
            Parent temp = this.activeScene.getRoot();
            this.activeScene.setRoot(sleepingRoot);
            this.sleepingRoot = temp;
        }
    }
}
