package view.common;

import Resplan.Starter;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import view.common.ViewDataImpl.Channel;

/**
 * The view for a channel info.
 * Extends this class and override {@code drawChannelInfos(Clip clip)} for 
 * personalize the infos view.
 */
public abstract class ChannelInfosView extends AnchorPane {
	
	private static final Paint BORDER_COLOR = Paint.valueOf("#999999");
	
	private static final double RIGHT_BORDER_WIDTH = 6;
	
	private final Channel ch;
	private final VBox infos = new VBox();
	
	public ChannelInfosView(final Channel ch, final Color color) {
		super();
		this.ch = ch;
		//borderLayout
		this.setBorder(new Border(new BorderStroke(BORDER_COLOR, color, BORDER_COLOR, color, 
				BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, null, new BorderWidths(0, RIGHT_BORDER_WIDTH, 1, 0), null)));
		//set content
		infos.getChildren().add(drawChannelInfos(ch));
		Label titleLabel = new Label(ch.getTitle());
		titleLabel.setTextFill(Color.BLACK);
		VBox content = new VBox(titleLabel, infos);
		this.getChildren().add(content);
		AnchorPane.setBottomAnchor(content, 0.0);
		AnchorPane.setTopAnchor(content, 0.0);
		AnchorPane.setRightAnchor(content, 0.0);
		AnchorPane.setLeftAnchor(content, 0.0);
		//set context menu
		MenuItem remove = new MenuItem("Remove");
		remove.setOnAction(a -> Starter.getController().deleteChannel(this.ch.getTitle()));
		ContextMenu menu = new ContextMenu(remove);
		this.setOnContextMenuRequested(e -> menu.show(this, e.getScreenX(), e.getScreenY()));
	}
	
	public abstract Node drawChannelInfos(Channel ch);

	/**
	 * Get the channel displayed by this channel infos pane.
	 * 
	 * @return the channel displayed by this channel infos pane.
	 */
	public Channel getChannel() {
		return this.ch;
	}
}
