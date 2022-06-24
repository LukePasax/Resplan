package view.common;

import controller.general.DownloadingException;
import controller.general.LoadingException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import resplan.Starter;
import java.io.IOException;
import static javafx.scene.layout.Region.USE_PREF_SIZE;

public final class WindowBar {

    private static final double ICON_SIZE = 25;
    private static final double BUTTON_SIZE = 50;
    private static final double BUTTON_ICON_SIZE = 15;
    private static final double LABEL_WIDTH = 100;
    public static final String GRADIENT = "-fx-background-color: linear-gradient(to bottom, orange, darkorange 50%, coral 100%)";
    public static final String RED = "-fx-background-color: red";
    public static final String ORANGE = "-fx-background-color: orange";
    public static final double HORIZONTAL_ANCHOR = 295.0;
    private final AnchorPane pane;
    private FilePicker filePicker;

    private static boolean isMaximized;
    private static double posX;
    private static double posY;
    private static double width;
    private static double height;
    private double offsetX;
    private double offsetY;
    private static boolean dragging;


    public WindowBar(final AnchorPane pane) {
        super();
        this.pane = pane;
        pane.getStylesheets().add("/stylesheets/resplan.css");
        pane.setPrefHeight(ICON_SIZE);
        pane.setMaxHeight(USE_PREF_SIZE);
        final ImageView resplanIcon = new ImageView("/icons/icon.png");
        resplanIcon.setFitHeight(ICON_SIZE);
        resplanIcon.setFitWidth(ICON_SIZE);
        AnchorPane.setLeftAnchor(resplanIcon, 0.0);
        AnchorPane.setTopAnchor(resplanIcon, 0.0);
        final MenuItem saveProject = new MenuItem("Save Project");
        saveProject.setOnAction(event -> this.saveProject());
        final MenuItem newProject = new MenuItem("New Project");
        newProject.setOnAction(event -> this.newProject());
        final MenuItem openProject = new MenuItem("Open Project");
        openProject.setOnAction(event -> this.openProject());
        final MenuItem closeProject = new MenuItem("Close Project");
        closeProject.setOnAction(event -> this.closeProject());
        final MenuItem setTemplate = new MenuItem("Set current Project as Template");
        setTemplate.setOnAction(event -> this.setTemplate());
        final MenuItem resetTemplate = new MenuItem("Reset Template");
        resetTemplate.setOnAction(event -> this.resetTemplate());
        final MenuItem export = new MenuItem("Export");
        export.setOnAction(event -> this.export());
        final Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(saveProject, newProject, openProject, closeProject, setTemplate, resetTemplate, export);
        final MenuItem newChannel = new MenuItem("New Channel");
        newChannel.setOnAction(event -> this.newChannel());
        final MenuItem newClip = new MenuItem("New Clip");
        newClip.setOnAction(event -> this.newClip());
        final MenuItem newSection = new MenuItem("New Section");
        newSection.setOnAction(event -> this.newSection());
        final Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(newChannel, newClip, newSection);
        final MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, editMenu);
        AnchorPane.setLeftAnchor(menuBar, ICON_SIZE - 1);
        AnchorPane.setRightAnchor(menuBar, 0.0);
        menuBar.setOnMousePressed(this::mousePressed);
        menuBar.setOnMouseDragged(this::mouseDragged);
        final Label projectName = new Label("Resplan");
        projectName.setPrefWidth(LABEL_WIDTH);
        projectName.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(projectName, 0.0);
        AnchorPane.setBottomAnchor(projectName, 0.0);
        AnchorPane.setLeftAnchor(projectName, HORIZONTAL_ANCHOR);
        AnchorPane.setRightAnchor(projectName, HORIZONTAL_ANCHOR);
        final Button closeButton = new Button("");
        ImageView icon = new ImageView("/icons/close-window.jpg");
        icon.setFitWidth(BUTTON_ICON_SIZE);
        icon.setFitHeight(BUTTON_ICON_SIZE);
        closeButton.setGraphic(icon);
        closeButton.setPrefWidth(BUTTON_SIZE);
        closeButton.setMinWidth(USE_PREF_SIZE);
        closeButton.setMinWidth(USE_PREF_SIZE);
        AnchorPane.setRightAnchor(closeButton, 0.0);
        AnchorPane.setTopAnchor(closeButton, 0.0);
        AnchorPane.setBottomAnchor(closeButton, 0.0);
        closeButton.setStyle("-fx-shape: rectangle");
        closeButton.setStyle(GRADIENT);
        closeButton.setOnAction(event -> this.close());
        final Button maximizeButton = new Button("");
        icon = new ImageView("/icons/maximize-window.jpg");
        icon.setFitWidth(BUTTON_ICON_SIZE);
        icon.setFitHeight(BUTTON_ICON_SIZE);
        maximizeButton.setGraphic(icon);
        maximizeButton.setPrefWidth(BUTTON_SIZE);
        maximizeButton.setMinWidth(USE_PREF_SIZE);
        maximizeButton.setMinWidth(USE_PREF_SIZE);
        AnchorPane.setRightAnchor(maximizeButton, BUTTON_SIZE);
        AnchorPane.setTopAnchor(maximizeButton, 0.0);
        AnchorPane.setBottomAnchor(maximizeButton, 0.0);
        maximizeButton.setStyle("-fx-shape: rectangle");
        maximizeButton.setStyle(GRADIENT);
        maximizeButton.setOnAction(event -> this.maximize());
        final Button minimizeButton = new Button("");
        icon = new ImageView("/icons/minimize-window.png");
        icon.setFitWidth(BUTTON_ICON_SIZE);
        icon.setFitHeight(BUTTON_ICON_SIZE);
        minimizeButton.setGraphic(icon);
        minimizeButton.setPrefWidth(BUTTON_SIZE);
        minimizeButton.setMinWidth(USE_PREF_SIZE);
        minimizeButton.setMinWidth(USE_PREF_SIZE);
        AnchorPane.setRightAnchor(minimizeButton, 2 * BUTTON_SIZE);
        AnchorPane.setTopAnchor(minimizeButton, 0.0);
        AnchorPane.setBottomAnchor(minimizeButton, 0.0);
        minimizeButton.setStyle("-fx-shape: rectangle");
        minimizeButton.setStyle(GRADIENT);
        minimizeButton.setOnAction(event -> this.minimize());
        closeButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                closeButton.setStyle(RED);
            } else {
                closeButton.setStyle(GRADIENT);
            }
        });
        maximizeButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                maximizeButton.setStyle(ORANGE);
            } else {
                maximizeButton.setStyle(GRADIENT);
            }
        });
        minimizeButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                minimizeButton.setStyle(ORANGE);
            } else {
                minimizeButton.setStyle(GRADIENT);
            }
        });
        pane.getChildren().addAll(resplanIcon, menuBar, projectName, minimizeButton, maximizeButton, closeButton);
        pane.setOnMousePressed(this::mousePressed);
        pane.setOnMouseDragged(this::mouseDragged);
    }

    public static boolean isDragging() {
        return dragging;
    }

    private void close() {
        Starter.getController().stop();
        this.pane.getScene().getWindow().hide();
    }

    private void maximize() {
        final Stage stage = (Stage) this.pane.getScene().getWindow();
        if (!isMaximized) {
            posX = stage.getX();
            posY = stage.getY();
            width = stage.getWidth();
            height = stage.getHeight();
            final var screen = Screen.getPrimary();
            final var bounds = screen.getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            isMaximized = true;
        } else {
            stage.setX(posX);
            stage.setY(posY);
            stage.setWidth(width);
            stage.setHeight(height);
            isMaximized = false;
        }
    }

    private void minimize() {
        final Stage stage = (Stage) this.pane.getScene().getWindow();
        stage.setIconified(true);
    }

    private void saveProject() {
        try {
            Starter.getController().save();
        } catch (DownloadingException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        } catch (IllegalStateException e) {
            this.filePicker =  new JsonFilePicker();
            try {
                final var file = this.filePicker.getFileChooser().showSaveDialog(this.pane.getScene().getWindow());
                if (file != null) {
                    Starter.getController().saveWithName(file);
                }
            } catch (DownloadingException ex) {
                AlertDispatcher.dispatchError(e.getLocalizedMessage());
            }
        }
    }

    public void newProject() {
        Starter.getController().newProject();
    }

    public void openProject() {
        this.filePicker = new JsonFilePicker();
        try {
            final var file = this.filePicker.getFileChooser().showOpenDialog(this.pane.getScene().getWindow());
            if (file != null) {
                Starter.getController().openProject(file);
            }
        } catch (LoadingException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void closeProject() {
        this.pane.getScene().getWindow().hide();
    }

    public void setTemplate() {
        try {
            Starter.getController().setTemplateProject();
        } catch (DownloadingException | IllegalStateException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void resetTemplate() {
        try {
            Starter.getController().resetTemplateProject();
        } catch (DownloadingException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void export() {
        this.launchWindow("view/ExportView.fxml", "Export");
    }

    public void newChannel()  {
        this.launchWindow("view/NewChannelWindow.fxml", "New Channel");
    }

    public void newClip() {
        if (Starter.getController().getChannelList().isEmpty()) {
            AlertDispatcher.dispatchError("No channels present");
        } else {
            this.launchWindow("view/NewClipWindow.fxml", "New Clip");
        }
    }

    public void newSection() {
        this.launchWindow("view/NewSectionWindow.fxml", "New Section");
    }

    private void launchWindow(final String fxml, final String title) {
        try {
            final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource(fxml));
            final Scene scene = new Scene(loader.load());
            final Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle(title);
            stage.initOwner(this.pane.getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mousePressed(final MouseEvent mouseEvent) {
        this.offsetX = mouseEvent.getSceneX();
        this.offsetY = mouseEvent.getSceneY();
    }

    private void mouseDragged(final MouseEvent mouseEvent) {
        if (!ResizeHelper.ResizeListener.isResizing()) {
            dragging = true;
            final Stage stage = (Stage) this.pane.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX() - this.offsetX);
            stage.setY(mouseEvent.getScreenY() - this.offsetY);

        }
    }

}
