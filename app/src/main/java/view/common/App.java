package view.common;

import javafx.stage.Screen;
import resplan.Starter;
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

public final class App extends Application {

    static final ViewData VIEW_DATA = new ViewDataImpl();
    private Scene activeScene;
    private Parent sleepingRoot;
    private EditViewController editController;

    public static ViewData getData() {
    	return VIEW_DATA;
    }


    @Override
    public void start(final Stage stage) throws Exception {
        final FXMLLoader planningLoader = new FXMLLoader(getClass().getResource("/view/PlanningView.fxml"));
        this.activeScene = new Scene(planningLoader.load());
        stage.setScene(activeScene);
        stage.getIcons().add(new Image("/icons/icon.png"));
        stage.setTitle("resplan");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()*2/5);
        stage.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight()*2/5);
        stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth()*3/5);
        stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight()*3/5);
        ResizeHelper.addResizeListener(stage);
        stage.show();
        final FXMLLoader editLoader = new FXMLLoader(getClass().getResource("/view/EditView.fxml"));
        this.sleepingRoot = editLoader.load();
        activeScene.setOnKeyPressed(this::switchScene);
        //this.planningController = planningLoader.getController();
        this.editController = editLoader.getController();
        Starter.getController().setApp(this);
        Starter.getController().loadViewData();
    }

    private void switchScene(final KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.A)) {
            final Parent temp = this.activeScene.getRoot();
            this.activeScene.setRoot(this.sleepingRoot);
            this.sleepingRoot = temp;
        }
    }

    public void updatePlaybackTime(final double time) {
    	editController.setPlaybackMarkerPosition(time);
    }
}
