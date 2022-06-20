package view.common;

import Resplan.Starter;
import controller.general.DownloadingException;
import controller.general.LoadingException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import static javafx.scene.layout.Region.USE_PREF_SIZE;

import java.io.IOException;

public class WindowBar {

    private static final double ICON_SIZE = 25;
    private static final double BUTTON_SIZE = 50;
    private static final double BUTTON_ICON_SIZE = 15;
    private static final double LABEL_WIDTH = 100;
    private final AnchorPane pane;
    private FilePicker filePicker;
    private final Label projectName;

    private static boolean IS_MAXIMIZED;
    private static double POS_X;
    private static double POS_Y;
    private static double WIDTH;
    private static double HEIGHT;
    private double offsetX;
    private double offsetY;

    public WindowBar(AnchorPane pane) {
        super();
        this.pane = pane;
        pane.getStylesheets().add("/stylesheets/planning.css");
        pane.setPrefHeight(ICON_SIZE);
        pane.setMaxHeight(USE_PREF_SIZE);
        ImageView resplanIcon = new ImageView("/icons/icon.png");
        resplanIcon.setFitHeight(ICON_SIZE);
        resplanIcon.setFitWidth(ICON_SIZE);
        AnchorPane.setLeftAnchor(resplanIcon,0.0);
        AnchorPane.setTopAnchor(resplanIcon, 0.0);
        MenuItem saveProject = new MenuItem("Save Project");
        saveProject.setOnAction(this::saveProject);
        MenuItem newProject = new MenuItem("New Project");
        newProject.setOnAction(this::newProject);
        MenuItem openProject = new MenuItem("Open Project");
        openProject.setOnAction(this::openProject);
        MenuItem closeProject = new MenuItem("Close Project");
        closeProject.setOnAction(this::closeProject);
        MenuItem setTemplate = new MenuItem("Set current Project as Template");
        setTemplate.setOnAction(this::setTemplate);
        MenuItem resetTemplate = new MenuItem("Reset Template");
        resetTemplate.setOnAction(this::resetTemplate);
        MenuItem export = new MenuItem("Export");
        export.setOnAction(this::export);
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(saveProject, new SeparatorMenuItem(), newProject, new SeparatorMenuItem(),
                openProject, new SeparatorMenuItem(), closeProject, new SeparatorMenuItem(),
                setTemplate, new SeparatorMenuItem(), resetTemplate, new SeparatorMenuItem(), export);
        MenuItem newChannel = new MenuItem("New Channel");
        newChannel.setOnAction(this::newChannel);
        MenuItem newClip = new MenuItem("New Clip");
        newClip.setOnAction(this::newClip);
        MenuItem newSection = new MenuItem("New Section");
        newSection.setOnAction(this::newSection);
        Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(newChannel, new SeparatorMenuItem(), newClip, new SeparatorMenuItem(), newSection);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, editMenu);
        AnchorPane.setLeftAnchor(menuBar, ICON_SIZE -1);
        AnchorPane.setRightAnchor(menuBar, 0.0);
        menuBar.setOnMousePressed(this::mousePressed);
        menuBar.setOnMouseDragged(this::mouseDragged);
        this.projectName = new Label("Resplan");
        this.projectName.setPrefWidth(LABEL_WIDTH);
        this.projectName.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(this.projectName, 0.0);
        AnchorPane.setBottomAnchor(this.projectName, 0.0);
        AnchorPane.setLeftAnchor(this.projectName, 295.0);
        AnchorPane.setRightAnchor(this.projectName, 295.0);
        Button closeButton = new Button("");
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
        closeButton.setStyle("-fx-background-color: linear-gradient(to bottom, orange, darkorange 50%, coral 100%)");
        closeButton.setOnAction(this::close);
        Button maximizeButton = new Button("");
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
        maximizeButton.setStyle("-fx-background-color: linear-gradient(to bottom, orange, darkorange 50%, coral 100%)");
        maximizeButton.setOnAction(this::maximize);
        Button minimizeButton = new Button("");
        icon = new ImageView("/icons/minimize-window.png");
        icon.setFitWidth(BUTTON_ICON_SIZE);
        icon.setFitHeight(BUTTON_ICON_SIZE);
        minimizeButton.setGraphic(icon);
        minimizeButton.setPrefWidth(BUTTON_SIZE);
        minimizeButton.setMinWidth(USE_PREF_SIZE);
        minimizeButton.setMinWidth(USE_PREF_SIZE);
        AnchorPane.setRightAnchor(minimizeButton, 2*BUTTON_SIZE);
        AnchorPane.setTopAnchor(minimizeButton, 0.0);
        AnchorPane.setBottomAnchor(minimizeButton, 0.0);
        minimizeButton.setStyle("-fx-shape: rectangle");
        minimizeButton.setStyle("-fx-background-color: linear-gradient(to bottom, orange, darkorange 50%, coral 100%)");
        minimizeButton.setOnAction(this::minimize);
        closeButton.hoverProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue) {
                closeButton.setStyle("-fx-background-color: red");
            } else {
                closeButton.setStyle("-fx-background-color: linear-gradient(to bottom, orange, darkorange 50%, coral 100%)");
            }
        }));
        maximizeButton.hoverProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue) {
                maximizeButton.setStyle("-fx-background-color: orange");
            } else {
                maximizeButton.setStyle("-fx-background-color: linear-gradient(to bottom, orange, darkorange 50%, coral 100%)");
            }
        }));
        minimizeButton.hoverProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue) {
                minimizeButton.setStyle("-fx-background-color: orange");
            } else {
                minimizeButton.setStyle("-fx-background-color: linear-gradient(to bottom, orange, darkorange 50%, coral 100%)");
            }
        }));
        pane.getChildren().addAll(resplanIcon, menuBar, this.projectName, minimizeButton, maximizeButton, closeButton);
        pane.setOnMousePressed(this::mousePressed);
        pane.setOnMouseDragged(this::mouseDragged);
    }

    private void close(ActionEvent actionEvent) {
        Starter.getController().stop();
        this.pane.getScene().getWindow().hide();
    }

    private void maximize(ActionEvent actionEvent) {
        Stage stage = (Stage) this.pane.getScene().getWindow();
        if (!IS_MAXIMIZED) {
            POS_X = stage.getX();
            POS_Y = stage.getY();
            WIDTH = stage.getWidth();
            HEIGHT = stage.getHeight();
            final var screen = Screen.getPrimary();
            final var bounds = screen.getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            IS_MAXIMIZED = true;
        } else {
            stage.setX(POS_X);
            stage.setY(POS_Y);
            stage.setWidth(WIDTH);
            stage.setHeight(HEIGHT);
            IS_MAXIMIZED = false;
        }
    }

    private void minimize(ActionEvent actionEvent) {
        Stage stage = (Stage) this.pane.getScene().getWindow();
        stage.setIconified(true);
    }

    private void saveProject(ActionEvent actionEvent) {
        try {
            Starter.getController().save();
        } catch (DownloadingException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        } catch (IllegalStateException e) {
            this.filePicker =  new JsonFilePicker();
            try {
                Starter.getController().saveWithName(this.filePicker.getFileChooser().showSaveDialog(this.pane.getScene().getWindow()));
            } catch (DownloadingException ex) {
                AlertDispatcher.dispatchError(e.getLocalizedMessage());
            }
        }
    }

    public void newProject(ActionEvent event) {
        Starter.getController().newProject();
    }

    public void openProject(ActionEvent event) {
        this.filePicker = new JsonFilePicker();
        try {
            Starter.getController().openProject(this.filePicker.getFileChooser().showOpenDialog(this.pane.getScene().getWindow()));
        } catch (LoadingException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void closeProject(ActionEvent event) {
        this.pane.getScene().getWindow().hide();
    }

    public void setTemplate(ActionEvent event) {
        try {
            Starter.getController().setTemplateProject();
        } catch (DownloadingException | IllegalStateException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void resetTemplate(ActionEvent event) {
        try {
            Starter.getController().resetTemplateProject();
        } catch (DownloadingException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    public void export(ActionEvent actionEvent) {
        this.launchWindow("view/ExportView.fxml","Export");
    }

    public void newChannel(ActionEvent event)  {
        this.launchWindow("view/NewChannelWindow.fxml","New Channel");
    }

    public void newClip(ActionEvent event) {
        if (Starter.getController().getChannelList().isEmpty()) {
            AlertDispatcher.dispatchError("No channels present");
        } else {
            this.launchWindow("view/NewClipWindow.fxml","New Clip");
        }
    }

    public void newSection(ActionEvent actionEvent) {
        this.launchWindow("view/NewSectionWindow.fxml","New Section");
    }

    private void launchWindow(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource(fxml));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
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

    private void mousePressed(MouseEvent mouseEvent) {
        this.offsetX = mouseEvent.getSceneX();
        this.offsetY = mouseEvent.getSceneY();
    }

    private void mouseDragged(MouseEvent mouseEvent) {
        Stage stage = (Stage) this.pane.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - this.offsetX);
        stage.setY(mouseEvent.getScreenY() - this.offsetY);
    }

}
