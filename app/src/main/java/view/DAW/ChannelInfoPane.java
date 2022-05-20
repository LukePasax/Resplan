package view.DAW;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ChannelInfoPane extends Pane {
	
	private final static Paint borderColor = Paint.valueOf("#999999");
	private final static Paint muteColor = Paint.valueOf("#FF3333");
	private final static Paint soloColor = Paint.valueOf("#B8D4FF");
	
	private final Label channelNameLabel;
	
	private final Button muteButton = new Button("M");
	
	private final Button soloButton = new Button("S");
	
	//da sostituire con controller.
	private boolean muted;
	private boolean solo;

	public ChannelInfoPane(String channelName) {
		super();
		//borderLayout
		this.setBorder(new Border(new BorderStroke(null, null, borderColor, null, 
				null, null, BorderStrokeStyle.SOLID, null, CornerRadii.EMPTY, null, null)));
		//set drag resize
		DragResizer.makeResizable(this);
		//set content
		this.channelNameLabel = new Label(channelName);
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
		this.getChildren().add(new VBox(channelNameLabel, new FlowPane(muteButton, soloButton)));
	}

}
