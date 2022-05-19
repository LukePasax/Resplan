package controller.general;

import daw.manager.Manager;

public interface Controller {

    void updateView();

    void saveProject();

    Manager loadProject();

    void newPlanningChannel(String type, String title, String description) throws IllegalArgumentException;

    void newPlanningClip() throws IllegalArgumentException;

}
