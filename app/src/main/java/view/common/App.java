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
    private static final double MIN_RATIO = 0.6;
    private static final double OPEN_RATIO = 0.7;

    public static double getMinHeight() {
        return MIN_RATIO * Screen.getPrimary().getVisualBounds().getHeight();
    }

    public static double getMinWidth() {
        return MIN_RATIO * Screen.getPrimary().getVisualBounds().getWidth();
    }

    public static ViewData getData() {
    	return VIEW_DATA;
    }

    @Override
    public void start(final Stage stage) throws Exception {
        final FXMLLoader planningLoader = new FXMLLoader(getClass().getResource("/view/PlanningView.fxml"));
        this.activeScene = new Scene(planningLoader.load());
        final FXMLLoader editLoader = new FXMLLoader(getClass().getResource("/view/EditView.fxml"));
        this.sleepingRoot = editLoader.load();
        activeScene.setOnKeyPressed(this::switchSceneListener);
        this.editController = editLoader.getController();
        stage.setScene(activeScene);
        stage.getIcons().add(new Image("/icons/icon.png"));
        stage.setTitle("resplan");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth() * OPEN_RATIO);
        stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight() * OPEN_RATIO);
        ResizeHelper.addResizeListener(stage);
        this.switchScene();
        ResizeHelper.addResizeListener(stage);
        this.switchScene();
        stage.show();
        Starter.getController().setApp(this);
        Starter.getController().loadViewData();
    }

    private void switchSceneListener(final KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.A)) {
            this.switchScene();
        }
    }

    private void switchScene() {
        final Parent temp = this.activeScene.getRoot();
        this.activeScene.setRoot(this.sleepingRoot);
        this.sleepingRoot = temp;
    }

    public void updatePlaybackTime(final double time) {
    	editController.setPlaybackMarkerPosition(time);
    }
}
