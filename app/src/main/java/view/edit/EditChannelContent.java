package view.edit;

import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import view.common.ChannelContentView;
import view.common.ViewDataImpl.Channel;
import view.common.ViewDataImpl.Clip;

public class EditChannelContent extends ChannelContentView {

	public EditChannelContent(Channel ch, NumberAxis axis) {
		super(ch, axis);
	}

	@Override
	public Node drawClipRegion(Clip clip) {
		return new Label(clip.getTitle());
	}

}
