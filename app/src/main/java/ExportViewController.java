import Resplan.Starter;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import view.common.AlertDispatcher;
import view.common.NumberFormatConverter;
import view.common.WavFilePicker;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExportViewController {
    public TextField fileName;
    public Button pickFile;
    public Button okButton;
    public TextField endTime;
    public CheckBox projectCheck;
    public TextField startTime;

    private ProgressBar progressBar;
    private AnchorPane pane;
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

    public void pickFilePressed(ActionEvent actionEvent) {
        WavFilePicker filePicker = new WavFilePicker();
        this.file = filePicker.getFileChooser().showSaveDialog(this.fileName.getScene().getWindow());
        if (this.file != null) {
            this.fileName.setText(file.getAbsolutePath());
        }
    }

    public void okPressed(ActionEvent actionEvent) {
        if (this.file == null) {
            AlertDispatcher.dispatchError("Select a file first");
        } else {
            this.window = fileName.getScene().getWindow();
            try {
                if (this.projectCheck.isSelected()) {
                    Double tickTime = Starter.getController().getProjectLength() / 100;
                    this.startProgressBar(Starter.getController().getProjectLength(), tickTime);
                    Starter.getController().startExport(0d);
                } else {
                    Double startTime =  this.converter.fromString(this.startTime.getText()).doubleValue();
                    Double endTime =  this.converter.fromString(this.endTime.getText()).doubleValue();
                    if (endTime > startTime) {
                        Double tickTime = (endTime - startTime) / 100;
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

    private void startProgressBar(Double duration, Double tickTime) {
        this.pane = new AnchorPane();
        this.progressBar = new ProgressBar(this.progress);
        AnchorPane.setTopAnchor(this.progressBar,140.0);
        AnchorPane.setBottomAnchor(this.progressBar,140.0);
        AnchorPane.setRightAnchor(this.progressBar,10.0);
        AnchorPane.setLeftAnchor(this.progressBar,10.0);
        this.pane.getChildren().add(this.progressBar);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        this.fileName.getScene().setRoot(this.pane);
        this.window = this.progressBar.getScene().getWindow();
        ScheduledFuture<?> handler = executor.scheduleAtFixedRate(
                this::updateProgress, 0, tickTime.longValue(), TimeUnit.MILLISECONDS);
        Runnable canceller = () -> {
            handler.cancel(true);
            try {
                Starter.getController().stopExport(this.file);
                this.window.hide();
            } catch (IOException e) {
                AlertDispatcher.dispatchError(e.getLocalizedMessage());
            }
        };
        executor.schedule(canceller, duration.longValue(), TimeUnit.MILLISECONDS);
    }

    private void updateProgress() {
        this.progress += 0.01;
        this.progressBar.setProgress(this.progress);
    }

    public void onCheck(ActionEvent actionEvent) {
        if (this.projectCheck.isSelected()) {
            this.startTime.setDisable(true);
            this.endTime.setDisable(true);
        } else  {
            this.startTime.setDisable(false);
            this.endTime.setDisable(false);
        }
    }
}
