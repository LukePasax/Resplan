package Resplan;

import view.common.App;
import controller.general.Controller;
import controller.general.ControllerImpl;
import javafx.application.Application;

public class Starter {

    final static Controller controller = new ControllerImpl();

    public static Controller getController() {
        return controller;
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
