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
import javafx.stage.StageStyle;
import view.edit.EditViewController;

public class App extends Application {
    
    static final ViewData viewData = new ViewDataImpl();
    
    public static ViewData getData() {
    	return viewData;
    }

    private Scene activeScene;
    private Parent sleepingRoot;
    
    private EditViewController editController;
    //private PlanningController planningController;
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader planningLoader = new FXMLLoader(getClass().getResource("/view/PlanningView.fxml"));
        this.activeScene = new Scene(planningLoader.load());
        stage.setScene(activeScene);
        stage.getIcons().add(new Image("/icons/icon.png"));
        stage.setTitle("Resplan");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        FXMLLoader editLoader = new FXMLLoader(getClass().getResource("/view/EditView.fxml"));
        this.sleepingRoot = editLoader.load();
        activeScene.setOnKeyPressed(this::switchScene);
        //this.planningController = planningLoader.getController();
        this.editController = editLoader.getController();
        Starter.getController().setApp(this);
        Starter.getController().loadViewData();
    }

    private void switchScene(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.A)) {
            Parent temp = this.activeScene.getRoot();
            this.activeScene.setRoot(this.sleepingRoot);
            this.sleepingRoot = temp;
        }
    }
    
    public void updatePlaybackTime(double time) {
    	editController.setPlaybackMarkerPosition(time);
    }
}
