package view.common;

import Resplan.App;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import view.common.ViewDataImpl.Channel;
import view.common.ViewDataImpl.Clip;

public abstract class ChannelsView {
	
	public ChannelsView(TimeAxisSetter timeAxisSetter, VBox channelsContentPane, VBox channelsInfoPane) {
		App.getData().addChannelsDataListener(new MapChangeListener<Channel, ObservableList<Clip>>() {

			@Override
			public void onChanged(Change<? extends Channel, ? extends ObservableList<Clip>> c) {
				if(c.wasAdded()) {
					//--------CREATE VIEW COMPONENT-----
					Channel ch = c.getKey();
					ChannelContentView cw = new ChannelContentView(ch, timeAxisSetter.getAxis()){

						@Override
						public Node drawClipRegion(Clip clip) {
							return drawClip(clip);
						}
						
					};
					ChannelInfosView iw = new ChannelInfosView(ch) {

						@Override
						public Node drawInfosRegion(Channel ch) {
							return drawInfos(ch);
						}
						
					};
					//set resize
					iw.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
						cw.setMinHeight(iw.getHeight());
					});
					RegionHeightResizer.makeResizable(iw);
					//--------ADDING TO VIEW-----
					channelsInfoPane.getChildren().add(iw);
					channelsContentPane.getChildren().add(cw);
					c.getKey().addToViewAll(iw,cw);
				}
				if(c.wasRemoved()) {
					//-------REMOVE FROM VIEW
					channelsInfoPane.getChildren().removeAll(c.getKey().getViewSet());
				}
			}
		});
	}
	
	public abstract Node drawClip(Clip clip);
	
	public abstract Node drawInfos(Channel ch);
}
