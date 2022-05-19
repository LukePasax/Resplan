package controller.general;

public interface Controller {

    void updateView();

    void saveProject();

    void newPlanningChannel(String type, String title, String description) throws IllegalArgumentException;

    void newPlanningClip() throws IllegalArgumentException;
}
