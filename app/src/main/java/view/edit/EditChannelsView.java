package view.edit;

import Resplan.Starter;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import view.common.App;
import view.common.ChannelsView;
import view.common.TimeAxisSetter;
import view.common.ToolBarSetter;
import view.common.ViewDataImpl.Channel;
import view.common.ViewDataImpl.Clip;

public class EditChannelsView extends ChannelsView {
	
	private final static Paint muteColor = Paint.valueOf("#FF3333");
	private final static Paint soloColor = Paint.valueOf("#B8D4FF");
	
	public EditChannelsView(TimeAxisSetter timeAxisSetter, VBox channelsContentPane, VBox channelsInfoPane, ToolBarSetter toolBarSetter) {
		super(timeAxisSetter, channelsContentPane, channelsInfoPane, toolBarSetter);
	}
	
	@Override
	public Node drawClip(Clip clip) {
		Label title = new Label(clip.getTitle());
		return title;
	}

	@Override
	public Node drawInfos(Channel ch) {
		//buttons
		Button muteButton = new Button("M");
		Button soloButton = new Button("S");
		muteButton.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		soloButton.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		muteButton.setOnAction(e->Starter.getController().setMute(ch.getTitle()));
		soloButton.setOnAction(e->Starter.getController().setSolo(ch.getTitle()));
		App.getData().getChannel(ch.getTitle()).isMuted().addListener((obs,old,n)->{
			if(n.booleanValue()) {
				muteButton.setBackground(new Background(new BackgroundFill(muteColor, null, null)));
			} else {
				muteButton.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			}
		});
		App.getData().getChannel(ch.getTitle()).isSolo().addListener((obs, old, n)->{
			if(n.booleanValue()) {
				soloButton.setBackground(new Background(new BackgroundFill(soloColor, null, null)));
			} else {
				soloButton.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			}
		});
		Label groupLabel = new Label(ch.getGroup());
		groupLabel.setTextFill(Color.GRAY);
		groupLabel.setFont(Font.font(12));
		return new VBox(groupLabel, new FlowPane(muteButton, soloButton));
	}
	
}
