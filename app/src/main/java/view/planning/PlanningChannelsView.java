package view.planning;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import view.common.ChannelsView;
import view.common.TimeAxisSetter;
import view.common.ViewDataImpl;

public class PlanningChannelsView extends ChannelsView {
    public PlanningChannelsView(TimeAxisSetter timeAxisSetter, VBox channelsContentPane, VBox channelsInfoPane) {
        super(timeAxisSetter, channelsContentPane, channelsInfoPane);
    }

    @Override
    public Node drawClip(ViewDataImpl.Clip clip) {
        return new Label(clip.getTitle());
    }

    @Override
    public Node drawInfos(ViewDataImpl.Channel ch) {
        FlowPane pane  = new FlowPane();
        pane.setMinHeight(20);
        return pane;
    }
}
