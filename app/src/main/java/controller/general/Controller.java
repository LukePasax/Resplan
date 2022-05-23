package controller.general;

import controller.view.planning.PlanningController;
import daw.manager.ImportException;
import daw.manager.Manager;

import java.io.File;
import java.util.List;

public interface Controller {

    void updateView();

    void downloadProject();

    Manager loadProject();

    void setPlanningController(PlanningController planningController);

    void newPlanningChannel(String type, String title, String description) throws IllegalArgumentException;

    void newPlanningClip(String type, String title, String description, String channel, Double time, File content) throws IllegalArgumentException, ImportException;

    List<String> getChannelList();
}
