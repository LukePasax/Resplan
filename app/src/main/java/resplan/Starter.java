package resplan;

import view.common.App;
import controller.general.Controller;
import controller.general.ControllerImpl;
import javafx.application.Application;

public final class Starter {

    final static Controller CONTROLLER = new ControllerImpl();

    private Starter() {}

    public static Controller getController() {
        return CONTROLLER;
    }

    public static void main( final String[] args) {
        Application.launch(App.class, args);
    }
}
