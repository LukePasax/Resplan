package view.planning;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import resplan.Starter;
import view.common.AlertDispatcher;
import view.common.NumberFormatConverter;
import view.common.WavFilePicker;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class ExportViewController {

    public static final double VERTICAL_ANCHOR = 140.0;
    public static final double HORIZONTAL_ANCHOR = 10.0;
    public static final double PROGRESS = 0.01;
    @FXML
    private TextField fileName;
    @FXML
    private TextField endTime;
    @FXML
    private CheckBox projectCheck;
    @FXML
    private TextField startTime;

    private ProgressBar progressBar;
    private File file;
    private NumberFormatConverter converter;
    private Double progress;
    private Window window;

    public void initialize() {
        this.converter = new NumberFormatConverter();
        this.endTime.setTextFormatter(new TextFormatter<>(this.converter.getFormatterUnaryOperator()));
        this.startTime.setTextFormatter(new TextFormatter<>(this.converter.getFormatterUnaryOperator()));
        this.projectCheck.setSelected(true);
        this.startTime.setDisable(true);
        this.endTime.setDisable(true);
        this.progress = 0.0;
    }

    public void pickFilePressed() {
        final WavFilePicker filePicker = new WavFilePicker();
        this.file = filePicker.getFileChooser().showSaveDialog(this.fileName.getScene().getWindow());
        if (this.file != null) {
            this.fileName.setText(file.getAbsolutePath());
        }
    }

    public void okPressed() {
        if (this.file == null) {
            AlertDispatcher.dispatchError("Select a file first");
        } else {
            this.window = this.fileName.getScene().getWindow();
            try {
                if (this.projectCheck.isSelected()) {
                    final Double tickTime = Starter.getController().getProjectLength() / 100;
                    this.startProgressBar(Starter.getController().getProjectLength(), tickTime);
                    Starter.getController().startExport(0d);
                } else {
                    final Double startTime =  this.converter.fromString(this.startTime.getText()).doubleValue();
                    final Double endTime =  this.converter.fromString(this.endTime.getText()).doubleValue();
                    if (endTime > startTime) {
                        final Double tickTime = (endTime - startTime) / 100;
                        this.startProgressBar(endTime - startTime, tickTime);
                        Starter.getController().startExport(startTime);
                    } else {
                        AlertDispatcher.dispatchError("Invalid time Stamps");
                    }
                }
            } catch (InterruptedException e) {
                AlertDispatcher.dispatchError(e.getLocalizedMessage());
            }
        }
    }

    private void startProgressBar(final Double duration, final Double tickTime) {
        final AnchorPane pane = new AnchorPane();
        this.progressBar = new ProgressBar(this.progress);
        this.progressBar.progressProperty().addListener(this::changed);
        AnchorPane.setTopAnchor(this.progressBar, VERTICAL_ANCHOR);
        AnchorPane.setBottomAnchor(this.progressBar, VERTICAL_ANCHOR);
        AnchorPane.setRightAnchor(this.progressBar, HORIZONTAL_ANCHOR);
        AnchorPane.setLeftAnchor(this.progressBar, HORIZONTAL_ANCHOR);
        pane.getChildren().add(this.progressBar);
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        this.fileName.getScene().setRoot(pane);
        this.window = this.progressBar.getScene().getWindow();
        final ScheduledFuture<?> handler = executor.scheduleAtFixedRate(
                this::updateProgress, 0, tickTime.longValue(), TimeUnit.MILLISECONDS);
        executor.schedule(() -> cancelBar(handler, executor), duration.longValue(), TimeUnit.MILLISECONDS);
    }

    private void cancelBar(final ScheduledFuture<?> handler, final ScheduledExecutorService executor) {
        try {
            Starter.getController().stopExport(this.file);
        } catch (IOException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
        handler.cancel(false);
        executor.shutdown();
    }

    public void changed(final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue) {
        if (newValue.doubleValue() >= 1.0) {
            Platform.runLater(() -> this.window.hide());
        }
    }

    private void updateProgress() {
        this.progress += PROGRESS;
        this.progressBar.setProgress(this.progress);
    }

    public void onCheck() {
        if (this.projectCheck.isSelected()) {
            this.startTime.setDisable(true);
            this.endTime.setDisable(true);
        } else  {
            this.startTime.setDisable(false);
            this.endTime.setDisable(false);
        }
    }

    public void cancelPressed() {
        this.endTime.getScene().getWindow().hide();
    }
}
