package controller.view;

import controller.general.Controller;
import controller.general.ControllerImpl;
import daw.manager.Manager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class planningApp extends Application {

    final static Controller controller = new ControllerImpl();

    public static Controller getController() {
        return controller;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader planningLoader = new FXMLLoader(getClass().getResource("/view/planningView.fxml"));
        Scene scene = new Scene(planningLoader.load());
        planningApp.getController().setPlanningController(planningLoader.getController());
        stage.setScene(scene);
        stage.getIcons().add(new Image("images/icon.png"));
        stage.show();
    }
}
