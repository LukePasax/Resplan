package view.edit;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import view.common.ChannelsView;
import view.common.TimeAxisSetter;
import view.common.ViewDataImpl.Channel;
import view.common.ViewDataImpl.Clip;

public class EditChannelsView extends ChannelsView {
	
	private final static Paint muteColor = Paint.valueOf("#FF3333");
	private final static Paint soloColor = Paint.valueOf("#B8D4FF");
	
	//da sostituire con controller.
	private boolean muted;
	private boolean solo;
	
	public EditChannelsView(TimeAxisSetter timeAxisSetter, VBox channelsContentPane, VBox channelsInfoPane) {
		super(timeAxisSetter, channelsContentPane, channelsInfoPane);
	}
	
	@Override
	public Node drawClip(Clip clip) {
		return new EditClipView(clip);
	}

	@Override
	public Node drawInfos(Channel ch) {
		//buttons
		Button muteButton = new Button("M");
		Button soloButton = new Button("S");
		muteButton.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		soloButton.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		muteButton.setOnAction(e->{
			if(!solo) {
				muted = !muted;
			}
			if(muted) {
				muteButton.setBackground(new Background(new BackgroundFill(muteColor, null, null)));
			} else {
				muteButton.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			}
		});
		soloButton.setOnAction(e->{
			solo = !solo;
			if(solo) {
				soloButton.setBackground(new Background(new BackgroundFill(soloColor, null, null)));
			} else {
				soloButton.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			}
		});
		
		return new FlowPane(muteButton, soloButton);
	}
	
}
