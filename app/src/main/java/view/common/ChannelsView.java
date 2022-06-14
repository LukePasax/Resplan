package view.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import view.common.ViewDataImpl.Channel;
import view.common.ViewDataImpl.Clip;

public abstract class ChannelsView {
	
	/**
	 * Index of last channel of each group.
	 */
	private Map<String, Integer> groupIndexes = new HashMap<>();
	private Map<String, Color> groupColors = new HashMap<>();
	StringProperty selected = new SimpleStringProperty();
	
	public ChannelsView(TimeAxisSetter timeAxisSetter, VBox channelsContentPane, VBox channelsInfoPane, ToolBarSetter toolBarSetter) {
		App.getData().addChannelsDataListener(new MapChangeListener<Channel, ObservableList<Clip>>() {

			@Override
			public void onChanged(Change<? extends Channel, ? extends ObservableList<Clip>> c) {
				if(c.wasAdded()) {
					//--------CREATE VIEW COMPONENT-----
					Channel ch = c.getKey();
					ChannelContentView cw = new ChannelContentView(ch, timeAxisSetter.getAxis(), toolBarSetter){

						@Override
						public Node drawClipContent(Clip clip) {
							return drawClip(clip);
						}
						
					};
					ChannelInfosView iw = new ChannelInfosView(ch) {

						@Override
						public Node drawChannelInfos(Channel ch) {
							return drawInfos(ch);
						}
						
					};
					//set ch group color
					if(groupColors.get(ch.getGroup())==null) {
						var rand = new Random();
						groupColors.put(ch.getGroup(), Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
					}
					iw.setBorder(new Border(new BorderStroke(null, groupColors.get(ch.getGroup()), null, null, null, BorderStrokeStyle.SOLID, null, BorderStrokeStyle.NONE, null,new BorderWidths(6), null)));
					//set resize
					iw.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
						cw.setMinHeight(iw.getHeight());
					});
					RegionHeightResizer.makeResizable(iw);
					//select channel
					iw.setOnMouseClicked(e->selected.set(ch.getTitle()));
					cw.setOnMouseClicked(e->selected.set(ch.getTitle()));
					//--------ADDING TO VIEW-----
					if(!groupIndexes.containsKey(ch.getGroup())) {
						groupIndexes.put(ch.getGroup(), channelsInfoPane.getChildren().size());
					} else {
						groupIndexes.put(ch.getGroup(), groupIndexes.get(ch.getGroup())+1);
					}
					groupIndexes.entrySet().stream().filter(entry->{
						return groupIndexes.get(ch.getGroup())<=entry.getValue() && entry.getKey()!=ch.getGroup();
					}).forEach(entry->{
						groupIndexes.put(entry.getKey(), entry.getValue()+1);
					});
					channelsInfoPane.getChildren().add(groupIndexes.get(ch.getGroup()), iw);
					channelsContentPane.getChildren().add(groupIndexes.get(ch.getGroup()), cw);
					c.getKey().addToViewAll(iw,cw);
				}
				if(c.wasRemoved()) {
					//-------REMOVE FROM VIEW
					Channel ch = c.getKey();
					if(groupIndexes.get(ch.getGroup()).equals(0) || groupIndexes.containsValue(groupIndexes.get(ch.getGroup())-1)) {
						groupIndexes.remove(ch.getGroup()); //TODO
					}
					groupIndexes.entrySet().stream().filter(entry->{
						if(groupIndexes.get(ch.getGroup())==null) {
							return false;
						}
						return groupIndexes.get(ch.getGroup())<=entry.getValue();
					}).forEach(entry->{
						groupIndexes.put(entry.getKey(), entry.getValue()-1);
						System.out.println("map: " + entry.getKey() + "gr index: " + groupIndexes.get(entry.getKey()));
					});
					channelsContentPane.getChildren().removeAll(c.getKey().getViewSet());
					channelsInfoPane.getChildren().removeAll(c.getKey().getViewSet());
				}
			}
		});
	}
	
	public abstract Node drawClip(Clip clip);
	
	public abstract Node drawInfos(Channel ch);
	
	public void addSelectObserver(ChangeListener<String> changeListener) {
		selected.addListener(changeListener);
	}
}
