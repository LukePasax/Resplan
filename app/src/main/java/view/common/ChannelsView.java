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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import view.common.ViewDataImpl.Channel;
import view.common.ViewDataImpl.Clip;

/**
 * The view for a channel.
 * This class is responsable of drawing the channel content and channel infos.
 * Extend this class and implement {@code drawClip(Clip clip)} e {@code drawInfos(Channel channel)} 
 * for personalize the channel view.
 */
public abstract class ChannelsView {
	
	private static final Background SELECTION_BACKGROUND = new Background(new BackgroundFill(Paint.valueOf("#ECF0FD"), null, null));
	private static final int RGB_COLORS = 256;
	
	/**
	 * Index of last channel of each group.
	 */
	private final Map<String, Integer> groupIndexes = new HashMap<>();
	private static final Map<String, Color> GROUP_COLORS = new HashMap<>();
	private final StringProperty selected = new SimpleStringProperty();
	private final Random rand = new Random();
	
	public ChannelsView(final TimeAxisSetter timeAxisSetter, final VBox channelsContentPane, final VBox channelsInfoPane, final ToolBarSetter toolBarSetter) {
		App.getData().addChannelsDataListener(new MapChangeListener<Channel, ObservableList<Clip>>() {

			@Override
			public void onChanged(final Change<? extends Channel, ? extends ObservableList<Clip>> c) {
				if (c.wasAdded()) {
					//--------CREATE VIEW COMPONENT-----
					Channel ch = c.getKey();
					//set ch group color
					if (GROUP_COLORS.get(ch.getGroup()) == null) {
						GROUP_COLORS.put(ch.getGroup(), Color.rgb(rand.nextInt(RGB_COLORS), rand.nextInt(RGB_COLORS), rand.nextInt(RGB_COLORS)));
					}
					ChannelContentView cw = new ChannelContentView(ch, timeAxisSetter.getAxis(), toolBarSetter, GROUP_COLORS.get(ch.getGroup())) {

						@Override
						public Node drawClipContent(final Clip clip) {
							return drawClip(clip);
						}
					};
					ChannelInfosView iw = new ChannelInfosView(ch, GROUP_COLORS.get(ch.getGroup())) {

						@Override
						public Node drawChannelInfos(final Channel ch) {
							return drawInfos(ch);
						}
					};
					//set resize
					iw.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
						cw.setMinHeight(iw.getHeight());
					});
					RegionHeightResizer.makeResizable(iw);
					//select channel
					iw.setOnMouseClicked(e -> selected.set(ch.getTitle()));
					cw.setOnMouseClicked(e -> {
						if (toolBarSetter.getCurrentTool().equals(Tool.CURSOR) || toolBarSetter.getCurrentTool().equals(Tool.MOVE)) {
							selected.set(ch.getTitle());
						}
						cw.clickEvent(e);
					});
					selected.addListener((obs, old, n) -> {
						if (n.equals(ch.getTitle())) {
							iw.setBackground(SELECTION_BACKGROUND);
							cw.setBackground(SELECTION_BACKGROUND);
						} else {
							iw.setBackground(null);
							cw.setBackground(null);
						}
					});
					//--------ADDING TO VIEW-----
					if (!groupIndexes.containsKey(ch.getGroup())) {
						groupIndexes.put(ch.getGroup(), channelsInfoPane.getChildren().size());
					} else {
						groupIndexes.put(ch.getGroup(), groupIndexes.get(ch.getGroup()) + 1);
					}
					groupIndexes.entrySet().stream().filter(entry -> {
						return groupIndexes.get(ch.getGroup()) <= entry.getValue() && !entry.getKey().equals(ch.getGroup());
					}).forEach(entry -> {
						groupIndexes.put(entry.getKey(), entry.getValue() + 1);
					});
					channelsInfoPane.getChildren().add(groupIndexes.get(ch.getGroup()), iw);
					channelsContentPane.getChildren().add(groupIndexes.get(ch.getGroup()), cw);
					c.getKey().addToViewAll(iw, cw);
				}
				if (c.wasRemoved()) {
					//-------REMOVE FROM VIEW
					Channel ch = c.getKey();
					boolean last = (groupIndexes.get(ch.getGroup()) <= 0 || groupIndexes.containsValue(groupIndexes.get(ch.getGroup()) - 1));
					groupIndexes.entrySet().stream().filter(entry -> {
						if (groupIndexes.get(ch.getGroup()) == null) {
							return false;
						}
						return groupIndexes.get(ch.getGroup()) <= entry.getValue();
					}).forEach(entry -> {
						groupIndexes.put(entry.getKey(), entry.getValue() - 1);
					});
					if (last) {
						groupIndexes.remove(ch.getGroup());
					}
					channelsContentPane.getChildren().removeAll(c.getKey().getViewSet());
					channelsInfoPane.getChildren().removeAll(c.getKey().getViewSet());
				}
			}
		});
	}
	
	public abstract Node drawClip(Clip clip);
	
	public abstract Node drawInfos(Channel ch);
	
	/**
	 * Register a listener of the selected channel.
	 * 
	 * @param  changeListener  The listener to add.
	 */
	public void addSelectListener(final ChangeListener<String> changeListener) {
		selected.addListener(changeListener);
	}
}
