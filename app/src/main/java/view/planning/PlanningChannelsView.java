package view.planning;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import view.common.ChannelsView;
import view.common.TimeAxisSetter;
import view.common.ToolBarSetter;
import view.common.ViewDataImpl;

public final class PlanningChannelsView extends ChannelsView {

    public static final int FONT_SIZE = 12;
    public static final int MIN_HEIGHT = 20;

    public PlanningChannelsView(final TimeAxisSetter timeAxisSetter, final VBox channelsContentPane,
                                final VBox channelsInfoPane, final ToolBarSetter toolBarSetter) {
        super(timeAxisSetter, channelsContentPane, channelsInfoPane, toolBarSetter);
    }

    @Override
    public Node drawClip(final ViewDataImpl.Clip clip) {
        return new Label(clip.getTitle());
    }

    @Override
    public Node drawInfos(final ViewDataImpl.Channel ch) {
        final Label groupLabel = new Label(ch.getGroup());
        groupLabel.setTextFill(Color.GRAY);
        groupLabel.setFont(Font.font(FONT_SIZE));
        final VBox pane  = new VBox(groupLabel);
        pane.setMinHeight(MIN_HEIGHT);
        return pane;
    }
}
