package view.planning;

import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import view.common.RegionHeightResizer;

public class ChannelInfoPane extends Pane {

    private final static Paint borderColor = Paint.valueOf("#999999");

    public ChannelInfoPane(String channelName) {
        this.setBorder(new Border(new BorderStroke(null, null, borderColor, null,
                null, null, BorderStrokeStyle.SOLID, null, CornerRadii.EMPTY, null, null)));
        RegionHeightResizer.makeResizable(this);
        Label channelNameLabel = new Label(channelName);
        this.getChildren().add(new VBox(channelNameLabel));
    }
}
