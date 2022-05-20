package controller.general;

import controller.view.planning.PlanningController;
import daw.manager.Manager;

public interface Controller {

    void updateView();

    void downloadProject();

    Manager loadProject();

    void setPlanningController(PlanningController planningController);

    void newPlanningChannel(String type, String title, String description) throws IllegalArgumentException;

    void newPlanningClip() throws IllegalArgumentException;

}
