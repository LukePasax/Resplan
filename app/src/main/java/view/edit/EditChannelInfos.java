package view.edit;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import view.common.ChannelInfosView;
import view.common.ViewDataImpl.Channel;

public class EditChannelInfos extends ChannelInfosView {

	private final static Paint muteColor = Paint.valueOf("#FF3333");
	private final static Paint soloColor = Paint.valueOf("#B8D4FF");
	
	//da sostituire con controller.
	private boolean muted;
	private boolean solo;

	public EditChannelInfos(Channel ch) {
		super(ch);
	}
	
	@Override
	public Node drawClipRegion() {
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
